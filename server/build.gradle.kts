plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
}

group = "com.availelabs.tabla"
version = "1.0.0"
application {
    mainClass = "com.availelabs.tabla.ApplicationKt"
}

dependencies {
    api(projects.core)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}