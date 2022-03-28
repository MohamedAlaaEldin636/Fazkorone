// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    //id("com.android.application") version "7.1.2" apply false
    //id("com.android.library") version "7.1.2" apply false
    //id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    id("dependencies") version "1.0.0" apply true
    //id("com.android.application") version "7.0.4" apply false
    //id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    //id("com.android.library") version "7.1.2" apply false
    //id("org.jetbrains.kotlin.android") version "1.6.10" apply false
}

apply(plugin = "dependencies")

buildscript {

    repositories {
        google()
        mavenCentral()
        mavenLocal()
        jcenter()

        maven { url = uri("https://jitpack.io") }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.2")

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")

        classpath("com.google.dagger:hilt-android-gradle-plugin:2.40.5")

        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.4.1")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }

}

allprojects {

    apply(plugin = "dependencies")

    repositories {
        google()
        mavenCentral()
        mavenLocal()
        jcenter()

        maven { url = uri("https://jitpack.io") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}





// Top-level build file where you can add configuration options common to all sub-projects/modules.
//plugins {
//    id 'com.android.application' version '7.1.2' apply false
//    id 'com.android.library' version '7.1.2' apply false
//    id 'org.jetbrains.kotlin.android' version '1.6.10' apply false
//}
//
//task clean(type: Delete) {
//    delete rootProject.buildDir
//}