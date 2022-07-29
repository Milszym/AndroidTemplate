buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${DependencyVersions.androidGradlePlugin}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${DependencyVersions.kotlin}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${DependencyVersions.androidNavigation}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }

    defaultKotlinJvmTarget()
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
