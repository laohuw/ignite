plugins {
    id("java")
    id("application")
}

group = "com.algo.ingite"
version = "1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}
var ignite_version="2.16.0"
dependencies {
    implementation("org.apache.ignite:ignite-core:$ignite_version")
    implementation("org.apache.ignite:ignite-spring:$ignite_version")
    implementation("org.apache.ignite:ignite-indexing:$ignite_version")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
