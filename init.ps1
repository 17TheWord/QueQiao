$file = Get-ChildItem -Path "tool" -Filter "ModMultiVersionTool*.jar" | Select-Object -First 1

if ($file) {
    java -jar $file.FullName
} else {
    Write-Output "can't find ModMultiVersionTool.jar"
}
