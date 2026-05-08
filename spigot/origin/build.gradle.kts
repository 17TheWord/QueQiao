plugins {
    java
    idea
    id("com.gradleup.shadow") version "9.4.1"
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
val spigotApiVersion = prop("spigot_api_version")

val toolVersion = fileProp("../../tool_version.txt")
val modVersion = fileProp("../../version.txt")

repositories {
    mavenCentral()
    maven {
        url = uri(mavenRepositoryUrl)
        credentials {
            username = System.getenv("GH_USERNAME")
            password = System.getenv("PACKAGE_READ_ONLY_TOKEN")
        }
    }
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:$spigotApiVersion")

    implementation("com.github.theword.queqiao:queqiao-tool:$toolVersion")
    implementation("org.java-websocket:Java-WebSocket:$javaWebsocketVersion")
    implementation("org.yaml:snakeyaml:$snakeyamlVersion")
    implementation("org.glavo:rcon-java:$glavoRconVersion")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)
}

tasks {
    shadowJar {
        destinationDirectory.set(file("../../QueQiao-jar/$modVersion"))
        archiveBaseName.set("${modName}-spigot+${minecraftVersion}")
        archiveClassifier.set("")
    }

    build {
        dependsOn(shadowJar)
    }

    processResources {
        val props = mapOf(
            "minecraft_version" to minecraftVersion,
            "mod_id" to modId,
            "mod_name" to modName,
            "mod_license" to modLicense,
            "mod_version" to modVersion,
            "mod_author" to modAuthor,
            "mod_description" to modDescription,
            "mod_home_page" to modHomePage
        )
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}