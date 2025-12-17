<div align="right">
🌍<a href="https://github.com/17TheWord/QueQiao/blob/main/README_EN.md">English</a> / 中文
</div>

<p align="center">
  <img src="https://raw.githubusercontent.com/17TheWord/nonebot-adapter-minecraft/main/assets/logo.png" width="200" height="200" alt="ChatImage">
</p>

<div align="center">

# 鹊桥

✨ Minecraft 服务端 Mod/Plugin，实时接收玩家事件、API广播消息 ✨

</div>

<p align="center">
  <a href="https://github.com/17TheWord/QueQiao/blob/main/LICENSE">
    <img src="https://img.shields.io/badge/license-MIT-green" alt="license">
  </a>
  <a href="https://github.com/17TheWord/QueQiao/releases">
    <img src="https://img.shields.io/github/v/release/17TheWord/QueQiao" alt="release">
  </a>
</p>

<p align="center">
  <a href="https://www.spigotmc.org">
    <img src="https://img.shields.io/badge/SpigotMC-1.12.2--latest-blue?logo=data:image/png;base64,..." alt="spigotmc"/>
  </a>
  <a href="https://docs.papermc.io/paper">
    <img src="https://img.shields.io/badge/PaperMC-1.17.1--latest-blue?logo=data:image/png;base64,..." alt="velocity">
  </a>
  <a href="https://docs.papermc.io/folia">
    <img src="https://img.shields.io/badge/Folia-1.21.4--latest-blue?logo=data:image/png;base64,..." alt="velocity">
  </a>
  <a href="https://papermc.io/software/velocity">
    <img src="https://img.shields.io/badge/Velocity-3.3.0-blue?logo=data:image/png;base64,..." alt="velocity">
  </a>
</p>

<p align="center">
  <a href="https://files.minecraftforge.net">
    <img src="https://img.shields.io/badge/Forge-1.7.10--1.21-blue?logo=data:image/png;base64,..." alt="forge">
  </a>
  <a href="https://fabricmc.net">
    <img src="https://img.shields.io/badge/Fabric-1.16.5--1.21.8-blue?logo=data:image/png;base64,..." alt="fabric">
  </a>
  <a href="https://neoforged.net/">
    <img src="https://img.shields.io/badge/NeoForge-1.21.1-blue?logo=data:image/png;base64,..." alt="fabric">
  </a>
</p>

<p align="center">
  <a href="https://queqiao-docs.pages.dev">📖Docs</a>
  ·
  <a href="https://modrinth.com/plugin/queqiao">⬇️Modrinth</a>
  ·
  <a href="https://www.curseforge.com/minecraft/mc-mods/queqiao">⬇️CurseForge</a>
  ·
  <a href="https://github.com/17TheWord/QueQiao/issues">🐛Submit Suggestion/Bug</a>
</p>

## 介绍

