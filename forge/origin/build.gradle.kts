plugins {
    java
    idea
    id("net.minecraftforge.gradle") version "[7.0.17,8)"
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
val minecraftVersionRange = prop("minecraft_version_range")
val forgeVersion = prop("forge_version")
val forgeLoaderVersionRange = prop("forge_loader_version_range")
val eventbusValidatorVersion = prop("eventbus_validator_version")

val toolVersion = fileProp("../../tool_version.txt")
val modVersion = fileProp("../../version.txt")

version = modVersion
group = modGroupId

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

sourceSets.main {
    resources.srcDir("src/generated/resources")
}

minecraft {
    runs {
        configureEach {
            workingDir = layout.projectDirectory.dir("run")

            systemProperty("eventbus.api.strictRuntimeChecks", "true")
            systemProperty("forge.enabledGameTestNamespaces", modId)
        }

        register("client")

        register("server") {
            args("--nogui")
        }

        register("gameTestServer")

        register("data") {
            workingDir = layout.projectDirectory.dir("run-data")

            args(
                "--mod", modId,
                "--all",
                "--output", layout.projectDirectory.dir("src/generated/resources"),
                "--existing", layout.projectDirectory.dir("src/main/resources")
            )
        }
    }
}

repositories {
    minecraft.mavenizer(this)
    maven(fg.forgeMaven)
    maven(fg.minecraftLibsMaven)
    mavenCentral()
    maven {
        url = uri(mavenRepositoryUrl)
        credentials {
            username = System.getenv("GH_USERNAME")
            password = System.getenv("PACKAGE_READ_ONLY_TOKEN")
        }
    }
}

dependencies {
    implementation(minecraft.dependency("net.minecraftforge:forge:$forgeVersion"))
    annotationProcessor("net.minecraftforge:eventbus-validator:$eventbusValidatorVersion")

    implementation("com.github.theword.queqiao:queqiao-tool:$toolVersion")
    implementation("org.java-websocket:Java-WebSocket:$javaWebsocketVersion")
    implementation("org.yaml:snakeyaml:$snakeyamlVersion")
    implementation("org.glavo:rcon-java:$glavoRconVersion")
}

val resourceProperties = mapOf(
    "mod_id" to modId,
    "mod_name" to modName,
    "mod_license" to modLicense,
    "mod_author" to modAuthor,
    "mod_group_id" to modGroupId,
    "mod_home_page" to modHomePage,
    "mod_description" to modDescription,
    "mod_version" to modVersion,

    "minecraft_version" to minecraftVersion,
    "minecraft_version_range" to minecraftVersionRange,
    "forge_version" to forgeVersion,
    "forge_loader_version_range" to forgeLoaderVersionRange
)

tasks.processResources {
    val props = resourceProperties

    inputs.properties(props)

    filesMatching("META-INF/mods.toml") {
        expand(props)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks {
    shadowJar {
        destinationDirectory.set(file("../../QueQiao-jar/$modVersion"))
        archiveBaseName.set("${modName}-forge+${minecraftVersion}")
        archiveClassifier.set("")

        dependencies {
            include(dependency("com.github.theword.queqiao:queqiao-tool:${toolVersion}"))
            include(dependency("org.java-websocket:Java-WebSocket:${javaWebsocketVersion}"))
            include(dependency("org.yaml:snakeyaml:${snakeyamlVersion}"))
            include(dependency("org.glavo:rcon-java:${glavoRconVersion}"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}