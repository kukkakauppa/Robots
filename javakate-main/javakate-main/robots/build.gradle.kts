plugins {
    id("java")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

group = "kate"
version = "1.0"

repositories {
    mavenCentral()
}

sourceSets {
    // Configuring the main source set
    main {
        java {
            // Setting the new source directory
            setSrcDirs(listOf("src"))
        }
        resources {
            // Setting the new resource directory
            setSrcDirs(listOf("resources"))
        }
    }
    // Similarly, you can configure the test source set if needed
    test {
        java {
            setSrcDirs(listOf("test"))
        }
    }
}

dependencies {
    implementation(project(mapOf("path" to ":api")))
    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.2.0")
    testImplementation("org.assertj:assertj-swing-junit:3.17.1")
    // https://mvnrepository.com/artifact/org.mockito/mockito-junit-jupiter
    testImplementation("org.mockito:mockito-junit-jupiter:5.7.0")
}

tasks.test {
    useJUnitPlatform()
}
