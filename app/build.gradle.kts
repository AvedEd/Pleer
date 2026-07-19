plugins {
    id 'com.android.application'
    id 'kotlin-android'
}

android {
    namespace "com.torrserv.pleer"
    compileSdk 34

    defaultConfig {
        applicationId "com.torrserv.pleer"
        minSdk 21
        targetSdk 34
        versionCode 1
        versionName "1.0.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.debug
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = '11'
    }

    buildFeatures {
        compose true
    }

    composeOptions {
        kotlinCompilerExtensionVersion "1.5.0"
    }
}

dependencies {
    // AndroidX
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.6.2'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2'
    implementation 'androidx.activity:activity-compose:1.8.0'
    implementation 'androidx.datastore:datastore-preferences:1.0.0'

    // Compose
    implementation 'androidx.compose.ui:ui:1.5.4'
    implementation 'androidx.compose.ui:ui-graphics:1.5.4'
    implementation 'androidx.compose.ui:ui-tooling-preview:1.5.4'
    implementation 'androidx.compose.material3:material3:1.1.1'
    implementation 'androidx.compose.foundation:foundation:1.5.4'
    implementation 'androidx.compose.material:material-icons-extended:1.5.4'

    // Media3 (ExoPlayer)
    implementation 'androidx.media3:media3-exoplayer:1.1.1'
    implementation 'androidx.media3:media3-ui:1.1.1'
    implementation 'androidx.media3:media3-common:1.1.1'
    implementation 'androidx.media3:media3-datasource-okhttp:1.1.1'

    // Network
    implementation 'com.squareup.okhttp3:okhttp:4.11.0'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.google.code.gson:gson:2.10.1'

    // Logging
    implementation 'com.jakewharton.timber:timber:5.0.1'

    // Testing
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'androidx.test.ext:junit:1.1.5'
    testImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    androidTestImplementation 'androidx.compose.ui:ui-test-junit4:1.5.4'
    debugImplementation 'androidx.compose.ui:ui-tooling:1.5.4'
    debugImplementation 'androidx.compose.ui:ui-test-manifest:1.5.4'
}
