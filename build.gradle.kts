plugins {
    // Эти алиасы теперь строго соответствуют секции [plugins] в TOML
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.realm.kotlin) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}