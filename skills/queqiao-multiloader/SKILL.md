---
name: "queqiao-multiloader"
description: "维护鹊桥（QueQiao）多加载器适配：core 接入、跨版本 API 边界、origin 模板改动与 Action 验证。适用于升级 core、新增加载器版本、跨平台补事件/命令/API 的任务。"
---

# 鹊桥多加载器适配

用于在 `queqiao` 项目中维护 10 个加载器的适配层。用户负责最终验收，通常通过 GitHub Action 逐版本构建。

## 何时使用

- 升级 `queqiao-core` 版本后同步各加载器。
- 新增加载器或 MC 版本。
- 跨平台补事件、命令、消息 API。
- 修复某个加载器在 CI 中的编译/运行问题。

不用于：只改文档、只改 CI、纯协议/事件字段说明。

## 固定原则

1. **只改 `<loader>/origin` 模板**，不改 `origin → 版本目录` 生成结果；生成交给 ModMultiVersion（参见 modmultiversion skill）。
2. **API 改动必须有依据**：以实际 API jar、项目内旧实现或同分支其它加载器为准；禁止凭记忆改名或加参数。
3. **默认不跑本地完整构建**：按用户要求用真实 API jar 做定向编译，或直接交给 GitHub Action。
4. **一个加载器一次提交**，便于 Action 定位失败版本。
5. 提交身份使用仓库约定，不写死到代码或文档里。

## 目录速查

- 加载器：`cleanroom`、`gtnh`、`vanilla`、`spigot`、`paper`、`folia`、`fabric`、`forge`、`neoforge`、`velocity`
- 源模板：`<loader>/origin/src/main/java/com/github/theword/queqiao/`
- core 版本：根目录 `tool_version.txt`
- 多版本条件：`<loader>/origin/gradle.properties` 与源码中的 `// IF ... // ELSE ... // END IF`
- 文档：`../queqiao-docs/docs/development/`

## core 接入骨架

每个加载器至少包含：

| 文件 | 职责 |
| --- | --- |
| `PlatformContext` | 继承 `AbstractPlatformContext<S, C, P>`，实现组件转换、玩家模型、广播、私聊、可选 Title/ActionBar |
| `QueQiao` / 主类 | 服务端启动完成后 `QueQiaoRuntime.create(context)` → `bindRuntime` → `start()`；关闭时 `shutdown()` |
| `EventProcessor` | 监听平台事件，用 `ConfigKeys.SubscribeEvent.*` 判断开关，`runtime.sendEvent(...)` |
| `CommandExecutor` + `*CommandContext` | `createCommandRouter()` 或 `createDeferredCommandTree(...)` 映射到平台命令 API |
| `*Tool` | `PlayerModel`、成就模型、JSON → 平台组件；翻译解析归 `PlatformContext` |

构建依赖使用 `io.github.17theword.queqiao:core:${tool_version}`（Maven Central），不再使用旧的 `com.github.theword.queqiao:queqiao-tool` 与 GitHub Packages。

### 初始化时机

| 平台 | 时机 |
| --- | --- |
| Paper | `onEnable` 后调度到主线程创建 Runtime |
| Folia | `ServerLoadEvent`（插件加载过早） |
| Forge / NeoForge | `ServerStartedEvent` / `FMLServerStartedEvent` |
| Fabric | `ServerLifecycleEvents.SERVER_STARTED`；命令用 `createDeferredCommandTree` |
| Velocity | `ProxyInitializeEvent` |

## 加载器差异矩阵

| 加载器 | 组件类型 | 命令 API | 事件来源 |
| --- | --- | --- | --- |
| cleanroom / gtnh | `IChatComponent` / `ITextComponent` | `CommandBase` | Forge 事件 |
| vanilla | 日志文本 + RCON | 无 | 日志轮询 |
| spigot | `BaseComponent` / Adventure | Bukkit `Command` | Bukkit 事件 |
| paper / folia | Adventure `Component` | Bukkit / Paper | Paper 事件 |
| forge | 1.16.5 `ITextComponent`，1.18+ `Component` | Brigadier / `CommandBase` | Forge 事件 |
| neoforge | Adventure `Component` | Brigadier | NeoForge 事件 |
| fabric | Yarn `Text` | Brigadier | Fabric API + Mixin 回调 |
| velocity | Adventure `Component` | `SimpleCommand` | Velocity 事件 |

## 已知版本边界（复用前先核对）

- **Fabric `ServerCommandSource#sendFeedback`**：`Supplier<Text>` 重载在 1.20+ 才存在；1.18.2 / 1.19.2 用 `sendFeedback(Text, boolean)`。
- **Fabric 组件转换**：`>=1.20.4` 用 `Text.Serialization.fromJsonTree(json)`；`1.21–1.21.5` 需 `registryManager`；`>=1.21.6` 用 `TextCodecs`；更早用 `Text.Serializer`。
- **Forge 翻译组件导入**：1.16.5 `TranslationTextComponent`；1.18.2 `TranslatableComponent`；1.19+ `TranslatableContents`。漏导入会直接编译失败。
- **Forge 1.18+ 标题包**：主标题用 `ClientboundSetTitleTextPacket`，副标题用 `ClientboundSetSubtitleTextPacket`；旧代码里主标题误用副标题包属于历史 bug。
- **Forge 1.16.5**：`ITextComponent` 没有 `append`，需 `IFormattableTextComponent`；发包是 `IPacket` / `SChatPacket` / `STitlePacket`。
- **Adventure `Title.Times`**：4.9.x 只有 `of(...)`；Velocity 3.3/3.4 可用 `times(...)`。core 传入的是 ticks，换算毫秒要 ×50。
- **Fabric 命令回调**：Brigadier 命令注册回调在 1.19 前后参数不同（`registryAccess/environment` vs `dedicated`）。
- **Fabric 成就类型**：`<1.20.4` 是 `Advancement`，`>=1.20.4` 是 `AdvancementEntry`。
- **Velocity**：`Server#sendMessage` 是广播；私聊必须 `player.sendMessage(...)`。

## 验证流程

1. 改动只落在 `origin`；检查是否还有旧 `queqiao.tool` / `GlobalContext` / `HandleApi*` 引用。
2. 若用户要求本地校验：下载对应版本 API jar，用 `javac --release <java_version>` 对真实依赖定向编译。
3. 否则提交并 push，由用户跑 Action；失败日志先定位到具体版本与行号，再对照同版本旧的 IF 分支修正。
4. 修完只提交对应加载器，避免把多个版本的修复混在一起。

## 参考索引

- 开发文档：`queqiao-docs/docs/development/quick-start/`、`standard-dev/`
- 迁移对照表：`quick-start/README.md`
- core 源码/API：Maven Central 的 `io.github.17theword.queqiao:core` 对应版本
