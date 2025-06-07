plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.openapi.generator)
}

android {
    namespace = "com.apiclient"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.gson)
    implementation(libs.gson.fire)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
}

// Apply swagger downloader script
apply(from = "${project.projectDir}/gradle/swagger-downloader.gradle.kts")

// Get the swagger file path from the downloader script
val swaggerFilePath = project.extra["swaggerFilePath"] as String

// Store a property to control code generation
val shouldGenerateApiCode = project.hasProperty("generateApi")

// OpenAPI Generator configuration
openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set(swaggerFilePath)
    outputDir.set(layout.buildDirectory.dir("generated/openapi").get().asFile.absolutePath)

    // Package configuration
    packageName.set("com.apiclient.api")
    apiPackage.set("com.apiclient.api.apis")
    modelPackage.set("com.apiclient.api.models")

    // Generator options
    configOptions.set(mapOf(
        "library" to "jvm-retrofit2",
        "serializationLibrary" to "gson",
        "dateLibrary" to "java8",
        "interfacesOnly" to "true",
        "enumPropertyNaming" to "UPPERCASE",
        "collectionType" to "list"
    ))
}

// Include generated sources in the build
android.sourceSets.getByName("main") {
    java.srcDir("${layout.buildDirectory.get()}/generated/openapi/src/main/kotlin")
}

// IMPORTANT: Only depend on openApiGenerate when explicitly requested
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    if (shouldGenerateApiCode) {
        dependsOn("openApiGenerate")
    }
}

// Create a manual task for API generation
tasks.register("generateApiClient") {
    group = "openapi"
    description = "Downloads Swagger spec and generates API client code"

    // This will invoke the generate task
    dependsOn("openApiGenerate")

    doLast {
        println("API client generated successfully!")
    }
}
