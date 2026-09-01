$file = Get-ChildItem -Path "tool" -Filter "ModMultiVersionTool*.jar" | Select-Object -First 1

if ($file) {
    java -jar $file.FullName
} else {
    Write-Output "can't find ModMultiVersionTool.jar"
}

# ModMultiVersionTool 暂不支持 velocity，手动从 origin 生成版本目录
$velocityVersions = @()
if (Test-Path "velocity") {
    $velocityVersions = Get-ChildItem -Path "velocity" -Directory -Filter "velocity-*" | Sort-Object Name
}

foreach ($versionDir in $velocityVersions) {
    $versionName = $versionDir.Name
    Write-Output "Generating $versionName files..."

    $srcDir = Join-Path $versionDir.FullName "src"
    $wrapperDir = Join-Path $versionDir.FullName "gradle/wrapper"
    New-Item -ItemType Directory -Force -Path $srcDir, $wrapperDir | Out-Null

    Copy-Item -Path "velocity/origin/src/*" -Destination $srcDir -Recurse -Force
    Copy-Item -Path "velocity/origin/build.gradle.kts" -Destination $versionDir.FullName -Force
    Copy-Item -Path "velocity/origin/gradle/wrapper/gradle-wrapper.jar" -Destination $wrapperDir -Force
    Copy-Item -Path "velocity/origin/gradle/wrapper/gradle-wrapper.properties" -Destination $wrapperDir -Force

    # 解析 origin/gradle.properties 中的 IF/ELSE 块，生成该版本的 gradle.properties
    $ifPattern = '^# *(IF|ELSE IF) ' + [regex]::Escape($versionName)
    $blockPattern = '^# *(IF|ELSE IF|ELSE|END IF)'
    $keep = $false

    $lines = foreach ($line in (Get-Content -Path "velocity/origin/gradle.properties")) {
        if ($line -match $ifPattern) {
            $keep = $true
            $line
        } elseif ($line -match $blockPattern) {
            $keep = $false
            $line
        } elseif ($keep) {
            $line -replace '^#', ''
        } else {
            $line
        }
    }

    # 写入 UTF-8 无 BOM，避免 Gradle 读取 properties 时首行出现 BOM 字符
    $content = ($lines -join "`n") + "`n"
    [System.IO.File]::WriteAllText(
        (Join-Path $versionDir.FullName "gradle.properties"),
        $content,
        (New-Object System.Text.UTF8Encoding $false)
    )
}
