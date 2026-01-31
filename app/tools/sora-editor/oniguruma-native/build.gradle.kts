plugins {
    id("com.android.library")
}

android {
    namespace = "io.github.rosemoe.sora.oniguruma"
    compileSdk = (rootProject.extra["compile_sdk"] as Int)
    buildToolsVersion = (rootProject.extra["build_tools"] as String)
    ndkVersion = (rootProject.extra["ndk_version"] as String)

    defaultConfig {
        minSdk = (rootProject.extra["min_sdk"] as Int)
        targetSdk = (rootProject.extra["target_sdk"] as Int)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    externalNativeBuild {
        cmake {
            path = project.file("src/main/cpp/CMakeLists.txt")
            version = "4.1.1" // User specified 4.1.1 CMake
        }
    }
}

dependencies {
}
