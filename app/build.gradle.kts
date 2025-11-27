plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
}

android {
    namespace = "legom.gpstracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "legom.gpstracker"
        minSdk = 21
        targetSdk = 34
        versionCode = 5
        versionName = "1.0.5"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding = true
    }
}



dependencies {

    //Yandex sdk
    implementation ("com.yandex.android:mobileads:7.16.1")
    implementation("com.yandex.ads.mediation:mobileads-mytarget:5.27.3.1")
    implementation("io.appmetrica.analytics:analytics:7.13.0")
//    implementation("com.yandex.ads.mediation:mobileads-mintegral:16.9.81.2")
////
//    implementation("com.mbridge.msdk.oversea:mbridge_android_sdk:17.0.21")

    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)


    implementation(libs.play.services.location)

    implementation (libs.androidx.preference.ktx)

    implementation(libs.osmdroid.android)
    implementation(libs.osmbonuspack)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}