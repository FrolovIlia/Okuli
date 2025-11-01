import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinSerialization)
    id("com.codingfeline.buildkonfig") version "0.15.1"
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.materialIconsExtended)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.client.logging)

                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)

                implementation(libs.koin.core)
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.koin)
                implementation(libs.voyager.transitions)

                implementation(libs.moko.resources)
                implementation(libs.moko.resources.compose)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.compose.ui.tooling)
                implementation(libs.android.activity.compose)
                implementation(libs.ktor.client.android)
            }
        }

        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "com.pixelrabbit.oculi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pixelrabbit.oculi"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

// --- 🔧 BuildKonfig для общей версии приложения ---
buildkonfig {
    packageName = "com.pixelrabbit.oculi"

    val androidExt = extensions.getByType(BaseAppModuleExtension::class)

    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "VERSION_NAME",
            androidExt.defaultConfig.versionName
        )
    }
}
