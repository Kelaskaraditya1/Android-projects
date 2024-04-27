plugins {
    id("com.android.application")
    id("com.google.gms.google-services")

}
android {
    namespace = "com.starkindustries.attendence_system"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.starkindustries.attendence_system"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures{
        dataBinding=true
    }

}
dependencies {
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }
//    implementation("com.google.android.gms:play-services-vision:20.0.0'")
//    {
//        exclude("com.google.android.gms', module: 'play-services-vision-common")
//    }
//    implementation("com.google.firebase:firebase-bom:31.0.0")
//    implementation("com.google.firebase:firebase-ml-vision-face-model:20.0.2")
//    implementation("com.google.android.gms:play-services-mlkit-face-detection:17.0.0")
//    implementation("com.google.android.gms:play-services-vision-common:20.1.3")
    implementation("com.google.android.gms:play-services-mlkit-face-detection:17.1.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.google.android.material:compose-theme-adapter:1.0.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-firestore:24.10.2")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("androidx.activity:activity:1.8.0")
    implementation("com.google.android.gms:play-services-vision-common:19.1.3")
    implementation("androidx.camera:camera-core:1.3.2")
//    implementation("com.google.firebase:firebase-admin:9.2.0")
    implementation("androidx.camera:camera-lifecycle:1.3.2")
//    implementation("com.google.firebase:firebase-ml-vision:24.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}