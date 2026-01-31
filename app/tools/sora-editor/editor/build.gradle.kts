plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    // id("com.vanniktech.maven.publish.base") // Removed
}

android {
    namespace = "io.github.rosemoe.sora"

    // Explicitly set SDK versions as requested
    compileSdk = (rootProject.extra["compile_sdk"] as Int)
    buildToolsVersion = (rootProject.extra["build_tools"] as String)
    ndkVersion = (rootProject.extra["ndk_version"] as String)

    defaultConfig {
        minSdk = (rootProject.extra["min_sdk"] as Int)
        targetSdk = (rootProject.extra["target_sdk"] as Int)
        // Removed testInstrumentationRunner as tests are pruned

        externalNativeBuild {
            cmake {
                version = "4.1.1"
            }
        }
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // api(libs.androidx.annotation) // Version catalog 'libs' likely unavailable in this monorepo context unless migrated
    // Replacing with direct dependencies or assuming resolution strategy handles it
    implementation("androidx.annotation:annotation:1.9.0")
    implementation("androidx.collection:collection:1.4.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22") // Example version
}
