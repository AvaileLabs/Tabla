plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSpring)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    api(projects.core)

    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.exposed.spring.boot4.starter)
    testImplementation(libs.spring.boot.starter.test)

    implementation(libs.kotlin.reflect)
    developmentOnly(libs.spring.boot.docker.compose)
    runtimeOnly(libs.postgresql)


    implementation(platform(libs.aws.sdk.bom))
    implementation(libs.aws.sdk.s3)
    testImplementation(libs.s3mock.testcontainers)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}