pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "UnicoCaseAiVoiceChatApp"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")

// Core Project Modules
include(":core:common")
include(":core:data")
include(":core:service")
include(":core:designsystem")
include(":core:domain")
include(":core:navigation")

// Feature Project Modules
include(":feature:chat")
include(":feature:history")
include(":feature:settings")