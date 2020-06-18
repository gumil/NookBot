import java.net.URI
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.72"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.3.72"
    id("io.kotless") version "0.1.5"
}

group = "dev.gumil.nookbot"
version = "0.0.1"

repositories {
    mavenCentral()
    jcenter()
    maven {
        url = URI("http://dynamodb-local.s3-website-us-west-2.amazonaws.com/release")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
    implementation("io.ktor:ktor-client-cio:1.3.2")
    implementation("io.ktor:ktor-client-serialization-jvm:1.3.2")
    implementation("io.ktor:ktor-client-logging-jvm:1.3.2")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("com.amazonaws:aws-java-sdk-dynamodb:1.11.804")
    implementation("io.kotless:lang:0.1.5")

    testImplementation("io.ktor:ktor-client-mock-jvm:1.3.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")

    testImplementation("com.amazonaws:DynamoDBLocal:1.12.0")
}

val copyNativeDeps by tasks.creating(Copy::class) {
    val configuration = project.configurations.getByName("testImplementation")
    configuration.isCanBeResolved = true

    from(configuration) {
        include("*.dylib")
        include("*.so")
        include("*.dll")
    }
    into("build/libs")
}

val testTask = tasks.getByName<Test>("test") {
    doFirst {
        this@getByName.systemProperty("java.library.path", "build/libs")
    }
    dependsOn("copyNativeDeps")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-XXLanguage:+InlineClasses"
        )
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
