import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.dsl.TestOptions
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.delegateClosureOf
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import java.io.File

val JVM_TARGET = JavaVersion.VERSION_11

/**
 * Retrieves the [android][com.android.build.gradle.LibraryExtension] extension.
 */
internal val Project.android: LibraryExtension
    get() = (this as ExtensionAware).extensions.getByName("android") as LibraryExtension

/**
 * Configures the [android][com.android.build.gradle.LibraryExtension] extension.
 */
internal fun Project.android(configure: Action<LibraryExtension>): Unit =
    (this as ExtensionAware).extensions.configure("android", configure)

internal fun TestedExtension.kotlinOptions(configure: Action<org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions>): Unit =
    (this as ExtensionAware).extensions.configure("kotlinOptions", configure)

/**
 * Configures the [kapt][org.jetbrains.kotlin.gradle.plugin.KaptExtension] extension.
 */
fun Project.kapt(configure: Action<KaptExtension>): Unit =
    (this as ExtensionAware).extensions.configure("kapt", configure)

/**
 * @param requireResourcePrefix configures module name as a prefix required for all the module's Android resources
 * @param includeAnnotationProcessing includes an annotation processor (like kapt) configuration block (should be true if a module uses kotlin-kapt plugin)
 * @param includeRoomConfig configures Room schema location and enables Room's incremental annotation processing
 * (implies [includeAnnotationProcessing] = true)
 * @param includeTestOptions includes Android's testOptions configuration block
 * @param includeViewBinding enables Android's ViewBinding feature
 */
fun Project.defaultAndroidLibrary(
    requireResourcePrefix: Boolean = true,
    includeAnnotationProcessing: Boolean = false,
    includeRoomConfig: Boolean = false,
    includeTestOptions: Boolean = false
) = android {

    defaultLibraryDefaultConfig()

    if (requireResourcePrefix) {
        resourcePrefix(project.name)
    }

    defaultCompileOptions()

    defaultKotlinOptions()

    defaultLibraryBuildTypes()

    if (includeAnnotationProcessing || includeRoomConfig) {
        defaultKapt(includeRoomConfig)
    }

    if (includeRoomConfig) {
        androidTestSourceSetsWithSchemaLocation(defaultSchemaLocationPath)
    }

    if (includeTestOptions) {
        defaultTestOptions()
    }

    defaultPackagingOptions()
}

fun BaseExtension.defaultLibraryDefaultConfig() = defaultConfig {
    compileSdkVersion(BuildVariables.compileSdkVersion)
    minSdk = BuildVariables.minSdkVersion
    targetSdk = BuildVariables.targetSdkVersion
    versionCode = BuildVariables.versionCode
    versionName = BuildVariables.versionName

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

fun TestedExtension.defaultCompileOptions(javaVersion: JavaVersion = JVM_TARGET) = compileOptions {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

fun TestedExtension.defaultKotlinOptions(javaVersion: JavaVersion = JVM_TARGET) = kotlinOptions {
    jvmTarget = javaVersion.toString()
}

fun BaseExtension.defaultLibraryBuildTypes() = buildTypes {
    getByName("release") {
        isMinifyEnabled = false
        setProguardFiles(
            listOf(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        )
    }
}

val Project.defaultSchemaLocation get() = File(rootDir, "schemas")
val Project.defaultSchemaLocationPath get() = defaultSchemaLocation.absolutePath

fun Project.defaultKapt(includeRoomConfig: Boolean) = kapt {
    arguments {
        if (includeRoomConfig) {
            arg("room.schemaLocation", defaultSchemaLocationPath)
            arg("room.incremental", true) // Can be removed after upgrading to Room 2.3.0
        }
    }
}

fun BaseExtension.androidTestSourceSetsWithSchemaLocation(schemaLocation: String) = sourceSets {
    getByName("test") {
        assets.srcDir(schemaLocation)
    }
    getByName("androidTest") {
        assets.srcDir(schemaLocation)
    }
}

fun BaseExtension.defaultTestOptions() = testOptions {
    unitTests(delegateClosureOf<TestOptions.UnitTestOptions> {
        isReturnDefaultValues = true
        isIncludeAndroidResources = true
    })
}

/**
 * Enables Android Test Orchestrator
 * https://developer.android.com/training/testing/instrumented-tests/androidx-test-libraries/runner#use-android
 *
 * If you want to use it, please also add [androidTestOrchestrator] dependency in the dependencies block.
 */
fun BaseExtension.useAndroidTestOrchestratorTestOptions() = testOptions {
    execution = "ANDROIDX_TEST_ORCHESTRATOR"
}

fun BaseExtension.defaultPackagingOptions() = packagingOptions {
    // https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-debug/index.html
    resources.excludes.add("META-INF/AL2.0") // https://github.com/Kotlin/kotlinx.coroutines/issues/2023
    resources.excludes.add("META-INF/LGPL2.1")
    resources.excludes.add("META-INF/licenses/ASM")
    resources.pickFirsts.add("win32-x86-64/attach_hotspot_windows.dll")
    resources.pickFirsts.add("win32-x86/attach_hotspot_windows.dll")
}

fun Project.requiresOptInDefault() = requiresOptIn(
    "kotlin.RequiresOptIn",
    "kotlinx.coroutines.ExperimentalCoroutinesApi"
)

// https://kotlinlang.org/docs/opt-in-requirements.html#module-wide-opt-in
fun Project.requiresOptIn(vararg optInAnnotations: String) =
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs += optInAnnotations.map { "-opt-in=$it" }
    }

fun Project.defaultKotlinJvmTarget(javaVersion: JavaVersion = JVM_TARGET) =
    tasks.withType(type = org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class) {
        kotlinOptions {
            jvmTarget = javaVersion.toString()
        }
    }
