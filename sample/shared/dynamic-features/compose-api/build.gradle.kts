import com.arkivanov.gradle.Target

plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("com.arkivanov.gradle.setup")
}

setupMultiplatform {
    targets(
        Target.Android,
        Target.Jvm,
    )
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":sample:shared:dynamic-features:api"))
                implementation(compose.runtime)
                implementation(compose.ui)
            }
        }
    }
}
