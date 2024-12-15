plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.tourismapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tourismapp"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")


        implementation ("org.mongodb:mongodb-driver-sync:4.4.0")


    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation(libs.volley)
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("com.google.android.material:material:1.6.0")
    implementation ("androidx.coordinatorlayout:coordinatorlayout:1.2.0")

}