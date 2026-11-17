#!/usr/bin/env bash
# Paper / Folia 的端到端验证
#
# 与 run.sh 的区别：这两个加载器的 build.gradle 里只有 `id 'java'` 与 shadow，
# 没有 runServer 任务，必须从外部提供官方服务端：
#   查 Fill API → 下载 paper jar（带缓存）→ 构建插件 → 放进 plugins/ → 启动 → 测试
#
# 用法：
#   ./paper.sh <目标目录>                例：./paper.sh ../../paper/paper-1.17.1
#   ./paper.sh <目标目录> --java <路径>   显式指定 JDK（Paper 1.17 需要 JDK 17，不能用 21）
#   ./paper.sh <目标目录> --skip-server   服务端已在运行时，只跑测试

set -uo pipefail

TARGET="${1:-}"
shift || true

MC_PORT=25565
WS_PORT=8080
JAVA_BIN=""
SKIP_SERVER=0

while [[ $# -gt 0 ]]; do
  case "$1" in
    --mc-port) MC_PORT="$2"; shift 2 ;;
    --ws-port) WS_PORT="$2"; shift 2 ;;
    --java) JAVA_BIN="$2"; shift 2 ;;
    --skip-server) SKIP_SERVER=1; shift ;;
    *) echo "未知参数: $1"; exit 64 ;;
  esac
done

if [[ -z "$TARGET" || ! -d "$TARGET" ]]; then
  echo "用法: ./paper.sh <目标目录> [--java <jdk路径>] [--mc-port N] [--ws-port N] [--skip-server]"
  exit 64
fi

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
TARGET_ABS="$(cd "$TARGET" && pwd)"
BASE="$(basename "$TARGET_ABS")"      # 例：paper-1.17.1
LOADER="${BASE%%-*}"                   # paper / folia
MC_VERSION="${BASE#*-}"                # 1.17.1
RUN_DIR="$TARGET_ABS/run"
LOG="$RUN_DIR/server-e2e.log"
SERVER_JAR="$RUN_DIR/paper-server.jar"
MOD_VERSION="$(tr -d '\r\n' < "$ROOT/version.txt")"

SERVER_PID=""

cleanup() {
  local code=$?
  if [[ -n "$SERVER_PID" ]] && kill -0 "$SERVER_PID" 2>/dev/null; then
    echo "→ 停止服务端 (PID $SERVER_PID)"
    kill "$SERVER_PID" 2>/dev/null
    sleep 2
    kill -9 "$SERVER_PID" 2>/dev/null
  fi
  exit $code
}
trap cleanup EXIT INT TERM

port_listening() {
  local p="$1"
  if command -v ss >/dev/null 2>&1; then
    ss -ltn 2>/dev/null | grep -q ":$p "
  elif command -v netstat >/dev/null 2>&1; then
    netstat -ano 2>/dev/null | grep -q ":$p .*LISTENING" \
      || netstat -ltn 2>/dev/null | grep -q ":$p "
  else
    return 1
  fi
}

detect_java() {
  [[ -n "$JAVA_BIN" ]] && { echo "$JAVA_BIN"; return; }

  # CI：setup-java 已按矩阵的 java 版本设好 JAVA_HOME，直接采用
  if [[ -n "${JAVA_HOME:-}" && -x "$JAVA_HOME/bin/java" ]]; then
    echo "$JAVA_HOME/bin/java"; return
  fi

  local cand
  # 本地：Paper 1.17 需要 JDK 17，优先找 Gradle toolchain 下载的那份
  for cand in "$(dirname "$ROOT")/.jdks"/*17*/bin/java \
              "${GRADLE_USER_HOME:-$HOME/.gradle}/jdks"/*17*/bin/java \
              /c/*/Build/Gradle/jdks/*17*/bin/java.exe \
              "$HOME"/.gradle/jdks/*17*/bin/java; do
    [[ -x "$cand" ]] && { echo "$cand"; return; }
  done
  command -v java 2>/dev/null
}

prepare() {
  mkdir -p "$RUN_DIR/plugins"
  [[ -f "$RUN_DIR/eula.txt" ]] || printf 'eula=true\n' > "$RUN_DIR/eula.txt"
  if [[ ! -f "$RUN_DIR/server.properties" ]]; then
    cat > "$RUN_DIR/server.properties" <<EOF
online-mode=false
server-port=$MC_PORT
motd=QueQiao E2E
max-players=5
level-type=flat
generate-structures=false
spawn-monsters=false
spawn-npcs=false
view-distance=4
EOF
  fi
}

