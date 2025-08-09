plugins {
    id("com.gtnewhorizons.gtnhconvention")
}
fun getVersionFromFile(fileName: String): String {
    val versionFile = file(fileName)
    return if (versionFile.exists()) {
        versionFile.readText().trim()
    } else {
        throw Exception("File not found: ${versionFile.absolutePath}")
    }
}

val modVersion = getVersionFromFile("version.txt")

extra["modVersion"] = modVersion
