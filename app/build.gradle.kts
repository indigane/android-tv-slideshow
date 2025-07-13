import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

fun getMinutesSinceEpoch(): Int {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val epoch = sdf.parse("2025-07-13T00:00:00Z")
    val now = Date()
    val diff = now.time - epoch.time
    return (diff / (60 * 1000)).toInt()
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

fun getGitUserSuffix(): String {
    try {
        val process = ProcessBuilder("git", "remote", "get-url", "origin").start()
        val remoteUrl = process.inputStream.bufferedReader().readText().trim()
        process.waitFor()
        // Regex for git@github.com:owner/repo and https://github.com/owner/repo capturing the owner
        val regex = Regex("[:/]([^/]+)/[^/]+(\\.git)?/?$")
        val matchResult = regex.find(remoteUrl)
        return matchResult?.groups?.get(1)?.value?.lowercase()?.let { ".$it" } ?: ".local"
    } catch (e: Exception) {
        println("Could not get git user: ${e.message}")
        return ".local"
    }
}

android {
    namespace = "home.photo_slideshow"
    compileSdk = 35
    defaultConfig {
        applicationId = "home.photo_slideshow"
        minSdk = 26
        targetSdk = 35
        versionCode = if (gradle.startParameter.taskNames.any { it.contains("Debug") }) getMinutesSinceEpoch() else 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    // Exclude dependencies info to fix "Problem: found extra signing block 'Dependency metadata'" in F-Droid.
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
    signingConfigs {
        create("release") {
            if (System.getenv("KEYSTORE_PATH") != null) {
                storeFile = file(System.getenv("KEYSTORE_PATH"))
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                storeType = "PKCS12"
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }
    buildTypes {
        getByName("release") {
            applicationIdSuffix = getGitUserSuffix()
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8 // Or JavaVersion.VERSION_17 if preferred
        targetCompatibility = JavaVersion.VERSION_1_8 // Or JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "1.8" // Or "17"
    }
    buildFeatures {
        viewBinding = true // Enable ViewBinding for easier UI interaction
    }
}

dependencies {
    // Core libraries
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("com.google.android.material:material:1.12.0")

    // For modern SMB (Samba) connectivity
    implementation("com.hierynomus:smbj:0.11.5")

    // For efficient image loading and caching
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Android TV Leanback library for UI components and themes
    implementation("androidx.leanback:leanback:1.0.0")

    // Testing libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
