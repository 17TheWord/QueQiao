---
name: "queqiao-multiloader"
description: "为鹊桥新增尚未支持的 Minecraft 版本或加载器版本：补齐 gradle 版本分支、origin 条件编译、模组元数据与 CI 矩阵，处理跨版本 API 边界。适用于用户要求支持新版本、新加载器或补齐已有加载器的任务。"
---

# 鹊桥新版本适配

用于在 `queqiao` 项目中为一个尚未支持的 Minecraft 版本或加载器补齐适配层。项目用 ModMultiVersion 维护 `origin` 模板与生成目录，CI 逐版本构建，用户负责最终验收。

## 何时使用

- 新增一个 MC 版本（加载器已存在，例如 forge 补 1.21）。
- 新增一个加载器（例如接入一个此前未支持的平台）。
- 把已有加载器补到新的主版本。
- 修复某个新版本在构建阶段的兼容问题。

不用于：升级 core 依赖、改文档、只调 CI。

## 源文件与同步

- 生成链路：`origin` → 版本目录。CLI 与 CI 都只从 `origin` 生成，方向单向。
- ModMultiVersion 的 IDE 插件通过 VFS 文件内容变更事件做同步：
  - 在版本目录中保存文件（保存动作或任何真实写盘）会反向同步回 `origin`。
  - 在 `origin` 中保存会正向同步到所有版本目录。
- 直接写文件的工具通常也会触发同样的 VFS 事件，但不能假定一定触发（IDE 未运行、VFS 未刷新、编辑器存在未保存缓冲都可能导致不同步）。
- 因此提交前以 `origin` 的实际内容为准：
  - 若改动来自版本目录，先确认 `git diff` 中 `origin` 已更新；
  - 若 `origin` 未更新，手动把改动补回 `origin`。
- 判断标准：删除版本目录后，仅凭 `origin` 能重建出当前提交的内容。

## 固定原则

1. **API 改动必须有依据**：以实际 API jar、同一加载器相邻版本的现有实现或该版本官方映射为准；禁止凭记忆改名、加参数或删分支。
2. **默认不跑本地完整构建**：按用户要求用真实 API jar 做定向编译，或直接交给 CI。
3. **一个加载器一次提交**：便于 CI 定位失败版本，也便于单独回滚。
4. 提交身份使用仓库约定，不写死到代码或文档里。
5. 生成目录不手工维护：所有可提交内容都必须能由 `origin` 重建。

## 目录速查

- 加载器：`cleanroom`、`gtnh`、`vanilla`、`spigot`、`paper`、`folia`、`fabric`、`forge`、`neoforge`、`velocity`
- 源模板：`<loader>/origin/src/main/java/com/github/theword/queqiao/`
- core 版本：根目录 `tool_version.txt`
- 版本声明：`<loader>/origin/gradle.properties` 中的 `IF` 分支
- 条件编译：源码中的 `// IF <loader>-<version>` / `// ELSE IF` / `// ELSE` / `// END IF`
- CI 矩阵：仓库根目录 `matrix.sh` 扫描各加载器的版本目录生成，新增目录后自动纳入

## 新增版本步骤

1. **确定目标坐标**
   - MC 版本、加载器、Java 版本。
   - 依赖坐标：`minecraft_version`、`yarn_mappings`、`forge_version`、`neo_version`、`api_version` 等。

2. **在 `gradle.properties` 增加版本分支**
   - 找到该加载器最后一个 `ELSE IF`，追加 `IF <loader>-<version>` 块。
   - 同时更新 `java_version`（若该版本要求更高的 JDK）。

3. **生成版本目录**
   - 优先用 ModMultiVersion 的 IDE 插件或项目初始化脚本生成。
   - 不要手工复制旧版本目录后只改名字，除非确认所有条件分支都已覆盖。

4. **在 `origin` 源码中补版本条件**
   按本次版本的 API 变化逐项检查：
   - 消息组件类型与访问方式（`Component` / `ITextComponent` / `Text`）。
   - 序列化与反序列化入口（是否要求 registry / codec）。
   - 命令 API（Brigadier / `CommandBase` / `SimpleCommand`）。
   - 事件来源与签名（事件类改名、参数增减）。
   - 权限与玩家属性读取方式。
   - 发包 API（包类名、构造参数、字段类型）。
   - 每个新增分支都要有依据，并保留注释形式，让工具能生成其它版本。

5. **补齐元数据**
   按加载器修改对应的版本声明：
   - 插件端：`plugin.yml` 的 `api-version` 或 gradle 中的 `api_version`。
   - Fabric：`fabric.mod.json5` 的 `minecraft` 依赖范围、loader 版本。
   - Forge / NeoForge：`META-INF/mods.toml` 或 `src/main/templates/META-INF/neoforge.mods.toml` 的 `minecraft_version_range`、loader 范围。

6. **确认 CI 收录**
   - 版本目录生成后，`matrix.sh` 应能扫描到新版本；CI 日志里要能看到该版本任务。
   - 若矩阵没有收录，先检查目录命名是否为 `<loader>-<version>`。

7. **构建与回溯**
   - 首次构建失败时先判断类型：依赖坐标错误、条件分支未覆盖、还是调用了该版本不存在的 API。
   - 只改 `origin` 中对应分支，再次生成并重跑。

8. **提交**
   - 一个加载器一次提交；提交信息注明加载器与目标版本。

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

## 跨版本 API 边界清单

新增版本时优先核对下列边界；它们是已有适配中实际出现过的差异点，可作为排查起点：

- **Fabric `ServerCommandSource#sendFeedback`**：`Supplier<Text>` 重载在 1.20+ 才存在；更早版本用 `sendFeedback(Text, boolean)`。
- **Fabric 组件转换**：`>=1.20.4` 用 `Text.Serialization.fromJsonTree(json)`；`1.21–1.21.5` 需 `registryManager`；`>=1.21.6` 用 `TextCodecs`；更早用 `Text.Serializer`。
- **Forge 翻译组件**：1.16.5 `TranslationTextComponent`；1.18.2 `TranslatableComponent`；1.19+ `TranslatableContents`。
- **Forge 1.18+ 标题包**：主标题 `ClientboundSetTitleTextPacket`，副标题 `ClientboundSetSubtitleTextPacket`。
- **Forge 1.16.5**：`ITextComponent` 没有 `append`，需 `IFormattableTextComponent`；发包是 `IPacket` / `SChatPacket` / `STitlePacket`。
- **Adventure `Title.Times`**：4.9.x 只有 `of(...)`；更高版本可用 `times(...)`。core 传入 ticks，换算毫秒要 ×50。
- **Fabric 命令注册回调**：1.19 前后参数不同（`registryAccess/environment` vs `dedicated`）。
- **Fabric 成就类型**：`<1.20.4` 是 `Advancement`，`>=1.20.4` 是 `AdvancementEntry`。
- **Velocity**：服务端 `sendMessage` 是广播；私聊必须对目标玩家调用。

## 验证流程

1. 确认 `origin` 自包含：版本目录可由 `origin` 重建。
2. 若用户要求本地校验：下载对应版本 API jar，用 `javac --release <java_version>` 对真实依赖定向编译。
3. 否则提交并推送，由用户跑 CI；失败日志先定位到具体版本与行号，再对照该加载器相邻版本的分支修正。
4. 修复只提交对应加载器，避免多个版本的改动混在一起。

## 参考索引

- 开发文档：`queqiao-docs/docs/development/quick-start/`、`standard-dev/`
- core 依赖：Maven Central 的 `io.github.17theword.queqiao:core`
- 多版本工具用法：参见 `modmultiversion` skill
