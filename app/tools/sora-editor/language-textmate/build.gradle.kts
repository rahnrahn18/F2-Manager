plugins {
    id("com.android.library")
}

android {
    namespace = "io.github.rosemoe.sora.langs.textmate"
    compileSdk = (rootProject.extra["compile_sdk"] as Int)
    buildToolsVersion = (rootProject.extra["build_tools"] as String)
    ndkVersion = (rootProject.extra["ndk_version"] as String)

    defaultConfig {
        minSdk = (rootProject.extra["min_sdk"] as Int)
        targetSdk = (rootProject.extra["target_sdk"] as Int)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    compileOnly(project(":sora-editor"))
    compileOnly(project(":sora-oniguruma"))

    implementation("com.google.code.gson:gson:2.13.1")
    implementation("org.jruby.jcodings:jcodings:1.0.55")
    implementation("org.jruby.joni:joni:2.1.41")
    implementation("org.snakeyaml:snakeyaml-engine:2.7")

    implementation("org.eclipse.jdt:org.eclipse.jdt.annotation:2.2.600") // Approximation
}
