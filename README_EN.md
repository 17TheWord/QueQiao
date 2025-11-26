<div align="right">
🌍<a href="https://github.com/17TheWord/QueQiao/blob/main/README.md">中文</a> / English
</div>

<p align="center">
  <img src="https://raw.githubusercontent.com/17TheWord/nonebot-adapter-minecraft/main/assets/logo.png" width="200" height="200" alt="QueQiao Logo">
</p>

<div align="center">

# QueQiao

✨ Minecraft Server Mod/Plugin for Real-time Player Events & API Broadcast Messaging ✨

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
  <a href="https://queqiao-docs.pages.dev">📖 Documentation</a>
  ·
  <a href="https://modrinth.com/plugin/queqiao">⬇️ Modrinth</a>
  ·
  <a href="https://www.curseforge.com/minecraft/mc-mods/queqiao">⬇️ CurseForge</a>
  ·
  <a href="https://github.com/17TheWord/QueQiao/issues">🐛 Report Issues</a>
</p>

## Introduction

- Distributes **player events** from the server in `JSON` format via `WebSocket`
  - Implemented [`Events`](https://queqiao-docs.pages.dev/events/v2/):
    - [`Player Chat`](https://queqiao-docs.pages.dev/events/v2/message/player-chat-event.html)
    - [`Player Command`](https://queqiao-docs.pages.dev/events/v2/message/player-command-event.html)
    - [`Player Death`](https://queqiao-docs.pages.dev/events/v2/notice/player-death-event.html)
    - [`Player Join`](https://queqiao-docs.pages.dev/events/v2/notice/player-join-event.html)
    - [`Player Quit`](https://queqiao-docs.pages.dev/events/v2/notice/player-quit-event.html)
    - [`Player Advancement (Achievement)`](https://queqiao-docs.pages.dev/events/v2/notice/player-achievement-event.html)
- Receives `JSON` messages via `WebSocket` and forwards to in-game players
  - Implemented [APIs](https://queqiao-docs.pages.dev/api/v2/):
    - [`Broadcast`](https://queqiao-docs.pages.dev/api/v2/broadcast.html)
    - [`Private Message`](https://queqiao-docs.pages.dev/api/v2/private-message.html)
    - [`Title & SubTitle`](https://queqiao-docs.pages.dev/api/v2/title.html)
    - [`Action Bar`](https://queqiao-docs.pages.dev/api/v2/action-bar.html)
    - [`Rcon Command`](https://queqiao-docs.pages.dev/api/v2/rcon-command.html)

## Documentation & Downloads

- Visit the [QueQiao Document](https://queqiao-docs.pages.dev/) for detailed documentation
- [![`Modrinth`](./assets/modrinth.svg)](https://modrinth.com/plugin/queqiao)
- [![`CurseForge`](./assets/curseforge.svg)](https://www.curseforge.com/minecraft/mc-mods/queqiao)

> Can't find a suitable Mod/Plugin version? Feel free to submit an [Issue](https://github.com/17TheWord/QueQiao/issues/new?template=version_feature.yml)

## Quick Start

1. Install the corresponding server `plugin/mod`
2. Configure `websocket_server` in `config.yml`:
  - `enable: true` # Enable WebSocket server
  - `host: "127.0.0.1"` # WebSocket Server address
  - `port: 8080` # WebSocket Server port
3. Start the server and wait for `WebSocket Server` to initialize
4. Use [`ApiFox`](https://apifox.com/) or other API testing tools, or connect with [Integration](#integration-projects) projects
  - Configure global `Request Header`:
    ```json5
    {
      // Required: server name, must match 'server_name' in config.yml
      "x-self-name": "TestServer",
      // Optional: auth token, must match 'access_token' in config.yml (omit if empty)
      "Authorization": "Bearer 123"
    }
    ```
5. Launch the game and join the server
6. Refer to the [API Documentation](https://queqiao-docs.pages.dev/api/v2/) to send messages or listen to player events.

## Integration Projects

- [`@17TheWord/nonebot-adapter-minecraft`](https://github.com/17TheWord/nonebot-adapter-minecraft): `NoneBot2` adapter
- [`@17TheWord/nonebot-plugin-mcqq`](https://github.com/17TheWord/nonebot-plugin-mcqq): `NoneBot2` plugin
- [`@CikeyQi/mc-plugin`](https://github.com/CikeyQi/mc-plugin): `YunZai` plugin
- [`@Twiyan0/koishi-plugin-minecraft-sync-msg`](https://github.com/Twiyin0/koishi-plugin-minecraft-sync-msg): `Koishi` plugin
- [`@17TheWord/zerobot-plugin-mcqq`](https://github.com/17TheWord/zerobot-plugin-mcqq): `ZeroBot` plugin
- [`@kterna/astrbot_plugin_mcqq`](https://github.com/kterna/astrbot_plugin_mcqq): `AstrBot` plugin
- [`@KroMiose/nekro-agent`](https://github.com/KroMiose/nekro-agent): `AI` agent

## Related Projects

- [`@kterna/queqiao_mcdr`](https://github.com/kterna/queqiao_mcdr): `MCDR` implementation of `QueQiao` for message exchange

## Compatibility

- [`@kitUIN/ChatImage`](https://github.com/kitUIN/ChatImage): Displays images in `Minecraft` chat

## Community

- [Discord](https://discord.gg/SBUkMYsyf2)

## Special Thanks

- [@kitUIN](https://github.com/kitUIN): For code assistance and build tools
- [`@kitUIN/ModMultiVersion`](https://github.com/kitUIN/ModMultiVersion): IDEA multi-version `MOD` plugin
- [`@kitUIN/ModMultiVersionTool`](https://github.com/kitUIN/ModMultiVersionTool): Multi-version `MOD` build tool

## Support & Contribution

- Star this project if you find it useful or support me on [AFDian](https://afdian.com/a/17TheWord)
- Submit suggestions/bugs via [Issues](https://github.com/17TheWord/QueQiao/issues)
- Contribute via [Pull requests](https://github.com/17TheWord/QueQiao/pulls)

## Stargazers

[![Stargazers over time](https://starchart.cc/17TheWord/QueQiao.svg?variant=adaptive)](https://starchart.cc/17TheWord/QueQiao)

## License

This project is licensed under the [MIT](https://github.com/17TheWord/QueQiao/blob/main/LICENSE) license.
