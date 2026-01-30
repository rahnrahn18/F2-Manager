plugins {
    id("com.android.library")
    id("com.vanniktech.maven.publish.base")
}

android {
    namespace = "io.github.rosemoe.sora"

    // Explicitly set SDK versions as requested
    compileSdk = 36
    buildToolsVersion = "35.0.1"
    ndkVersion = "28.2.13676358"

    defaultConfig {
        minSdk = 26
        targetSdk = 36
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
}

dependencies {
    // api(libs.androidx.annotation) // Version catalog 'libs' likely unavailable in this monorepo context unless migrated
    // Replacing with direct dependencies or assuming resolution strategy handles it
    implementation("androidx.annotation:annotation:1.9.0")
    implementation("androidx.collection:collection:1.4.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22") // Example version
}
