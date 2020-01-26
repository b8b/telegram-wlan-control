import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.3.61"

    kotlin("jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion
    id("org.jetbrains.dokka") version "0.9.17"
    `maven-publish`
    id("org.cikit.makefile")
}

group = "org.cikig"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
}

dependencies {
    val jacksonVersion = "2.10.2"
    val logbackVersion = "1.2.3"

    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")
    
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("commons-io:commons-io:2.6")

    testCompile("junit:junit:4.12")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val main by sourceSets

val sourcesJar by tasks.creating(Jar::class) {
    group = "build"
    classifier = "sources"
    from(main.allSource)
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    classifier = "javadoc"
    from(tasks["dokka"])
}

val jar = tasks.named<Jar>("jar") {
//    dependsOn("generatePomFileForMavenJavaPublication")
    into("META-INF/maven/${project.group}/${project.name}") {
        from(File(buildDir, "publications/mavenJava"))
        rename(".*", "pom.xml")
    }
    manifest.attributes.apply {
        val classpath = configurations[JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME]
                .resolvedConfiguration
                .resolvedArtifacts
        put("Main-Class", "WlanControlBotKt")
        put("Class-Path", project.configurations
                .findByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME)
                ?.joinToString(" ") { it.name })
    }
}.get()

makefile {
    user = "root"
    group = "wheel"

    jvmArgs.add("-Dvertx.cacheDirBase=$(LOCALSTATEDIR)/vertx")
}
