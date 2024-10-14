plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
   id("com.google.gms.google-services")

}


android {
    namespace = "com.example.diplom1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.diplom1"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
       // kotlinCompilerExtensionVersion = "1.6.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildToolsVersion = "34.0.0"

}

dependencies {


    implementation("com.google.firebase:firebase-storage:21.0.1")
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.0")
    implementation ("com.google.firebase:firebase-appcheck:18.0.0")
    implementation ("com.google.firebase:firebase-appcheck-playintegrity:18.0.0")
    implementation("com.google.android.gms:play-services-vision-common:19.1.3")
    // Camerax implementation
    val camerax_version = "1.4.0-alpha04"

    implementation ("androidx.camera:camera-core:1.3.4")
    implementation ("androidx.camera:camera-camera2:1.3.4")
    implementation ("androidx.camera:camera-lifecycle:1.3.4")
    implementation("androidx.camera:camera-video:${camerax_version}")
    implementation("androidx.camera:camera-view:${camerax_version}")
    implementation("androidx.camera:camera-mlkit-vision:${camerax_version}")
    implementation("androidx.camera:camera-extensions:${camerax_version}")
    // Camerax implementation
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.1")
    implementation("androidx.camera:camera-mlkit-vision:1.3.0-beta02")
    //для распознавания лиц
    implementation("com.google.mlkit:face-detection:16.1.7")
    //для распознавания лиц
    //база FirebaseФ
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
 //   implementation("com.google.firebase:firebase-firestore:24.11.1")
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-analytics")
    //база Firebase

    //Tesseract
    implementation ("com.rmtheis:tess-two:9.1.0")
     implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")
    // LiveData integration
    implementation ("androidx.compose.runtime:runtime-livedata:1.7.3")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

        //   implementation ("com.google.android.gms:play-services-vision:20.1.3")
    //Tesseract
    //для видио

   // implementation ("org.webrtc:google-webrtc:1.0.32006")
   // implementation ("org.webrtc:google-webrtc:1.0.32006")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
 //  implementation ("com.github.ZEGOCLOUD:zego_uikit_prebuilt_call_android:+")
    //
       //  implementation ("org.jitsi.react:jitsi-meet-sdk:+")

    implementation ("org.mozilla:rhino:1.7.13")
   // implementation ("io.agora:access-token:2.1.0")
   // implementation ("io.agora.rtc:full-sdk:3.5.1")
    // //implementation ("io.agora.rtc:voice-sdk:4.2.6")
  //  implementation ("io.agora.uikit:agorauikit:2.0.1")
  // implementation 'io.agora.uikit:agorauikit:2.0.1'
    implementation("io.coil-kt:coil:2.6.0")
    implementation( "io.coil-kt:coil-compose:2.3.0")
   // implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.1")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui:1.6.7")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation ("androidx.compose.material:material:1.6.7")
    implementation("androidx.appcompat:appcompat:1.7.0")
  //  implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}