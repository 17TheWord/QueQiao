#!/bin/bash

file=$(find tool -name "ModMultiVersionTool*.jar" | head -n 1)

if [ -n "$file" ]; then
    java -jar "$file"
else
    echo "can't find ModMultiVersionTool.jar"
fi

# ModMultiVersionTool 暂不支持 velocity，手动从 origin 生成版本目录
velocity_versions=$(find velocity -mindepth 1 -maxdepth 1 -type d -name "velocity-*" 2>/dev/null | sort)

for version_dir in $velocity_versions; do
    version_name=$(basename "$version_dir")
    echo "Generating $version_name files..."

    mkdir -p "$version_dir/src" "$version_dir/gradle/wrapper"
    cp -r velocity/origin/src/. "$version_dir/src/"
    cp velocity/origin/build.gradle.kts "$version_dir/build.gradle.kts"
    cp velocity/origin/gradle/wrapper/gradle-wrapper.jar "$version_dir/gradle/wrapper/"
    cp velocity/origin/gradle/wrapper/gradle-wrapper.properties "$version_dir/gradle/wrapper/"

    # 解析 origin/gradle.properties 中的 IF/ELSE 块，生成该版本的 gradle.properties
    awk '
    BEGIN { keep = 0 }
    /^# *(IF|ELSE IF) '"$version_name"'/ { keep = 1; print; next }
    /^# *(IF|ELSE IF|ELSE|END IF)/ { keep = 0; print; next }
    keep { sub(/^#/, ""); print; next }
    { print }
    ' velocity/origin/gradle.properties > "$version_dir/gradle.properties"
done
