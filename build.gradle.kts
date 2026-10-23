subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    dependencies {
        "testImplementation"("junit:junit:4.13.2")
    }

    tasks.withType<JavaCompile> {
        options.release = 17
    }

    tasks.withType<Test> {
        useJUnit()
    }
}
