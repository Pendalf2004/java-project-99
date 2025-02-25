import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent


plugins {
	java
	application
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"

	jacoco
	id ("com.adarshr.test-logger") version "3.0.0"

	id("io.freefair.lombok") version "8.10.2"



}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

application {
	mainClass.set("hexlet/code/app/AppApplication")
}

repositories {
	mavenCentral()
}

jacoco {
    toolVersion = "0.8.12"
}


dependencies {



	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security:3.4.1")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("org.mapstruct:mapstruct:1.6.3")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

	implementation ("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("com.h2database:h2:2.2.220")
	implementation("jakarta.jms:jakarta.jms-api:3.1.0")
	implementation("org.postgresql:postgresql:42.2.5")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.assertj:assertj-core:3.25.3")
	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.7")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	testImplementation(platform("org.junit:junit-bom:5.11.0-M1"))
}

sourceSets {
	test {
		java {
			setSrcDirs(listOf("src/test/java/hexlet/code/app"))
		}
	}
}


tasks.test {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
	testLogging {
		exceptionFormat = TestExceptionFormat.FULL
		events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
		showStackTraces = true
		showCauses = true
		showStandardStreams = true
	}
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
		reports {
			xml.required = true
	}
}
