plugins{
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val versionName = "1.1.7"

android {
    namespace = "spa.lyh.cn.ft_webview"
    compileSdk = 34


    defaultConfig {
        minSdk = 19
    }

    buildTypes {
        release {
            isMinifyEnabled  = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),"proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility =  JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

}

dependencies {
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("io.github.liyuhaolol:CommonUtils:1.5.3")
}
