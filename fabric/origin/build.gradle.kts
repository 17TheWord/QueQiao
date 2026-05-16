plugins {
    id("net.fabricmc.fabric-loom")
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

val toolVersion = fileProp("../../tool_version.txt")

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
val loaderVersion = prop("loader_version")
val fabricApiVersion = prop("fabric_api_version")

version = fileProp("../../version.txt")
group = modGroupId

base {
    archivesName = "$modName-fabric+$minecraftVersion"
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

loom {
    splitEnvironmentSourceSets()

    mods {
        register(modId) {
            sourceSet(sourceSets.main.get())
        }
    }
}

fun DependencyHandlerScope.includeImplementation(notation: Any) {
    implementation(notation)
    include(notation)
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")

    implementation("net.fabricmc:fabric-loader:$loaderVersion")
    implementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

    includeImplementation("com.github.theword.queqiao:queqiao-tool:$toolVersion")
    includeImplementation("org.java-websocket:Java-WebSocket:$javaWebsocketVersion")
    includeImplementation("org.yaml:snakeyaml:$snakeyamlVersion")
    includeImplementation("org.glavo:rcon-java:$glavoRconVersion")

    testImplementation("net.fabricmc:fabric-loader-junit:$loaderVersion")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

val modJsonProps = mapOf(
    "mod_id" to modId,
    "mod_name" to modName,
    "mod_description" to modDescription,
    "mod_author" to modAuthor,
    "mod_license" to modLicense,
    "mod_version" to version,
    "mod_home_page" to modHomePage
)

tasks.processResources {
    inputs.properties(modJsonProps)

    filesMatching("fabric.mod.json") {
        expand(modJsonProps)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = javaVersion.toInt()
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
    destinationDirectory.set(
        layout.projectDirectory.dir("../../QueQiao-jar/$version")
    )

    val projectName = project.name
    inputs.property("projectName", projectName)

    from("LICENSE") {
        rename { "${it}_$projectName" }
    }
}