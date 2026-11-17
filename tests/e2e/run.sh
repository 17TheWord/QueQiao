#!/usr/bin/env bash
# QueQiao 端到端验证编排脚本
#
# 用法：
#   ./run.sh <目标目录>            例：./run.sh fabric/fabric-26.1
#   ./run.sh <目标目录> --skip-server   服务端已在运行时，只跑测试
#   ./run.sh <目标目录> --mc-port 25566 --ws-port 8081
#
# 覆盖 fabric / forge / neoforge / paper：四者都有可用的 runServer 任务。
# spigot（需 BuildTools）与 velocity（代理端）暂不在本脚本范围内。
#
# 做完三件事：准备运行配置 → 起服并等就绪 → 跑 e2e.js → 清理。

set -uo pipefail

TARGET="${1:-}"
shift || true

SKIP_SERVER=0
MC_PORT=25565
WS_PORT=8080
while [[ $# -gt 0 ]]; do
  case "$1" in
    --skip-server) SKIP_SERVER=1; shift ;;
    --mc-port) MC_PORT="$2"; shift 2 ;;
    --ws-port) WS_PORT="$2"; shift 2 ;;
    *) echo "未知参数: $1"; exit 64 ;;
  esac
done

if [[ -z "$TARGET" || ! -d "$TARGET" ]]; then
  echo "用法: ./run.sh <目标目录> [--skip-server] [--mc-port N] [--ws-port N]"
  echo "可用目录示例:"
  ls -d ../fabric/*-* ../forge/*-* 2>/dev/null | grep -v origin | head -5
  exit 64
fi

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
TARGET_ABS="$(cd "$TARGET" && pwd)"
RUN_DIR="$TARGET_ABS/run"
LOG="$RUN_DIR/server-e2e.log"

# 从目录名推导 MC 版本： fabric-26.1 → 26.1；paper-26.1.1 → 26.1.1
BASE="$(basename "$TARGET_ABS")"
MC_VERSION="${BASE##*-}"
[[ "$MC_VERSION" =~ ^[0-9] ]] || MC_VERSION="26.1"

SERVER_PID=""

cleanup() {
  local code=$?
  if [[ -n "$SERVER_PID" ]] && kill -0 "$SERVER_PID" 2>/dev/null; then
    echo "→ 停止服务端 (PID $SERVER_PID)"
    kill "$SERVER_PID" 2>/dev/null
    sleep 3
    kill -9 "$SERVER_PID" 2>/dev/null
  fi
  # 兜底：gradlew 会 fork 出真正的 MC 进程，父进程被杀后子进程可能残留
  # devlaunchinjector 覆盖 fabric/forge/neoforge，paper 由 run-paper 直接起官方 jar
  case "$(uname -s)" in
    MINGW*|MSYS*|CYGWIN*)
      powershell -NoProfile -Command "Get-CimInstance Win32_Process -Filter \"name='java.exe'\" | Where-Object { \$_.CommandLine -like '*devlaunchinjector*' -or \$_.CommandLine -like '*paperclip*' } | ForEach-Object { Stop-Process -Id \$_.ProcessId -Force -ErrorAction SilentlyContinue }" 2>/dev/null
      ;;
    *)
      pkill -f devlaunchinjector 2>/dev/null
      pkill -f paperclip 2>/dev/null
      ;;
  esac
  exit $code
}
trap cleanup EXIT INT TERM

prepare_run_dir() {
  mkdir -p "$RUN_DIR"
  [[ -f "$RUN_DIR/eula.txt" ]] || printf 'eula=true\n' > "$RUN_DIR/eula.txt"

  if [[ ! -f "$RUN_DIR/server.properties" ]]; then
    cat > "$RUN_DIR/server.properties" <<EOF
online-mode=false
server-port=$MC_PORT
motd=QueQiao E2E
max-players=5
level-type=minecraft\:flat
generate-structures=false
spawn-monsters=false
spawn-npcs=false
spawn-animals=false
view-distance=4
simulation-distance=4
enable-rcon=true
rcon.port=$((MC_PORT + 10))
rcon.password=queqiao-e2e
EOF
    echo "→ 已生成 server.properties（离线模式 / 超平坦 / 开 RCON）"
  else
    echo "→ 复用已有 server.properties"
    if ! grep -q '^online-mode=false' "$RUN_DIR/server.properties"; then
      echo "  ! 警告：online-mode 不是 false，假玩家将无法登录"
      echo "  ! 请在 $RUN_DIR/server.properties 中设置 online-mode=false"
    fi
  fi
}

port_listening() {
  local p="$1"
  if command -v ss >/dev/null 2>&1; then
    ss -ltn 2>/dev/null | grep -q ":$p "
  elif command -v netstat >/dev/null 2>&1; then
    # Windows 的 netstat 不支持 -ltn，只能 -ano 匹配 LISTENING；Linux 用 -ltn
    netstat -ano 2>/dev/null | grep -q ":$p .*LISTENING" \
      || netstat -ltn 2>/dev/null | grep -q ":$p "
  else
    return 1
  fi
}

check_ports() {
  for p in "$MC_PORT" "$WS_PORT"; do
    if port_listening "$p"; then
      echo "✗ 端口 $p 已被占用，请先释放或改用 --mc-port / --ws-port"
      exit 65
    fi
  done
}

if [[ "$SKIP_SERVER" -eq 0 ]]; then
  check_ports
  prepare_run_dir

  echo "→ 启动服务端：$TARGET_ABS（MC $MC_VERSION）"
  # 仓库里的 gradlew 没有 exec bit（git 记录为 100644），
  # Linux 上直接 ./gradlew 会 Permission denied；Windows 因不认 exec bit 所以本地测不出来
  chmod +x "$TARGET_ABS/gradlew" 2>/dev/null || true
  ( cd "$TARGET_ABS" && ./gradlew runServer --console=plain ) > "$LOG" 2>&1 &
  SERVER_PID=$!

  echo "→ 等待 WebSocket Server 就绪（端口 $WS_PORT，最多 600 秒）…"
  READY=0
  for _ in $(seq 1 300); do
    # 探端口比 grep 日志可靠：gradlew 输出重定向到文件时会被块缓冲，日志可能迟迟不落盘
    if port_listening "$WS_PORT"; then READY=1; break; fi
    if ! kill -0 "$SERVER_PID" 2>/dev/null; then break; fi
    sleep 2
  done

  if [[ "$READY" -ne 1 ]]; then
    echo "✗ 服务端未就绪，日志末尾："
    tail -30 "$LOG" 2>/dev/null
    exit 66
  fi
  echo "→ 服务端就绪"
  grep -E "Done \(|WebSocket Server 在" "$LOG" 2>/dev/null | tail -2 | sed 's/^/  /'
fi

cd "$ROOT/tests/e2e" || exit 67
[[ -d node_modules ]] || npm i --no-audit --no-fund > /dev/null 2>&1

echo "→ 执行 e2e 验证"
node e2e.js --ws-port "$WS_PORT" --mc-port "$MC_PORT" --version "$MC_VERSION"
TEST_CODE=$?
exit $TEST_CODE
