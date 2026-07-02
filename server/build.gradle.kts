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

    implementation(platform(libs.ktor.bom))
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    testImplementation(libs.ktor.serverTestHost)

    testImplementation(platform(libs.kotlin.bom))
    testImplementation(libs.kotlin.testJunit)
}