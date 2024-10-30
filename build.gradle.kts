@file:Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin).apply(false)
    alias(libs.plugins.library).apply(false)
    alias(libs.plugins.application).apply(false)
    alias(libs.plugins.compose.compiler) apply false
}