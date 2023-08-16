import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.util.*

plugins {
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.spring") version "1.9.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.5.1"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    id("com.github.ben-manes.versions") version "0.47.0"
    id("org.sonarqube") version "4.3.0.3225"
    id("org.owasp.dependencycheck") version "8.3.1"
    id("org.cyclonedx.bom") version "1.7.4"
    id("org.graalvm.buildtools.native") version "0.9.24"
    jacoco
}

group = "com.ryzingtitan"
version = "3.0.0"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.3")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test:")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("org.junit.platform:junit-platform-suite-api:1.10.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    testImplementation("io.cucumber:cucumber-java:7.13.0")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.13.0")
    testImplementation("io.cucumber:cucumber-spring:7.13.0")
    testImplementation("io.projectreactor:reactor-test:3.5.8")
    testImplementation("no.nav.security:mock-oauth2-server:1.0.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.getByName("addKtlintFormatGitPreCommitHook") {
    dependsOn("processResources")
    dependsOn("processTestResources")
    dependsOn("ktlintMainSourceSetCheck")
    dependsOn("ktlintTestSourceSetCheck")
    dependsOn("ktlintKotlinScriptCheck")
    dependsOn("runKtlintFormatOverMainSourceSet")
    dependsOn("runKtlintFormatOverTestSourceSet")
    dependsOn("ktlintKotlinScriptFormat")
    dependsOn("ktlintMainSourceSetFormat")
    dependsOn("ktlintTestSourceSetFormat")
    dependsOn("collectReachabilityMetadata")
    dependsOn("detekt")
}

tasks.getByName("compileKotlin") {
    dependsOn("addKtlintFormatGitPreCommitHook")
}

ktlint {
    version.set("0.50.0")
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    reporters {
        reporter(ReporterType.JSON)
    }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "17"

    reports {
        html.required.set(true)
        html.outputLocation.set(file("${rootProject.rootDir}/${rootProject.name}/detektHtmlReport/detekt.html"))
    }
}

jacoco {
    toolVersion = "0.8.10"
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.outputLocation.set(file("${rootProject.rootDir}/${rootProject.name}/jacocoHtmlReport"))
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.90".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn("jacocoTestCoverageVerification")
}

tasks.withType<DependencyUpdatesTask> {
    checkForGradleUpdate = true
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"

    resolutionStrategy {
        componentSelection {
            all {
                if (candidate.version.isNonStable() && !currentVersion.isNonStable()) {
                    reject("Release candidate")
                }
            }
        }
    }
}

fun String.isNonStable(): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { uppercase(Locale.getDefault()).contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(this)
    return isStable.not()
}

sonarqube {
    properties {
        property("sonar.projectKey", "ryzingTitan_datalog-api")
        property("sonar.organization", "ryzingtitan")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
