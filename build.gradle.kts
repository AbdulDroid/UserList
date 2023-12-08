// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.jetbrainsKotlinAllOpen)
    alias(libs.plugins.navigationSafeArgs) apply false
    alias(libs.plugins.hiltAndroid) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}