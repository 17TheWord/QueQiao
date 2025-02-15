#!/bin/bash

while [[ $# -gt 0 ]]; do
  case $1 in
    -path)
      IFS=',' read -r -a paths <<< "$2"
      shift 2
      ;;
    *)
      echo "Unknown option: $1"
      exit 1
      ;;
  esac
done

# 用于存储所有文件夹的数组
allFolderObjects=()

# 遍历每个路径
for path in "${paths[@]}"; do
  # 获取指定路径下的文件夹
  folders=$(find "$path" -mindepth 1 -maxdepth 1 -type d)

  # 过滤掉名为 "origin" 的文件夹
  for folder in $folders; do
    folderName=$(basename "$folder")
    if [[ "$folderName" != "origin" ]]; then
      # 提取 mc-version 和 mc-loader 信息
      mcVersion="${folderName//$path-/}"
      mcLoader="$path"
      supportVersionFile="$mcLoader/$mcLoader-$mcVersion/support_version.txt"
      if [[ -f "$supportVersionFile" ]]; then
        supportVersion=$(cat "$supportVersionFile")
      else
        supportVersion="$mcVersion"
      fi

      if [ "$mcLoader" == "fabric" ]; then
        publishLoaders="fabric quilt"
      else
        publishLoaders="$mcLoader"
      fi
      allFolderObjects+=("{\"mc-version\": \"$mcVersion\", \"mc-loader\": \"$mcLoader\", \"publish-loaders\": \"$publishLoaders\", \"publish-version\": \"$supportVersion\"}")
    fi
  done
done

# 创建 JSON 格式的输出
json=$(printf "{\"config\":[%s]}" "$(IFS=,; echo "${allFolderObjects[*]}")")

# 输出最终的 JSON 结果
echo "matrix=$json" >> $GITHUB_OUTPUT
