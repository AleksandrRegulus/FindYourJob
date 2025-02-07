plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("ru.practicum.android.diploma.plugins.developproperties")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {

    buildFeatures {
        viewBinding = true
    }

    namespace = "ru.practicum.android.diploma"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.practicum.android.diploma"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(type = "String", name = "HH_ACCESS_TOKEN", value = "\"${developProperties.hhAccessToken}\"")

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        buildConfig = true
    }
}

dependencies {
    // Annotation processors
    annotationProcessor(libs.ui.glide)

    // Kotlin annotation processing
    kapt(libs.database.roomCompiler)

    implementation(libs.androidX.core)
    implementation(libs.androidX.appCompat)
    implementation(libs.androidX.fragmetnKtx)

    // UI layer libraries
    implementation(libs.ui.material)
    implementation(libs.ui.constraintLayout)
    implementation(libs.ui.glide)

    // Network
    implementation(libs.network.retrofit)
    implementation(libs.network.gsonConverter)

    // DI
//    implementation(libs.di.daggerCore)
//    implementation(libs.di.daggerCompiler)


    implementation(libs.di.hiltAndroid)
    kapt(libs.di.hiltAndroidCompiler)
    // DI hilt support ViewModel
//    implementation(libs.di.hiltLifecycleViewmodel)
//    kapt(libs.di.hiltCompiler)

    // Navigation
    implementation(libs.navigation.fragmentKtx)
    implementation(libs.navigation.uiKtx)

    // Database
    implementation(libs.database.roomRuntime)
    implementation(libs.database.roomKtx)

    // region Unit tests
    testImplementation(libs.unitTests.junit)
    // endregion

    // region UI tests
    androidTestImplementation(libs.uiTests.junitExt)
    androidTestImplementation(libs.uiTests.espressoCore)
    // endregion
}

kapt {
    correctErrorTypes = true
}
