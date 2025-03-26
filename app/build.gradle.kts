plugins {
    id("com.android.application")
}

android {
    namespace = "com.wearos.ox"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wearos.ox"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.wear:wear:1.3.0")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("com.google.android.support:wearable:2.9.0")
    implementation(libs.cardview)
    compileOnly("com.google.android.wearable:wearable:2.9.0")
}

// Force all Kotlin dependencies to use version 1.8.22
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion("1.8.22")
        }
    }
}
