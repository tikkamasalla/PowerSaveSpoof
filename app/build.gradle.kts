plugins {
    id("com.android.application")
}

android {
    namespace = "com.tikkamasalla.powersavespoof"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.tikkamasalla.powersavespoof"
        minSdk = 29
        targetSdk = 34
        versionCode = 4
        versionName = "1.3"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    compileOnly("de.robv.android.xposed:api:82")
}
