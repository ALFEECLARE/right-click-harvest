plugins {
    id("fabric-loom") version "1.3-SNAPSHOT"
    id("io.github.juuxel.loom-vineflower") version "1.+"
    id("io.github.p03w.machete") version "1.+"
    id("org.cadixdev.licenser") version "0.6.+"
}

apply(from = "https://raw.githubusercontent.com/JamCoreModding/Gronk/main/publishing.gradle.kts")
apply(from = "https://raw.githubusercontent.com/JamCoreModding/Gronk/main/misc.gradle.kts")

val mod_version: String by project

group = "io.github.jamalam360"
version = mod_version

repositories {
    val mavenUrls = mapOf(
        Pair("https://maven.terraformersmc.com/releases", listOf("com.terraformersmc")),
        Pair("https://api.modrinth.com/maven/", listOf("maven.modrinth")),
        Pair("https://maven.jamalam.tech/releases", listOf("io.github.jamalam360")),
        Pair("https://maven.quiltmc.org/repository/release", listOf("org.quiltmc")),
        Pair("https://ladysnake.jfrog.io/artifactory/mods", listOf("dev.onyxstudios.cardinal-components-api")),
        Pair("https://jitpack.io", listOf("com.github.p03w-rehost")),
        Pair("https://server.bbkr.space/artifactory/libs-release", listOf("io.github.cottonmc")),
    )

    for (mavenPair in mavenUrls) {
        maven {
            url = uri(mavenPair.key)
            content {
                for (group in mavenPair.value) {
                    includeGroup(group)
                }
            }
        }
    }
}

dependencies {
    minecraft(libs.minecraft)
    mappings(variantOf(libs.quilt.mappings) { classifier("intermediary-v2") })

    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)
    modApi(libs.jamlib) {
        exclude(group = "net.fabricmc.fabric-api")
    }
    modImplementation(libs.mod.menu)
}

sourceSets {
    val main = this.getByName("main")

    create("gametest") {
        this.compileClasspath += main.compileClasspath
        this.compileClasspath += main.output
        this.runtimeClasspath += main.runtimeClasspath
        this.runtimeClasspath += main.output
    }
}

loom {
    runs {
        create("gametest") {
            server()
            name("Game Test")
            source(sourceSets.getByName("gametest"))
            vmArg("-Dfabric-api.gametest")
            vmArg("-Dfabric-api.gametest.report-file=${project.buildDir}/junit.xml")
            runDir("build/run-gametest")
        }

        create("gametestDebug") {
            client()
            name("Game Test")
            source(sourceSets.getByName("gametest"))
            vmArg("-Dfabric-api.gametest")
            vmArg("-Dfabric-api.gametest.report-file=${project.buildDir}/junit.xml")
            runDir("build/run-gametest")
        }
    }
}

tasks {
    test {
        dependsOn("runGametest")
    }
}

