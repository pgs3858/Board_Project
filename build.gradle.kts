plugins {
	java
	id("org.springframework.boot") version "4.0.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-h2console")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	runtimeOnly("com.h2database:h2")
//	runtimeOnly("com.mysql:mysql-connector-j")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-thymeleaf-test")
	testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	//최신 타임리프 설치
	implementation ("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:4.0.0")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	//swagger 가능 // 보기 편하게 문서화 웹에서 기능을 따로 테스트 해볼수있다
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.2")



	implementation("org.springframework.boot:spring-boot-starter-security")   // Spring Security (인증/인가)
	implementation("org.springframework.boot:spring-boot-starter-validation") // @Valid 유효성 검사

	// JWT 토큰 라이브러리
	// jjwt-api: JWT 생성/파싱 인터페이스
	// jjwt-impl: 실제 구현체 (런타임에만 필요)
	// jjwt-jackson: JSON 직렬화/역직렬화 지원
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	//인증 인가 시큐리티
	implementation("org.springframework.boot:spring-boot-starter-security")
	testImplementation("org.springframework.boot:spring-boot-starter-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")




}

tasks.withType<Test> {
	useJUnitPlatform()
}
