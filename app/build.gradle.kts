import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialization)
}

val apikeyProperties = Properties().apply {
    load(File(rootDir, "apikey.properties").inputStream())
}


android {
    namespace = "com.giovanna.amatucci.melisearch"
    compileSdk = 36
    buildToolsVersion = "36.0.0"

    defaultConfig {
        applicationId = "com.giovanna.amatucci.melisearch"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField(
            "String",
            "PUBLIC_KEY",
            "\"${apikeyProperties.getProperty("PUBLIC_KEY")}\""
        )
        buildConfigField(
            "String",
            "PRIVATE_KEY",
            "\"${apikeyProperties.getProperty("PRIVATE_KEY")}\""
        )
        buildConfigField("String", "BASE_URL", "\"api.mercadolibre.com\"")
        buildConfigField(
            "String",
            "DB_PASSPHRASE",
            "\"${apikeyProperties.getProperty("DB_PASSPHRASE")}\""
        )

    }

    buildTypes {
        release {
            applicationIdSuffix = ".release"
            versionNameSuffix = "-release"
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("Boolean", "DEBUG_MODE", "false")
            buildConfigField("Long", "REQUEST_TIMEOUT", "20_000L")
            buildConfigField("Long", "CONNECT_TIMEOUT", "15_000L")
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            buildConfigField("Boolean", "DEBUG_MODE", "true")
            buildConfigField("Long", "REQUEST_TIMEOUT", "30_000L")
            buildConfigField("Long", "CONNECT_TIMEOUT", "20_000L")
        }
        create("staging") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            buildConfigField("Boolean", "DEBUG_MODE", "false")
            buildConfigField("Long", "REQUEST_TIMEOUT", "30_000L")
            buildConfigField("Long", "CONNECT_TIMEOUT", "20_000L")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // --- KotlinX ---
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.datetime)

    // --- Jetpack Compose & UI --- 🚀
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)

    // --- Visualization and debugging tools ---
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.text)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // --- Architecture and Lifecycle ---
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtimeKtx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.core.splashscreen)

    // --- Dependency Injection (Koin) ---
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.compose.viewmodel)


    // --- Networking (Ktor) ---
    implementation(libs.ktor.core)
    implementation(libs.ktor.auth)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.client.mock)
    implementation(libs.ktor.json)
    implementation(libs.ktor.logging)

    // --- Room ---
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    //implementation(libs.room.testing)

    // --- Image Loading (Glide) ---
    implementation(libs.glide.compose)
    implementation(libs.glide.ksp)

    // --- Paging ---
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    // --- Logging (Timber) ---
    implementation(libs.timber)

    // --- Coroutines ---
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)

    // --- SQLCipher ---
    implementation(libs.sqlcipher)

    // --- Unit Testing (Local) --- 🧪
    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.turbine)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.koin.test)
    testImplementation(libs.paging.test)

    // --- Instrumentation Tests (Android) --- 🧪
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.test.coreKtx)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}