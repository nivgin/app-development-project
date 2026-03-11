import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

// -------------------------------
// Load local.properties
// -------------------------------
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

// Safely read API keys
val cloudName: String = localProperties.getProperty("cloudinary.cloud_name")
    ?: throw GradleException("cloudinary.cloud_name not found in local.properties")
val apiKey: String = localProperties.getProperty("cloudinary.api_key")
    ?: throw GradleException("cloudinary.api_key not found in local.properties")
val apiSecret: String = localProperties.getProperty("cloudinary.api_secret")
    ?: throw GradleException("cloudinary.api_secret not found in local.properties")
val fatsecretAPIKey: String = localProperties.getProperty("fatsecret.api_key")
    ?: throw GradleException("fatsecret.api_key not found in local.properties")
val fatsecretAPISecret: String = localProperties.getProperty("fatsecret.api_secret")
    ?: throw GradleException("fatsecret.api_secret not found in local.properties")
android {
    namespace = "com.example.recipease"
    compileSdk = 36
    viewBinding {
        enable = true
    }
    defaultConfig {
        buildConfigField("String", "CLOUDINARY_CLOUD_NAME", "\"$cloudName\"")
        buildConfigField("String", "CLOUDINARY_API_KEY", "\"$apiKey\"")
        buildConfigField("String", "CLOUDINARY_API_SECRET", "\"$apiSecret\"")
        buildConfigField("String", "FATSECRET_API_KEY", "\"$fatsecretAPIKey\"")
        buildConfigField("String", "FATSECRET_API_SECRET", "\"$fatsecretAPISecret\"")

        applicationId = "com.example.recipease"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures { buildConfig = true }

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.swiperefreshlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("se.akerfeldt:okhttp-signpost:1.1.0")
    implementation("oauth.signpost:signpost-core:2.1.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation(platform("com.google.firebase:firebase-bom:34.9.0"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.cloudinary:cloudinary-android:2.3.1")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
}