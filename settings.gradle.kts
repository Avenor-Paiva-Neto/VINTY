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

rootProject.name = "VINTY"
include(":app")
include(":infra")
include(":feature:player")
include(":feature:discovery")
include(":feature:profile")
include(":core")
include(":domain")
include(":data")
include(":feature:auth")
include(":feature:watchlist")
