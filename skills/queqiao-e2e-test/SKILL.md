---
name: "queqiao-e2e-test"
description: "对 QueQiao 做端到端验证：启动真实 Minecraft 服务端、用假玩家触发事件、通过 WebSocket 打全量 API，并确认消息真的到达客户端。适用于改动事件/消息构建逻辑、新增加载器版本后的验收，以及排查「API 返回 200 但玩家没收到」「事件不推送」「连接上却没响应」这类问题。"
---

# QueQiao 端到端验证

起一个真实服务端，从 WebSocket 和玩家客户端两侧同时施压，
验证「API 调用 → 消息构建 → 平台下发 → 客户端收到」整条链路，
而不只是看 HTTP 式的返回码。

## 何时使用

- 改动了 `EventProcessor`、`PlatformContext`、`FabricTool` 等适配层。
- 新增加载器或版本，需要确认 API 与事件真的可用。
- 排查「API 返回 200 但没效果」「玩家事件不推送」「WebSocket 连上但无响应」。

不用于：纯构建失败（交给 CI）、文档改动、只改 core 依赖版本。

## 测试拓扑

```
Node 测试脚本 ──ws──▶ QueQiao WsServer (127.0.0.1:8080) ──▶ 平台适配层 ──▶ MC 服务端
       └────────── mineflayer 假玩家 ──────▶ MC 服务端 (127.0.0.1:25565)
```

两侧同时接入，才能验证「事件从游戏流向 WS」和「指令从 WS 流向游戏」两个方向。

## 一、准备服务端

在 `<loader>/<loader>-<version>/run/` 下建两个文件（不提交，属运行时产物）：

`eula.txt`

```
eula=true
```

`server.properties`（关键是前几项）

```properties
online-mode=false
server-port=25565
motd=QueQiao Test Server
max-players=5
level-type=minecraft\:flat
generate-structures=false
spawn-monsters=false
spawn-npcs=false
spawn-animals=false
view-distance=4
simulation-distance=4
enable-rcon=true
rcon.port=25575
rcon.password=queqiao-test
```

- `online-mode=false` 必填，否则 mineflayer 无法用离线身份登录。
- `level-type=minecraft\:flat` + 关掉生成物，世界秒开（实测 `Done (0.585s)`）。
- RCON 开着，便于交叉验证与优雅停机。

确认 25565 / 25575 / 8080 三个端口空闲：

```bash
netstat -ano | grep -E ":25565|:25575|:8080"
```

## 二、启动

```bash
cd <loader>/<loader>-<version>
./gradlew runServer --console=plain
```

判定就绪：日志出现 `Done (x.xxxs)! For help, type "help"`，随后应看到

```
(QueQiao) WebSocket Server 在 127.0.0.1:8080 启动...
```

首次运行会生成 `run/config/QueQiao/config.yml`（默认 `websocket_server.enable=true`、`port=8080`、`access_token` 空）。

## 三、握手约定（最容易踩的坑）

`WsServer.validateHandshake` 会校验三个字段，任一不满足直接关闭连接：

| 字段 | 位置 | 要求 |
| --- | --- | --- |
| `x-self-name` | 请求头或 URL query | 需 URL 编码；解码后**必须等于配置里的 `server_name`**（默认 `Server`） |
| `x-client-origin` | 请求头或 URL query | **不能是 `minecraft`**，服务端用它拒绝自连 |
| `Authorization` | 请求头或 URL query | 格式 `Bearer <access_token>`；**仅当 `access_token` 非空时校验** |

缺失时服务端日志只有一句「连接请求头中缺少服务器名，将断开连接」，
客户端侧的现象却是「连接成功、请求全部无响应、最后 readyState=3」，极具迷惑性。

```js
new WebSocket('ws://127.0.0.1:8080', {
  headers: {
    'x-self-name': encodeURIComponent('Server'),
    'x-client-origin': 'e2e-test',
  },
});
```

## 四、API 清单

请求：`{ "api": "<name>", "data": {...}, "echo": "<任意字符串>" }`
响应：`{ "api", "code", "post_type": "response", "status", "message", "data", "echo" }`

| api | data | 实测 |
| --- | --- | --- |
| `broadcast` | `{ message: <组件> }` | 200，全服收到 |
| `send_actionbar` | `{ message: <组件> }` | 200 |
| `send_title` | `{ title, subtitle, fadein, stay, fadeout }` | 200，时长单位 tick |
| `send_private_msg` | `{ uuid, nickname, message }` | 200；**uuid 不存在时也返回 200**（见坑） |
| `get_status` | `{}` | 200，返回 `server_type` / `server_version` / `cpu_information` / `memory_information` / `server_list_ping` |
| `send_command` | `{ command }` | fabric 返回 **503** `send_command is not supported now`（平台未实现） |
| `send_rcon_command` | `{ command }` | 需 `rcon.enable=true`，未验证 |
| `send_msg` | — | 协议常量里有，未验证 |

状态码：`200` 成功、`400` 参数错误、`404` 未知 API、`500` 内部错误、`503` 服务不可用。

错误处理已具备：未知 api → 404，缺 `api` 字段 → 400，发送畸形 JSON 不会断连、后续请求正常。

## 五、消息组件格式

**只接受 Minecraft 原生 JSON 文本组件**，不接受 Adventure 的 `{"type":"text","data":{...}}` 包装。
发包装格式会 500，服务端异常形如
`IllegalStateException: Failed to parse either ... No key text in MapLike[...]`。

