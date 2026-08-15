plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.rwoods.thecomicsoracle"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    defaultConfig {
        applicationId = "com.rwoods.thecomicsoracle"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            applicationIdSuffix = ".release"
            buildConfigField("String", "BASE_URL", "\"https://comicvine.gamespot.com/api/\"")
            buildConfigField("String", "API_KEY", "\"310286c7441deb2eac2cc1aeae01393b69c99452\"")
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"))
        }

        getByName("debug") {
            buildConfigField("String", "BASE_URL", "\"https://comicvine.gamespot.com/api/\"")
            buildConfigField("String", "API_KEY", "\"310286c7441deb2eac2cc1aeae01393b69c99452\"")
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

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
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

    // Android Support dependencies
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    implementation(libs.androidx.constraintlayout)

    // Kotlin
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)

    //Room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

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
    kapt(libs.glide.compiler)
    implementation(libs.glide.compose)

    // Logging
    implementation(libs.slf4j.simple)

    // Firebase
    implementation(libs.firebase.core)
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
    androidTestImplementation(libs.androidx.test.rules)

    // Espresso core
    androidTestImplementation(libs.androidx.test.espresso.core) {
        exclude(module = "support-annotations")
    }
    // Espresso-contrib for DatePicker, RecyclerView, Drawer actions, Accessibility checks, CountingIdlingResource
    androidTestImplementation(libs.androidx.test.espresso.contrib) {
        exclude(module = "support-annotations")
        exclude(module = "support-v4")
        exclude(module = "support-v13")
        exclude(module = "design")
        exclude(module = "appcompat-v7")
        exclude(module = "recyclerview-v7")
    }

    androidTestImplementation(libs.androidx.annotation)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
}

kotlin {
    jvmToolchain(17)
}

hilt {
    enableExperimentalClasspathAggregation = true
}
