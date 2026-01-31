plugins {
    id("com.android.library")
}

android {
    compileSdk = (rootProject.extra["compile_sdk"] as Int)
    buildToolsVersion = (rootProject.extra["build_tools"] as String)
    namespace = "org.lsposed.hiddenapibypass.library"

    buildFeatures {
        buildConfig = true
        androidResources = false
    }

    defaultConfig {
        minSdk = (rootProject.extra["min_sdk"] as Int)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        targetSdk = (rootProject.extra["target_sdk"] as Int)
    }

    buildTypes {
        release {
            consumerProguardFiles("consumer-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    compileOnly(project(":hidden-api-stub"))
    compileOnly("androidx.annotation:annotation:1.9.1")
}
