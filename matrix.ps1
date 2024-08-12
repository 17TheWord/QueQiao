param (
    [string[]]$paths
)

$allFolderObjects = @()

foreach ($path in $paths) {
    $folders = Get-ChildItem -Path $path -Directory
    $filteredFolders = $folders | Where-Object { $_.Name -ne "origin" }

    $folderObjects = $filteredFolders | ForEach-Object {
        [PSCustomObject]@{
            "mc-version" = $_.Name.Replace("$path-", "")
            "mc-loader"  = $path
        }
    }

    $allFolderObjects += $folderObjects
}

$json = [PSCustomObject]@{
    "config" = $allFolderObjects
} | ConvertTo-Json -Compress

Write-Output "::set-output name=matrix::$json"
