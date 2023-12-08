plugins {
    alias(libs.plugins.androidApplication)
    kotlin("android")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
    id("kotlin-allopen")

    alias(libs.plugins.navigationSafeArgs)
}

allOpen {
    // allows mocking for classes w/o directly opening them for release builds
    annotation("com.user.list.testing.OpenClass")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.user.list"
        minSdk = 22
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.incremental", "true")

            }
        }
        val APP_ID: String by project
        val BASE_URL: String by project
        buildConfigField("String", "APP_ID", APP_ID)
        buildConfigField("String", "BASE_URL", BASE_URL)
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = !project.hasProperty("android.injected.invoked.from.ide")
            enableAndroidTestCoverage = !project.hasProperty("android.injected.invoked.from.ide")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    namespace = "com.user.list"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.gson)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.picasso)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.dagger)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.recyclerview)
    debugImplementation(libs.androidx.fragment.testing)
    debugImplementation(libs.androidx.core)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.okhttp3.mockwebserver)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.mockito.core)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.androidx.test.truth)
    androidTestImplementation(libs.google.truth)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.mockito.core)
    androidTestImplementation(libs.espresso.contrib)

    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.androidx.room.compiler)
    kapt(libs.dagger.compiler)
}