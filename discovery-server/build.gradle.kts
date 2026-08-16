plugins { java; id("org.springframework.boot") version "3.5.5"; id("io.spring.dependency-management") version "1.1.7" }
group = "dev.asyncluna.rental"; version = "0.0.1-SNAPSHOT"
java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }
repositories { mavenCentral() }
dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server"); implementation("org.springframework.boot:spring-boot-starter-actuator"); testImplementation(
    "org.springframework.boot:spring-boot-starter-test"
); testImplementation("org.testcontainers:junit-jupiter:1.21.3")
}
dependencyManagement { imports { mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.0.0") } }
tasks.withType<Test> { useJUnitPlatform() }
