#!/bin/bash

targetLoader="all"

while [[ $# -gt 0 ]]; do
  case $1 in
    -path)
      IFS=',' read -r -a paths <<< "$2"
      shift 2
      ;;
    -loader)
      targetLoader="$2"
      shift 2
      ;;
    *)
      echo "Unknown option: $1"
      exit 1
      ;;
  esac
done

allFolderObjects=()

for path in "${paths[@]}"; do
  folders=$(find "$path" -mindepth 1 -maxdepth 1 -type d)

  for folder in $folders; do
    folderName=$(basename "$folder")
    if [[ "$folderName" == "origin" ]]; then
      continue
    fi

    mcVersion="${folderName//$path-/}"
    mcLoader="$path"

    if [[ "$targetLoader" != "all" && "$mcLoader" != "$targetLoader" ]]; then
      continue
    fi

    supportVersionFile="$mcLoader/$mcLoader-$mcVersion/support_version.txt"
    if [[ -f "$supportVersionFile" ]]; then
      supportVersion=$(cat "$supportVersionFile")
    else
      supportVersion="$mcVersion"
    fi

    if [[ "$mcLoader" == "fabric" ]]; then
      publishLoaders="fabric quilt"
    else
      publishLoaders="$mcLoader"
    fi

    allFolderObjects+=("{\"mc-version\": \"$mcVersion\", \"mc-loader\": \"$mcLoader\", \"publish-loaders\": \"$publishLoaders\", \"publish-version\": \"$supportVersion\"}")
  done
done

if [[ ${#allFolderObjects[@]} -eq 0 ]]; then
  echo "No matching build targets for loader: $targetLoader" >&2
  exit 1
fi

json=$(printf "{\"config\":[%s]}" "$(IFS=,; echo "${allFolderObjects[*]}")")

echo "matrix=$json" >> "$GITHUB_OUTPUT"