fabric 侧由 `FabricTool.buildComponent` 决定实现：`>=1.21.6` 走 `TextCodecs.CODEC`，
`>=1.21` 走 `Text.Serialization` + registry，`>=1.20.4` 走 `Text.Serialization`，更早走 `Text.Serializer`。
这些入口都直接吃原生组件，**core 不做包装格式的转换**。

可用格式（实测全部 200）：

```
{"text":"x"}                  纯文本
[{"text":"x"}]                数组
"x"                           纯字符串
{"text":"x","color":"aqua"}   带样式
```

### 交互组件的版本断崖

纯文本与样式全版本通用，但 `click` / `hover` 在 **1.21.5** 前后不兼容：

| 版本 | click 字段 | hover 字段 | 值字段 |
| --- | --- | --- | --- |
| 1.13 – 1.20.4 | `clickEvent` | `hoverEvent` | 统一 `value`；hover 的文本在 `contents` |
| 1.20.5 – 1.21.4 | `clickEvent` | `hoverEvent` | 统一 `value` |
| **1.21.5+** | `click_event` | `hover_event` | **按 action 分**：`url` / `command` / `value` |

旧版本收到新字段名不会报错，只是**静默失去交互效果**——比 500 更难发现。
发交互组件前先确认目标版本，或降级为纯样式文本。

## 六、事件推送

服务端主动推送，`post_type` 区分类型：

| 事件 | post_type | 关键字段 |
| --- | --- | --- |
| 玩家加入 / 退出 | `notice` | `player.nickname` / `uuid` / `address` / `health` |
| 玩家聊天 | `message` | `message`（原文）+ `player` |

订阅开关在 `config.yml` 的 `subscribe_event.*`，默认全开。

## 七、脚本骨架

依赖：`npm i ws mineflayer`

```js
const WebSocket = require('ws');
const mineflayer = require('mineflayer');

const ws = new WebSocket('ws://127.0.0.1:8080', {
  headers: { 'x-self-name': encodeURIComponent('Server'), 'x-client-origin': 'e2e' },
});

let seq = 0;
const pending = new Map();
const events = [];

ws.on('message', (raw) => {
  const m = JSON.parse(raw.toString());
  if (m.post_type === 'response' && m.echo && pending.has(m.echo)) {
    const p = pending.get(m.echo);
    clearTimeout(p.timer);
    pending.delete(m.echo);
    p.resolve(m);
  } else {
    events.push(m);           // 服务端推送
  }
});

function api(name, data, timeout = 10000) {
  return new Promise((resolve, reject) => {
    const echo = `t${++seq}`;
    const timer = setTimeout(() => { pending.delete(echo); reject(new Error('超时')); }, timeout);
    pending.set(echo, { resolve, timer });
    ws.send(JSON.stringify({ api: name, data, echo }));
  });
}

ws.on('open', async () => {
  console.log(await api('get_status', {}));

  // 假玩家：关键是用 offline 方式，版本号要与服务端一致
  const bot = mineflayer.createBot({
    host: '127.0.0.1', port: 25565,
    username: 'QQTester', auth: 'offline', version: '1.21.11',
  });
  const got = [];
  bot.on('message', (j) => got.push(j.toString()));

  await new Promise((res) => bot.once('spawn', res));

  await api('broadcast', { message: { text: 'DELIVER-CHECK' } });
  setTimeout(() => {
    // 这一步才是真验证：返回 200 不代表送达
    console.log('客户端收到:', got.some((s) => s.includes('DELIVER-CHECK')));
    bot.quit();
    ws.close();
  }, 2000);
});
```

## 八、故障对照表

| 现象 | 原因 | 处理 |
| --- | --- | --- |
| 连接成功但请求全超时，最终 `readyState=3` | 缺 `x-self-name` 或它不等于 `server_name` | 补握手头，名称与 config.yml 对齐 |
| `code=500`，日志含 `No key text in MapLike` | 消息用了 `{"type":"text","data":{...}}` 包装 | 改原生组件 `{"text":"..."}` |
| `code=503` + `send_command is not supported now` | 该平台未实现 | 平台能力差异，非缺陷 |
| 交互效果在新版本失效但不报错 | 1.21.5 前后字段名不同 | 按目标版本选 `clickEvent` / `click_event` |
| `send_private_msg` 返回 200 但目标没收到 | uuid 不存在时也未报错 | 已知语义问题，调用方需自行确认在线状态 |
| 假玩家登录失败 | `online-mode=true`，或 `version` 与服务端不符 | 改 `online-mode=false`，版本号对齐 |

## 九、清理

```bash
# 优雅停机优先走 RCON；直接杀进程也可，测试世界无保留价值
# 确认三个端口都释放
netstat -ano | grep -E ":25565|:25575|:8080"
```

`run/` 目录属运行时产物，不应提交。

## 验收标准

一次合格的验证至少覆盖：

1. `get_status` 返回正确的 `server_type` 与 `server_version`。
2. `broadcast` / `send_title` / `send_actionbar` 返回 200，且**假玩家客户端确实收到文本**。
3. 假玩家加入、发言、退出，三个事件都推送到 WS 侧。
4. 错误路径：未知 api → 404、缺 `api` → 400、畸形 JSON 后连接仍可用。
5. 平台能力差异（如 `send_command` 503）记录在案，不当作失败。
