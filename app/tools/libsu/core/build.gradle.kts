plugins {
    id("com.android.library")
}

group="com.github.topjohnwu.libsu"

android {
    namespace = "com.topjohnwu.superuser"

    compileSdk = 36
    buildToolsVersion = "35.0.1"
    ndkVersion = "28.2.13676358"

    defaultConfig {
        minSdk = 26
        targetSdk = 36
        consumerProguardFiles("proguard-rules.pro")

        externalNativeBuild {
            cmake { version = "4.1.1" }
        }
    }
}

dependencies {
    compileOnly("androidx.annotation:annotation:1.9.0")
    // javadocDeps("androidx.annotation:annotation:1.9.0") // Assuming standard config, removal if not needed or update
}
