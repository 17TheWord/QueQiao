# 鹊桥

## 介绍

- 可以通过 [`nonebot-adapter-minecraft`](https://github.com/17TheWord/nonebot-adapter-minecraft)
  连接至 [`nonebot2`](https://github.com/nonebot/nonebot2) 的 `Minecraft` 服务端 `plugin/mod`。

## 自行构建

1. 克隆项目

    ```shell
    git clone https://github.com/17TheWord/QueQiao.git
    ```

2. 配置工具包仓库的个人访问令牌

    - 各服务端插件/模组的共用部分已被移至 [`鹊桥工具包`](https://github.com/17TheWord/QueQiaoTool)
    - 访问 `GitHub Maven Packages` 需要配置个人访问令牌
    - 需要配置 `只读` 的 `Package Token`
    - 参考 [GitHub Packages 文档](https://docs.github.com/zh/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#%E5%90%91-github-packages-%E9%AA%8C%E8%AF%81)
    - 该项目使用的环境变量名
        - 必填
            - `USERNAME`：GitHub 用户名
            - `PACKAGE_READ_ONLY_TOKEN`：GitHub 个人访问令牌

3. IDEA 加载项目
    - 运行 `init.ps1` 将各服务端插件/模组代码复制至各个版本模块内
    - 加载 `Gradle` 项目等待项目配置完毕

4. 构建插件/模组
    - 在右侧面的 `Gradle` 面板运行所需版本模块的 `Tasks` -> `build` -> `build`
    - 或
    - 命令移至对应的版本模块内运行构建命令
      ```shell
      ./gradlew build
      ```

## 特别感谢

- [@kitUIN](https://github.com/kitUIN)：提供代码上的帮助以及构建工具
- [@kitUIN/ModMultiVersion](https://github.com/kitUIN/ModMultiVersion)：IDEA多版本MOD插件
- [@kitUIN/ModMultiVersionTool](https://github.com/kitUIN/ModMultiVersionTool)：多版本MOD构建工具

## 贡献与支持

- 觉得好用可以给这个项目点个 `Star` 或者去 [爱发电](https://afdian.com/a/17TheWord) 投喂我。

- 有意见或者建议也欢迎提交 [Issues](https://github.com/17TheWord/QueQiao/issues)
  和 [Pull requests](https://github.com/17TheWord/QueQiao/pulls) 。

## 开源许可

本项目使用 [MIT](./LICENSE) 作为开源许可证。
