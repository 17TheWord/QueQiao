#!/bin/bash

file=$(find tool -name "ModMultiVersionTool*.jar" | head -n 1)

if [ -n "$file" ]; then
    java -jar "$file"
else
    echo "can't find ModMultiVersionTool.jar"
fi
