import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

// Versión única fuente de verdad. versionCode = MAJOR*10000 + MINOR*100 + PATCH
val verMajor = 1
val verMinor = 1
val verPatch = 0

val keystoreProps = Properties().apply {
    val f = rootProject.file("keystore.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}

android {
    namespace = "com.aether.tv"
    compileSdk = 35

    defaultConfig {
        // NO CAMBIAR NUNCA: garantiza que las actualizaciones se instalen sobre versiones anteriores.
        applicationId = "com.aether.tv"
        minSdk = 24
        targetSdk = 35
        versionCode = verMajor * 10000 + verMinor * 100 + verPatch
        versionName = "$verMajor.$verMinor.$verPatch"
        buildConfigField("String", "GITHUB_REPO", "\"aether-android-tv\"")
        buildConfigField("String", "GITHUB_OWNER", "\"${project.findProperty("githubOwner") ?: "CAMBIAR_USUARIO"}\"")
    }

    signingConfigs {
        create("release") {
            val storePath = System.getenv("AETHER_KEYSTORE_PATH") ?: keystoreProps.getProperty("storeFile")
            if (storePath != null) {
                storeFile = file(storePath)
                storePassword = System.getenv("AETHER_KEYSTORE_PASSWORD") ?: keystoreProps.getProperty("storePassword")
                keyAlias = System.getenv("AETHER_KEY_ALIAS") ?: keystoreProps.getProperty("keyAlias")
                keyPassword = System.getenv("AETHER_KEY_PASSWORD") ?: keystoreProps.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            if (signingConfigs.getByName("release").storeFile != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.10.01")
    implementation(composeBom)
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.tv:tv-foundation:1.0.0-alpha11")
    implementation("androidx.tv:tv-material:1.0.0")
    implementation("androidx.navigation:navigation-compose:2.8.3")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}
