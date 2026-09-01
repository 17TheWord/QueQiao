import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.compile.JavaCompile

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

val modName = prop("mod_name")
val modGroupId = prop("mod_group_id")

val javaVersion = prop("java_version")
val glavoRconVersion = prop("glavo_rcon_version")
val snakeyamlVersion = prop("snakeyaml_version")
val javaWebsocketVersion = prop("java_websocket_version")
val slf4jVersion = prop("slf4j_version")
val commonsIoVersion = prop("commons_io_version")
val junitJupiterVersion = prop("junit_jupiter_version")
val mavenRepositoryUrl = prop("maven_repository_url")

val apiVersion = prop("api_version")

val toolVersion = fileProp("../../tool_version.txt")
val modVersion = fileProp("../../version.txt")

group = modGroupId
version = modVersion

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        url = uri(mavenRepositoryUrl)
        credentials {
            username = System.getenv("GH_USERNAME")
            password = System.getenv("PACKAGE_READ_ONLY_TOKEN")
        }
    }
}

dependencies {
    implementation("com.velocitypowered:velocity-api:$apiVersion-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:$apiVersion-SNAPSHOT")

    implementation("com.github.theword.queqiao:queqiao-tool:$toolVersion")
    implementation("org.java-websocket:Java-WebSocket:$javaWebsocketVersion")
    implementation("org.yaml:snakeyaml:$snakeyamlVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    implementation("commons-io:commons-io:$commonsIoVersion")
    implementation("org.glavo:rcon-java:$glavoRconVersion")

    testImplementation("org.slf4j:slf4j-simple:$slf4jVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)
}

val generateTemplates = tasks.register<Copy>("generateTemplates") {
    val props = mapOf("version" to project.version.toString())
    inputs.properties(props)

    from(file("src/main/templates"))
    into(layout.buildDirectory.dir("generated/sources/templates"))
    expand(props)
}

sourceSets {
    main {
        java.srcDir(generateTemplates.map { it.outputs })
    }
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(javaVersion.toInt())
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    shadowJar {
        destinationDirectory.set(file("../../QueQiao-jar/$modVersion"))
        archiveBaseName.set("${modName}-velocity+${apiVersion}")
        archiveClassifier.set("")

        dependencies {
            include(dependency("com.github.theword.queqiao:queqiao-tool:$toolVersion"))
            include(dependency("commons-io:commons-io:$commonsIoVersion"))
            include(dependency("org.java-websocket:Java-WebSocket:$javaWebsocketVersion"))
            include(dependency("org.yaml:snakeyaml:$snakeyamlVersion"))
            include(dependency("org.glavo:rcon-java:$glavoRconVersion"))
        }

        mergeServiceFiles()
    }

    build {
        dependsOn(shadowJar)
    }
}
