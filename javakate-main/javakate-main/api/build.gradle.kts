plugins {
  id("java")
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

tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
}

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.9.2"))
  testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
  useJUnitPlatform()
}
