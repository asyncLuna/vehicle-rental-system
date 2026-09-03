plugins {
  java
  id("org.springframework.boot") version "3.5.5"
  id("io.spring.dependency-management") version "1.1.7"
  id("com.diffplug.spotless") version "7.0.2"
}

group = "dev.asyncluna.rental"

version = "0.0.1-SNAPSHOT"

java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

repositories { mavenCentral() }

dependencyManagement {
  imports { mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.0.0") }
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
  implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
  implementation("io.github.resilience4j:resilience4j-spring-boot3:2.3.0")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")
  runtimeOnly("org.postgresql:postgresql")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.testcontainers:junit-jupiter:1.21.3")
  testImplementation("org.testcontainers:postgresql:1.21.3")
}

tasks.withType<Test> { useJUnitPlatform() }

tasks.withType<Test> { environment("DOCKER_API_VERSION", "1.40") }

spotless {
  java {
    target("src/**/*.java")
    palantirJavaFormat()
    trimTrailingWhitespace()
    endWithNewline()
  }
  kotlinGradle {
    target("*.gradle.kts")
    ktfmt()
    trimTrailingWhitespace()
    endWithNewline()
  }
}
