import java.util.Properties
import java.util.Base64

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
    alias(libs.plugins.kotlin.compose)
}

tasks.register("generateGoogleServicesJson") {
    doFirst {
        val yamlFile = rootProject.file("google-services.yml")
        if (yamlFile.exists()) {
            val content = yamlFile.readText()
            val regex = "google_services_base64\\s*:\\s*[\"']([^\"']+)[\"']".toRegex()
            val matchResult = regex.find(content)
            val base64Str = matchResult?.groups?.get(1)?.value
            if (!base64Str.isNullOrEmpty()) {
                val decodedBytes = Base64.getDecoder().decode(base64Str)
                val targetFile = file("google-services.json")
                targetFile.writeBytes(decodedBytes)
            }
        }
    }
}

tasks.named("preBuild").configure {
    dependsOn("generateGoogleServicesJson")
}

android {
    namespace = "com.rwoods.thecomicsoracle"
    compileSdk = 37

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    val comicsVineApiKey = try {
        val process = ProcessBuilder("gcloud", "secrets", "versions", "access", "latest", "--secret=COMICS_VINE_API_KEY")
            .redirectErrorStream(true)
            .start()
        val output = process.inputStream.bufferedReader().readText().trim()
        val exitCode = process.waitFor()
        if (exitCode == 0 && output.isNotEmpty()) {
            output.removeSurrounding("\"").removeSurrounding("'")
        } else {
            project.findProperty("ComicsVineApiKey")?.toString()?.removeSurrounding("\"") ?: ""
        }
    } catch (_: Exception) {
        project.findProperty("ComicsVineApiKey")?.toString()?.removeSurrounding("\"") ?: ""
    }

    defaultConfig {
        applicationId = "com.rwoods.thecomicsoracle"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "COMICS_VINE_API_KEY", "\"$comicsVineApiKey\"")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            applicationIdSuffix = ".release"
            buildConfigField("String", "BASE_URL", "\"https://comicvine.gamespot.com/api/\"")
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }

        getByName("debug") {
            buildConfigField("String", "BASE_URL", "\"https://comicvine.gamespot.com/api/\"")
            applicationIdSuffix = ".debug"
        }

        create("jnidebug") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".jnidebug"
            isJniDebuggable = true
        }
    }
    flavorDimensions += listOf("density")

    productFlavors {
        create("fullmdpi") {
            dimension = "density"
            minSdk = 23
        }

        create("fullhdpi") {
            dimension = "density"
            minSdk = 23
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }

    packagingOptions {
        resources {
            excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/LICENSE",
                "META-INF/NOTICE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE.txt"
            )
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    // Android Support dependencies
    implementation(libs.androidx.multidex)
    implementation(libs.firebase.config)
    implementation(libs.google.material)

    // Kotlin
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.core)

    //Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)

    // Square dependencies
    implementation(libs.retrofit)
    implementation(libs.retrofit.mock)
    implementation(libs.okhttp.urlconnection)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.moshi)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi.kotlin)

    // Apache Collections Commons
    implementation(libs.apache.commons.lang3)
    implementation(libs.apache.commons.collections4)
    implementation(libs.commons.codec)

    // Glide
    implementation(libs.glide)
    implementation(libs.glide.compose)
    ksp(libs.glide)

    // Logging
    implementation(libs.slf4j.simple)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)

    // Recommended: Add the Firebase SDK for Google Analytics.
    implementation(libs.firebase.analytics)

    // Add the Firebase SDK for Crashlytics.
    implementation(libs.firebase.crashlytics)

    // Timber
    implementation(libs.timber)

    // Google Authentication
    implementation(libs.play.services.auth)

    // JUnit
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)

    // Android JUnit Runner
    androidTestImplementation(libs.androidx.test.runner)

    // JUnit4 Rules
    androidTestImplementation(libs.androidx.annotation)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
}

kotlin {
    jvmToolchain(17)
}

hilt {
    enableExperimentalClasspathAggregation = true
}
