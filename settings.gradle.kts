plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "amarr"

include("app")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // Versions
            version("ktor", "2.3.7")
            version("kotlin", "1.9.21")
            version("bt", "1.10")
            version("kotest", "5.8.0")

            // Libraries
            library("ktor-server-core", "io.ktor", "ktor-server-core").versionRef("ktor")
            library("ktor-server-netty", "io.ktor", "ktor-server-netty").versionRef("ktor")
            library("ktor-server-content-negotiation", "io.ktor", "ktor-server-content-negotiation").versionRef("ktor")
            library("ktor-server-call-logging", "io.ktor", "ktor-server-call-logging").versionRef("ktor")
            library("ktor-server-call-logging-jvm", "io.ktor", "ktor-server-call-logging-jvm").versionRef("ktor")
            library("ktor-server-default-headers-jvm", "io.ktor", "ktor-server-default-headers-jvm").versionRef("ktor")
            library("ktor-serialization-kotlinx-xml", "io.ktor", "ktor-serialization-kotlinx-xml").versionRef("ktor")
            library("ktor-serialization-kotlinx-json", "io.ktor", "ktor-serialization-kotlinx-json").versionRef("ktor")
            library("ktor-server-test-host-jvm", "io.ktor", "ktor-server-test-host-jvm").versionRef("ktor")
            library("ktor-client-core", "io.ktor", "ktor-client-core").versionRef("ktor")
            library("ktor-client-cio", "io.ktor", "ktor-client-cio").versionRef("ktor")
            library("ktor-client-logging", "io.ktor", "ktor-client-logging").versionRef("ktor")
            library("ktor-client-mock", "io.ktor", "ktor-client-mock").versionRef("ktor")
            library("jamule", "com.vexdev", "jamule").version("1.0.3")
            library("guava", "com.google.guava", "guava").version("33.4.0-jre")
            library("kotest-runner-junit5", "io.kotest", "kotest-runner-junit5").versionRef("kotest")
            library("kotest-assertions-core", "io.kotest", "kotest-assertions-core").versionRef("kotest")
            library("kotest-property", "io.kotest", "kotest-property").versionRef("kotest")
            library("mockk", "io.mockk", "mockk").version("1.13.8")
            library("logback", "ch.qos.logback", "logback-classic").version("1.4.14")
            library("kotlin-test-junit", "org.jetbrains.kotlin", "kotlin-test-junit").versionRef("kotlin")
            library("commons-text", "org.apache.commons", "commons-text").version("1.11.0")

            // Plugins
            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("kotlin-serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")
            plugin("jib", "com.google.cloud.tools.jib").version("3.4.0")

            // Bundles
            bundle("kotest", listOf("kotest-runner-junit5", "kotest-assertions-core", "kotest-property"))
            bundle(
                "ktor-server",
                listOf(
                    "ktor-server-core",
                    "ktor-server-netty",
                    "ktor-server-content-negotiation",
                    "ktor-server-call-logging",
                    "ktor-server-call-logging-jvm",
                    "ktor-server-default-headers-jvm",
                    "ktor-serialization-kotlinx-xml",
                    "ktor-serialization-kotlinx-json"
                )
            )
            bundle(
                "ktor-client",
                listOf(
                    "ktor-client-core",
                    "ktor-client-cio",
                    "ktor-client-logging"
                )
            )
        }
    }
}