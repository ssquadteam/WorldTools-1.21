architectury {
    platformSetupLoomIde()
    forge()
}

base.archivesName.set("${base.archivesName.get()}-forge")

loom {
    accessWidenerPath.set(project(":common").loom.accessWidenerPath)
    forge {
        convertAccessWideners = true
        extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
        mixinConfig("worldtools.mixins.common.json")
    }
}

repositories {
    maven("https://thedarkcolour.github.io/KotlinForForge/") {
        name = "KotlinForForge"
    }
    maven("https://cursemaven.com") {
        name = "Curse"
    }
}

val common: Configuration by configurations.creating {
    configurations.compileClasspath.get().extendsFrom(this)
    configurations.runtimeClasspath.get().extendsFrom(this)
    configurations["developmentForge"].extendsFrom(this)
}

dependencies {
    forge("net.minecraftforge:forge:${project.properties["forge_version"]!!}")
    implementation("thedarkcolour:kotlinforforge:${project.properties["kotlin_forge_version"]!!}")
    common(project(":common", configuration = "namedElements")) { isTransitive = false }
    shadowCommon(project(path = ":common", configuration = "transformProductionForge")) { isTransitive = false }
    forgeRuntimeLibrary(include("net.kyori:adventure-text-minimessage:${project.properties["kyori_text_minimessage_version"]}")!!)
    forgeRuntimeLibrary(include("net.kyori:adventure-text-serializer-gson:${project.properties["kyori_text_minimessage_version"]}")!!)
    implementation(annotationProcessor("io.github.llamalad7:mixinextras-common:0.3.2")!!)
    implementation(include("io.github.llamalad7:mixinextras-forge:0.3.2")!!)
    modApi("me.shedaniel.cloth:cloth-config-forge:11.1.106")
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand(getProperties())
            expand(mutableMapOf("version" to project.version))
        }
    }
}