# 从 PaperMC Fill API 取最新 STABLE build 的下载地址
fetch_server_jar() {
  if [[ -f "$SERVER_JAR" ]]; then
    echo "→ 复用已下载的服务端 jar（$(du -h "$SERVER_JAR" | cut -f1)）"
    return 0
  fi

  local api="https://fill.papermc.io/v3/projects/${LOADER}/versions/${MC_VERSION}/builds"
  echo "→ 查询 Fill API：$LOADER $MC_VERSION"
  local json url
  json=$(curl -s --max-time 30 -H "User-Agent: queqiao-e2e/1.0" "$api")

  url=$(printf '%s' "$json" | python -c "
import json,sys
try:
    d=json.load(sys.stdin)
    builds=[b for b in d if b.get('channel')=='STABLE'] or d
    print(builds[0]['downloads']['server:default']['url'])
except Exception:
    print('')
" 2>/dev/null)

  if [[ -z "$url" ]]; then
    echo "✗ 未能从 Fill API 获取下载地址（可能该版本已不再提供）"
    echo "  手动下载后放到：$SERVER_JAR"
    return 1
  fi

  echo "→ 下载：$url"
  curl -sL --max-time 300 -o "$SERVER_JAR" "$url" || { echo "✗ 下载失败"; return 1; }
  echo "→ 已保存 $(du -h "$SERVER_JAR" | cut -f1)"
}

# 构建插件并部署到 plugins/
build_and_deploy_plugin() {
  echo "→ 构建插件：$TARGET_ABS"
  ( cd "$TARGET_ABS" && ./gradlew build -q ) || { echo "✗ 构建失败"; return 1; }

  # shadowJar 的输出目录被配置到仓库根的 QueQiao-jar/<version>/，
  # 而不是 build/libs（后者只是不含依赖的瘦 jar）
  local jar="$ROOT/QueQiao-jar/${MOD_VERSION}/QueQiao-${LOADER}+${MC_VERSION}-${MOD_VERSION}.jar"
  if [[ ! -f "$jar" ]]; then
    echo "✗ 未找到构建产物：$jar"
    ls "$ROOT/QueQiao-jar/${MOD_VERSION}/" 2>/dev/null | head -5
    return 1
  fi

  rm -f "$RUN_DIR/plugins/"*.jar
  cp "$jar" "$RUN_DIR/plugins/"
  echo "→ 已部署：$(basename "$jar")（$(du -h "$jar" | cut -f1)）"
}

if [[ "$SKIP_SERVER" -eq 0 ]]; then
  for p in "$MC_PORT" "$WS_PORT"; do
    if port_listening "$p"; then
      echo "✗ 端口 $p 已被占用"
      exit 65
    fi
  done

  JAVA_BIN="$(detect_java)"
  if [[ -z "$JAVA_BIN" ]]; then
    echo "✗ 未找到可用的 JDK 17（Paper 1.17 不接受更高版本）"
    exit 66
  fi
  echo "→ JDK：$JAVA_BIN（$("$JAVA_BIN" -version 2>&1 | head -1 | cut -d'"' -f2)）"

  prepare || exit 1
  fetch_server_jar || exit 1
  build_and_deploy_plugin || exit 1

  echo "→ 启动服务端"
  ( cd "$RUN_DIR" && "$JAVA_BIN" -Xmx2G -jar "$(basename "$SERVER_JAR")" nogui ) > "$LOG" 2>&1 &
  SERVER_PID=$!

  echo "→ 等待 WebSocket 就绪（端口 $WS_PORT，最多 300 秒）…"
  READY=0
  for _ in $(seq 1 150); do
    if port_listening "$WS_PORT"; then READY=1; break; fi
    if ! kill -0 "$SERVER_PID" 2>/dev/null; then break; fi
    sleep 2
  done

  if [[ "$READY" -ne 1 ]]; then
    echo "✗ 服务端未就绪，日志末尾："
    tail -25 "$LOG" 2>/dev/null
    exit 67
  fi
  echo "→ 服务端就绪"
  grep -a -E "Done \(|WebSocket Server" "$LOG" 2>/dev/null | tail -2 | sed 's/^/  /'
fi

cd "$ROOT/tests/e2e" || exit 68
[[ -d node_modules ]] || npm i --no-audit --no-fund > /dev/null 2>&1

echo "→ 执行 e2e 验证"
node e2e.js --ws-port "$WS_PORT" --mc-port "$MC_PORT" --version "$MC_VERSION"
exit $?
