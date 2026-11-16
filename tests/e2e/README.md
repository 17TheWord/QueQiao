# QueQiao 端到端验证

起一个真实 Minecraft 服务端，同时从 WebSocket 侧和玩家侧施压，
验证「API → 消息构建 → 平台下发 → 客户端渲染」整条链路。

重点在最后一环：**API 返回 200 不代表玩家收到**，这里两者都验。

## 快速开始

```bash
cd tests/e2e
npm i

# 起服 + 测试 + 清理，一条命令
bash run.sh ../../fabric/fabric-1.21.11
```

服务端已在跑时，只跑测试：

```bash
bash run.sh ../../fabric/fabric-1.21.11 --skip-server
```

## 参数

| 参数 | 默认 | 说明 |
| --- | --- | --- |
| `--mc-port` | 25565 | MC 服务端端口 |
| `--ws-port` | 8080 | QueQiao WebSocket 端口 |
| `--skip-server` | — | 跳过启动，直接测已运行的服务端 |

MC 版本从目标目录名推导（`fabric-1.21.11` → `1.21.11`），假玩家会按这个版本握手。

## 覆盖范围

| 分组 | 检查项 |
| --- | --- |
| API | `get_status` 字段完整性、`broadcast`、`send_actionbar`、`send_title` |
| 错误处理 | 未知 api → 404、缺 `api` → 400、畸形 JSON 后连接存活 |
| 事件 | `player_join` / `player_chat` / `player_quit` 推送 |
| 送达 | 广播与私聊**确实出现在假玩家客户端** |
| 格式 | 原生组件可用；Adventure 包装格式应被拒绝 |

## 前置条件

- 服务端 `run/server.properties` 中 `online-mode=false`，否则假玩家无法用离线身份登录。
  脚本首次运行会自动生成一份，已存在则复用。
- 端口 25565 / 25575 / 8080 空闲。
- Node 18+。

## 已知的平台差异（不算失败）

- `send_command` 在 fabric 返回 `503 send_command is not supported now`，属未实现，脚本按 `skip` 处理。
- `send_private_msg` 对不存在的 UUID 返回 `200 success`，是当前语义，脚本不强制判错。

## 在 CI 里用

见 `.github/workflows/e2e.yml`。核心是把「起服」和「测试」拆开：
`run.sh` 负责编排，`e2e.js` 只做断言，退出码即结果。
