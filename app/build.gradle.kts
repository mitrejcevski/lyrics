plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.navigation.safe.args)
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
    val koinVersion = "3.4.3"
    val coroutinesVersion = "1.7.2"
    val mockitoKotlinVersion = "2.2.0"
    val androidArchNavigationVersion = "2.7.0"
    val activityKtxVersion = "1.7.2"
    val fragmentKtxVersion = "1.6.1"
    val swipeRefreshLayoutVersion = "1.1.0"

    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:$swipeRefreshLayoutVersion")
    implementation("androidx.activity:activity-ktx:$activityKtxVersion")
    implementation("androidx.fragment:fragment-ktx:$fragmentKtxVersion")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$androidArchNavigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$androidArchNavigationVersion")
    implementation("com.google.firebase:firebase-firestore:24.7.0")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:rules:1.5.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    testImplementation("io.insert-koin:koin-test:$koinVersion")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:$mockitoKotlinVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:3.6.28")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}