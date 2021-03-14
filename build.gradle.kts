import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.4.31"
  kotlin("kapt") version "1.4.31"
  application
}

group = "com.tagtech"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
  jcenter()
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  api("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.3.5")
  // do not remove this dependency; for some 3rd party JAVA based implementation lib this is required
  api("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8", "1.3.5")

  api("ch.qos.logback", "logback-classic", "1.2.3")

  "0.11.0".let {
    api("io.arrow-kt", "arrow-core", it)
    api("io.arrow-kt", "arrow-syntax", it)
    kapt("io.arrow-kt", "arrow-meta", it)
  }

  testImplementation(kotlin("test-junit5"))
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}

tasks {
  compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
  }
  compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
  }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
  freeCompilerArgs = listOf("-Xinline-classes")
}


application {
  mainClassName = "MainKt"
}
