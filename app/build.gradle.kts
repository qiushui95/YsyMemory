import son.ysy.useful.dependencies.BuildVersion
import son.ysy.useful.dependencies.AndroidDependency

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion(BuildVersion.CompileSdk.value)
    buildToolsVersion(BuildVersion.BuildTools.value)

    defaultConfig {
        applicationId = "son.ysy.memory"
    }
    defaultConfig {
        minSdkVersion(BuildVersion.MinSdk.value)
        targetSdkVersion(BuildVersion.CompileSdk.value)

        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets {
        getAt("main").java.srcDirs("src/main/kotlin")
        getAt("debug").java.srcDirs("src/debug/kotlin")
        getAt("release").java.srcDirs("src/release/kotlin")
        getAt("test").java.srcDirs("src/test/kotlin")
        getAt("androidTest").java.srcDirs("src/androidTest/kotlin")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }

    ndkVersion = "22.0.7026061"
}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    implementation(AndroidDependency.Kotlin.Stdlib.fullGradle)
    implementation(AndroidDependency.Kotlin.Reflect.fullGradle)

    implementation(AndroidDependency.Single.Activity.fullGradle)
    implementation(AndroidDependency.Single.AndroidUtil.fullGradle)
    implementation(AndroidDependency.View.Material.fullGradle)
    implementation(AndroidDependency.View.ConstraintLayout.fullGradle)

    implementation(AndroidDependency.Single.JodaTime.fullGradle)

    implementation(AndroidDependency.Koin.Core.fullGradle)
    implementation(AndroidDependency.Koin.Ext.fullGradle)
    implementation(AndroidDependency.Koin.Scope.fullGradle)
    implementation(AndroidDependency.Koin.ViewModel.fullGradle)

    implementation(AndroidDependency.Single.Mmkv.fullGradle)

    implementation(AndroidDependency.Lifecycle.Common.fullGradle)
    implementation(AndroidDependency.Lifecycle.LiveData.fullGradle)
    implementation(AndroidDependency.Lifecycle.Runtime.fullGradle)
    implementation(AndroidDependency.Lifecycle.SavedState.fullGradle)
    implementation(AndroidDependency.Lifecycle.ViewModel.fullGradle)

    implementation(AndroidDependency.Navigation.Core.fullGradle)
    implementation(AndroidDependency.Navigation.Ui.fullGradle)
}