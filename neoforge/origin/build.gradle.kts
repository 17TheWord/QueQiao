plugins {
    java
    idea
    id("net.neoforged.gradle.userdev") version "7.1.26"
}

fun prop(key: String): String = providers.gradleProperty(key).get()

fun fileProp(fileName: String): String {
    val versionFile = file(fileName)

    return if (versionFile.exists()) {
        versionFile.readText().trim()
    } else {
        throw Exception("File not found: ${versionFile.absolutePath}")
    }
}

val modId = prop("mod_id")
val modName = prop("mod_name")
val modLicense = prop("mod_license")
val modAuthor = prop("mod_author")
val modGroupId = prop("mod_group_id")
val modHomePage = prop("mod_home_page")
val modDescription = prop("mod_description")

val javaVersion = prop("java_version")
val glavoRconVersion = prop("glavo_rcon_version")
val snakeyamlVersion = prop("snakeyaml_version")
val javaWebsocketVersion = prop("java_websocket_version")
val mavenRepositoryUrl = prop("maven_repository_url")

val minecraftVersion = prop("minecraft_version")
val minecraftVersionRange = prop("minecraft_version_range")
val neoVersion = prop("neo_version")

val toolVersion = fileProp("../../tool_version.txt")
val modVersion = fileProp("../../version.txt")

version = modVersion
group = modGroupId

base {
    archivesName = "${modName}-neoforge+$minecraftVersion"
}

repositories {
    mavenLocal()
    maven {
        url = uri(mavenRepositoryUrl)
        credentials {
            username = System.getenv("GH_USERNAME")
            password = System.getenv("PACKAGE_READ_ONLY_TOKEN")
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion.toInt()))
    }
}

sourceSets.named("main") {
    resources.srcDir("src/generated/resources")
}

fun DependencyHandler.implementationJarJar(dependencyNotation: String) {
    implementation(dependencyNotation)
    jarJar(dependencyNotation)
}

dependencies {
    implementation("net.neoforged:neoforge:$neoVersion")

    implementationJarJar("com.github.theword.queqiao:queqiao-tool:$toolVersion")
    implementationJarJar("org.java-websocket:Java-WebSocket:$javaWebsocketVersion")
    implementationJarJar("org.yaml:snakeyaml:$snakeyamlVersion")
    implementationJarJar("org.glavo:rcon-java:$glavoRconVersion")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val props = mapOf(
        "minecraft_version" to minecraftVersion,
        "minecraft_version_range" to minecraftVersionRange,
        "neo_version" to neoVersion,
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_license" to modLicense,
        "mod_version" to modVersion,
        "mod_author" to modAuthor,
        "mod_description" to modDescription
    )

    inputs.properties(props)
    expand(props)

    from("src/main/templates")
    into(layout.buildDirectory.dir("generated/sources/modMetadata"))
}

sourceSets.named("main") {
    resources.srcDir(generateModMetadata)
}

tasks.named<Jar>("jarJar") {
    archiveClassifier.set("")
    destinationDirectory.set(file("../../QueQiao-jar/$modVersion"))
}

tasks.build {
    dependsOn("jarJar")
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}