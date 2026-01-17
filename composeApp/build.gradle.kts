import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.realm.kotlin)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    // 1. Подавляем предупреждение о Beta для expect/actual во всем проекте
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
            // Дублируем флаг для Android компилятора
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }
    }

    // Настройка iOS таргетов (только если не Windows)
    if (!System.getProperty("os.name").contains("Windows", ignoreCase = true)) {
        listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
            it.binaries.framework {
                baseName = "composeApp"
                isStatic = true
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(libs.realm.library.base)
                implementation(libs.kotlinx.datetime)
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.screenmodel)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.core.ktx)
                implementation(libs.yandex.mobileads)
                implementation(libs.androidx.work.runtime.ktx) // Необходим для NotificationManager
            }
        }

        // iOS зависимости
        if (!System.getProperty("os.name").contains("Windows", ignoreCase = true)) {
            val iosMain by getting {
                dependencies {
                    implementation(libs.mobileads.ios)
                }
            }
        }
    }
}

android {
    namespace = "com.pixelrabbit.oculi"
    compileSdk = 35

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.pixelrabbit.oculi"
        minSdk = 24
        targetSdk = 35
        versionCode = 31
        versionName = "1.31.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}