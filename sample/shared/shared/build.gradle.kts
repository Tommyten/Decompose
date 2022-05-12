import com.arkivanov.gradle.Target

plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
    id("kotlin-parcelize")
    id("com.arkivanov.gradle.setup")
}

setupMultiplatform {
    targets(
        Target.Android,
        Target.Jvm,
        Target.Js(mode = Target.Js.Mode.IR),
        Target.Ios(
            arm64 = false, // Uncomment to enable arm64 target
        ),
    )
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":decompose"))
                implementation(project(":sample:shared:dynamic-features:api"))
                implementation(deps.reaktive.reaktive)
            }
        }

        named("androidMain") {
            dependencies {
                implementation(project(":extensions-android"))
                implementation(deps.android.material.material)
                implementation(deps.android.play.core)
            }
        }

        named("nonAndroidMain") {
            dependencies {
                implementation(project(":sample:shared:dynamic-features:feature1Impl"))
                implementation(project(":sample:shared:dynamic-features:feature2Impl"))
            }
        }

        named("jsMain") {
            dependencies {
                implementation(project.dependencies.enforcedPlatform(deps.jetbrains.kotlinWrappers.kotlinWrappersBom.get()))
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-styled")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-mui")
            }
        }
    }
}
