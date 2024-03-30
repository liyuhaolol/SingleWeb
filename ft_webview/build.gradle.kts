import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties
import net.thebugmc.gradle.sonatypepublisher.PublishingType.AUTOMATIC

plugins{
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("net.thebugmc.gradle.sonatype-central-portal-publisher") version "1.2.3"
}

val versionName = "1.1.8"

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

var signingKeyId = ""//签名的密钥后8位
var signingPassword = ""//签名设置的密码
var secretKeyRingFile = ""//生成的secring.gpg文件目录
var ossrhUsername = ""//sonatype用户名
var ossrhPassword = "" //sonatype密码

val localProperties = project.rootProject.file("local.properties")

if (localProperties.exists()) {
    println("Found secret props file, loading props")
    val properties = Properties()

    InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
        properties.load(reader)
    }
    signingKeyId = properties.getProperty("signingKeyId")
    signingPassword = properties.getProperty("signingPassword")
    secretKeyRingFile = properties.getProperty("secretKeyRingFile")
    ossrhUsername = properties.getProperty("ossrhUsername")
    ossrhPassword = properties.getProperty("ossrhPassword")

} else {
    println("No props file, loading env vars")
}


centralPortal {
    username = ossrhUsername
    password = ossrhPassword
    name = "SingleWeb"
    group = "io.github.liyuhaolol"
    version = versionName
    pom {
        //packaging = "aar"
        name = "SingleWeb"
        description = "webview for android"
        url = "https://github.com/liyuhaolol/SingleWeb"
        licenses {
            license {
                name = "The MIT License"
                url = "https://github.com/liyuhaolol/SingleWeb/blob/master/LICENSE"
            }
        }
        developers {
            developer {
                id = "liyuhao"
                name = "liyuhao"
                email = "liyuhaoid@sina.com"
            }
        }
        scm {
            connection = "scm:git@github.com/liyuhaolol/SingleWeb.git"
            developerConnection = "scm:git@github.com/liyuhaolol/SingleWeb.git"
            url = "https://github.com/liyuhaolol/SingleWeb"
        }

    }
    publishingType = AUTOMATIC
    javadocJarTask = tasks.create<Jar>("javadocEmptyJar") {
        archiveClassifier = "javadoc"
    }

}


gradle.taskGraph.whenReady {
    if (allTasks.any { it is Sign }) {
        allprojects {
            extra["signing.keyId"] = signingKeyId
            extra["signing.secretKeyRingFile"] = secretKeyRingFile
            extra["signing.password"] = signingPassword
        }
    }
}

signing {
    sign(publishing.publications)
}
