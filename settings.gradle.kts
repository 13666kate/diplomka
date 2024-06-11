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
        jcenter()
        maven {
            url  = uri("https://github.com/jitsi/jitsi-maven-repository/raw/master/releases")
        }
        maven {
            url  = uri("https://maven.google.com")

               // url =uri("https://maven.google.com")

        }
     //   maven { url =uri("https://storage.zego.im/maven" ) }  // <- Add this line.
       // maven { url =uri("https://www.jitpack.io" ) }  // <- Add this line. d this line
       // maven { url = uri("https://jitpack.io" )}
    }
}

rootProject.name = "diplom1"
include(":app")
 