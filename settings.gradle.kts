pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url =uri("https://storage.zego.im/maven" ) }  // <- Add this line.
        maven { url =uri("https://www.jitpack.io" ) }  // <- Add this line. d this line
    }
}

rootProject.name = "diplom1"
include(":app")
 