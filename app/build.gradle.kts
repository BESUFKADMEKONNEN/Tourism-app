plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
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
//
//dependencies {
//
//    implementation(libs.appcompat)
//    implementation(libs.material)
//    implementation(libs.constraintlayout)
//    implementation(libs.lifecycle.livedata.ktx)
//    implementation(libs.lifecycle.viewmodel.ktx)
//    implementation(libs.navigation.fragment)
//    implementation(libs.navigation.ui)
//    implementation(libs.activity)
//    implementation(libs.firebase.database)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.ext.junit)
//    androidTestImplementation(libs.espresso.core)
//
//    //fetch from api
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
//    implementation("androidx.appcompat:appcompat:1.6.0")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
//
//    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
//
//    implementation ("com.github.bumptech.glide:glide:4.15.1")
//    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
//
//
//        implementation ("org.mongodb:mongodb-driver-sync:4.4.0")
//
//    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
//    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
//
//
//
//    implementation ("androidx.recyclerview:recyclerview:1.3.2")
//    implementation ("com.squareup.picasso:picasso:2.71828")
//    implementation(libs.volley)
//    implementation ("com.google.android.material:material:1.9.0")
//    implementation ("com.google.android.material:material:1.6.0")
//    implementation ("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
//
//}

dependencies {

    // AppCompat, Material Design, ConstraintLayout
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    // Lifecycle
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Activity
    implementation(libs.activity)

    // Firebase
    implementation(libs.firebase.database)
    implementation ("com.google.firebase:firebase-storage:21.0.1")
    implementation (libs.google.firebase.database.v2003)
    implementation ("com.google.firebase:firebase-auth:21.0.0")
    implementation ("com.google.firebase:firebase-appcheck:18.0.0")


    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Image Loading
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    implementation("com.squareup.picasso:picasso:2.71828")


    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Volley
    implementation(libs.volley)

    // CoordinatorLayout
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
}
