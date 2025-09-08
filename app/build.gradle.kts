plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.navigation.safe.args)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

apply(from = rootProject.file("release_notes_generator.gradle"))

android {
    namespace = "nl.jovmit.lyrics"
    compileSdk = libs.versions.compileSdkVersion.get().toInt()

    defaultConfig {
        applicationId = "nl.jovmit.lyrics"
        minSdk = libs.versions.minSdkVersion.get().toInt()
        targetSdk = libs.versions.compileSdkVersion.get().toInt()
        versionCode = 16
        versionName = "1.0.0"

        testInstrumentationRunner = "nl.jovmit.lyrics.TestAppRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    compileOptions {
        val javaVersion = libs.versions.javaVersion.get()
        sourceCompatibility = JavaVersion.toVersion(javaVersion)
        targetCompatibility = JavaVersion.toVersion(javaVersion)
    }

    kotlinOptions {
        jvmTarget = libs.versions.javaVersion.get()
    }

    testOptions.unitTests {
        isReturnDefaultValues = true
        all { tests ->
            tests.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.coroitines)
    implementation(libs.bundles.compose)
    implementation(libs.firebase.firestore)

    debugImplementation(libs.bundles.compose.debug)

    androidTestImplementation(libs.bundles.ui.test)

    testImplementation(libs.bundles.unit.test)

    testRuntimeOnly(libs.jupiter.engine)
}