- 将服务端**玩家事件**以 `Json` 格式通过 `Websocket` 分发。
  - 已实现的 [事件](https://queqiao-docs.pages.dev/events/v2/)
    - [`玩家聊天`](https://queqiao-docs.pages.dev/events/v2/message/player-chat-event.html)
    - [`玩家命令`](https://queqiao-docs.pages.dev/events/v2/message/player-command-event.html)
    - [`玩家死亡`](https://queqiao-docs.pages.dev/events/v2/notice/player-death-event.html)
    - [`玩家加入`](https://queqiao-docs.pages.dev/events/v2/notice/player-join-event.html)
    - [`玩家离开`](https://queqiao-docs.pages.dev/events/v2/notice/player-quit-event.html)
    - [`玩家成就`](https://queqiao-docs.pages.dev/events/v2/notice/player-achievement-event.html)
- 通过 `Websocket` 接收 `Json` 消息，并广播至游戏玩家。
    - 已实现的 [接口](https://queqiao-docs.pages.dev/api/v2/broadcast.html)
        - [`广播消息`](https://queqiao-docs.pages.dev/api/v2/broadcast.html)
        - [`私聊消息`](https://queqiao-docs.pages.dev/api/v2/private-message.html)
        - [`标题 & 子标题`](https://queqiao-docs.pages.dev/api/v2/title.html)
        - [`动画栏`](https://queqiao-docs.pages.dev/api/v2/action-bar.html)
        - [`Rcon 命令`](https://queqiao-docs.pages.dev/api/v2/rcon-command.html)

## 帮助与下载

- 前往 [文档](https://queqiao-docs.pages.dev) 查看详细使用说明
- [![`Modrinth`](./assets/modrinth.svg)](https://modrinth.com/plugin/queqiao)
- [![`CurseForge`](./assets/curseforge.svg)](https://www.curseforge.com/minecraft/mc-mods/queqiao)

> 没有找到合适的 Mod/Plugin 版本？欢迎提交 [Issues](https://github.com/17TheWord/QueQiao/issues/new?template=version_feature.yml)

## 快速开始

1. 安装服务端对应的 `插件/Mod`
2. 配置 `config.yml` 中的 `websocket_server`
    - `enable: true` # 是否启用
    - `host: "127.0.0.1"`     # WebSocket Server 地址
    - `port: 8080` # WebSocket Server 端口
3. 启动服务器，等待开启 `Websocket Server`
4. 使用 [`ApiFox`](https://apifox.com/) 或其他API测试工具，或连接 [`对接`](#对接) 项目
    - 配置全局 `Request Header`
      ```json5
      {
        // 必填
        // 服务器名称，必须与 config.yml 中的 'server_name' 一致
        "x-self-name": "TestServer",
        // 选填
        // 鉴权，必须与 config.yml 中的 'access_token' 一致,如果 config.yml 中的 'auth_token' 为空，则可不设置此项
        "Authorization": "Bearer 123"
      }
      ```
5. 开始游戏，加入服务器
6. 参考 [API 文档](https://queqiao-docs.pages.dev/api/v2/)，使用对应接口发送消息，或监听玩家事件

## 对接

- [`@17TheWord/nonebot-adapter-minecraft`](https://github.com/17TheWord/nonebot-adapter-minecraft)：`NoneBot2` 适配器
- [`@17TheWord/nonebot-plugin-mcqq`](https://github.com/17TheWord/nonebot-plugin-mcqq)：`NoneBot2` 插件，支持 `OneBot`
  和 `QQ` 适配器实现互通聊天。
- [`@CikeyQi/mc-plugin`](https://github.com/CikeyQi/mc-plugin)：`mcqq` 的 `YunZai` 插件实现。
- [`@Twiyan0/koishi-plugin-minecraft-sync-msg`](https://github.com/Twiyin0/koishi-plugin-minecraft-sync-msg)：`mcqq` 的 `Koishi` 插件实现。
- [`@17TheWord/zerobot-plugin-mcqq`](https://github.com/17TheWord/zerobot-plugin-mcqq)：`mcqq` 的 `ZeroBot` 插件实现。
- [`@kterna/astrbot_plugin_mcqq`](https://github.com/kterna/astrbot_plugin_mcqq)：`mcqq` 的 `AstrBot` 插件实现。
- [`@KroMiose/nekro-agent`](https://github.com/KroMiose/nekro-agent)：更智能、更优雅的代理执行 `AI`

## 相关项目

- [`@kterna/queqiao_mcdr`](https://github.com/kterna/queqiao_mcdr)：鹊桥的 `MCDR` 实现。

## 兼容

- [`@kitUIN/ChatImage`](https://github.com/kitUIN/ChatImage)：在 `Minecraft` 聊天栏中显示图片

## 社群

- [Discord](https://discord.gg/SBUkMYsyf2)

## 特别感谢

- [@kitUIN](https://github.com/kitUIN)：提供代码上的帮助以及构建工具
- [`@kitUIN/ModMultiVersion`](https://github.com/kitUIN/ModMultiVersion)：`IDEA` 多版本 `MOD` 插件
- [`@kitUIN/ModMultiVersionTool`](https://github.com/kitUIN/ModMultiVersionTool)：多版本 `MOD` 构建工具

## 贡献与支持

- 觉得好用可以给这个项目点个 `Star` 或者去 [爱发电](https://afdian.com/a/17TheWord) 投喂我。

- 有意见或者建议也欢迎提交 [Issues](https://github.com/17TheWord/QueQiao/issues)
  和 [Pull requests](https://github.com/17TheWord/QueQiao/pulls) 。

## 星星

[![Stargazers over time](https://starchart.cc/17TheWord/QueQiao.svg?variant=adaptive)](https://starchart.cc/17TheWord/QueQiao)

## 开源许可

本项目使用 [MIT](https://github.com/17TheWord/QueQiao/blob/main/LICENSE) 作为开源许可证。
