plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.openapi.generator)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.poker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.poker"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // Module dependencies
    implementation(project(":api-client"))

    // Android core dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.viewbinding)
    implementation(libs.androidx.swiperefreshlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Networking
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.gson)
    implementation(libs.gson.fire)
    implementation(libs.okhttp.logging)

    // Architecture components
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Dependency Injection
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.android)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // SignalR for real-time updates
    implementation(libs.signalr)
}
// For Hilt annotation processing - disabling aggregating task
hilt {
    enableAggregatingTask = false
}

// Enhance kapt configuration
kapt {
    correctErrorTypes = true
    // Add these options for better performance and error handling
    arguments {
        arg("dagger.fastInit", "enabled")
        arg("dagger.experimentalDaggerErrorMessages", "enabled")
    }
}