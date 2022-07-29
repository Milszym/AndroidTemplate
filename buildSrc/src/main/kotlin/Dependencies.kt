import org.gradle.api.Action
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.accessors.runtime.addDependencyTo

// region dependency management extension functions
// These functions are copied from the Gradle DSL library as there was no other way to do it.
fun DependencyHandler.compileOnly(dependencyNotation: Any): Dependency? =
    add("compileOnly", dependencyNotation)

fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)

fun DependencyHandler.debugImplementation(dependencyNotation: Any): Dependency? =
    add("debugImplementation", dependencyNotation)

fun DependencyHandler.implementation(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "implementation", dependencyNotation, dependencyConfiguration
)

fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? =
    add("testImplementation", dependencyNotation)

fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? =
    add("androidTestImplementation", dependencyNotation)

fun DependencyHandler.androidTestUtil(dependencyNotation: Any): Dependency? =
    add("androidTestUtil", dependencyNotation)

fun DependencyHandler.api(dependencyNotation: Any): Dependency? =
    add("api", dependencyNotation)

fun DependencyHandler.api(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "api", dependencyNotation, dependencyConfiguration
)

fun DependencyHandler.kapt(dependencyNotation: Any): Dependency? =
    add("kapt", dependencyNotation)

fun DependencyHandler.kaptTest(dependencyNotation: Any): Dependency? =
    add("kaptTest", dependencyNotation)

fun DependencyHandler.kaptAndroidTest(dependencyNotation: Any): Dependency? =
    add("kaptAndroidTest", dependencyNotation)

fun DependencyHandler.testAnnotationProcessor(dependencyNotation: Any): Dependency? =
    add("testAnnotationProcessor", dependencyNotation)

@Suppress("unchecked_cast")
fun <T : ModuleDependency> T.exclude(group: String? = null, module: String? = null): T =
    exclude(excludeMapFor(group, module)) as T

internal fun excludeMapFor(group: String?, module: String?): Map<String, String> =
    mapOfNonNullValuesOf(
        "group" to group,
        "module" to module
    )

internal fun mapOfNonNullValuesOf(vararg entries: Pair<String, String?>): Map<String, String> =
    mutableMapOf<String, String>().apply {
        for ((k, v) in entries) {
            if (v != null) {
                put(k, v)
            }
        }
    }

// endregion
fun DependencyHandler.kotlin() {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${DependencyVersions.kotlin}")
}

fun DependencyHandler.kotlinCoroutines(dependencyTarget: (DependencyHandler.(Any) -> Dependency?)? = null) {
    (dependencyTarget
        ?: DependencyHandler::implementation)("org.jetbrains.kotlinx:kotlinx-coroutines-core:${DependencyVersions.kotlinCoroutines}")
}

fun DependencyHandler.kotlinCoroutinesAndroid(dependencyTarget: (DependencyHandler.(Any) -> Dependency?)? = null) {
    (dependencyTarget
        ?: DependencyHandler::implementation)("org.jetbrains.kotlinx:kotlinx-coroutines-android:${DependencyVersions.kotlinCoroutines}")
}

fun DependencyHandler.inject(
    dependencyTarget: (DependencyHandler.(Any) -> Dependency?) = DependencyHandler::implementation
) {
    dependencyTarget("javax.inject:javax.inject:1")
}

fun DependencyHandler.dagger() {
    implementation("com.google.dagger:dagger:${DependencyVersions.dagger}")
    kapt("com.google.dagger:dagger-compiler:${DependencyVersions.dagger}")
    kaptTest("com.google.dagger:dagger-compiler:${DependencyVersions.dagger}")
}

fun DependencyHandler.daggerAndroid() {
    implementation("com.google.dagger:dagger-android:${DependencyVersions.dagger}")
    implementation("com.google.dagger:dagger-android-support:${DependencyVersions.dagger}")
    kapt("com.google.dagger:dagger-android-processor:${DependencyVersions.dagger}")
    kaptAndroidTest("com.google.dagger:dagger-compiler:${DependencyVersions.dagger}")
}

fun DependencyHandler.androidAnnotation() {
    implementation("androidx.annotation:annotation:${DependencyVersions.androidAnnotation}")
}

fun DependencyHandler.androidArchCore() {
    implementation("androidx.arch.core:core-common:${DependencyVersions.androidxArch}")
    implementation("androidx.arch.core:core-runtime:${DependencyVersions.androidxArch}")
}

fun DependencyHandler.androidCore() {
    implementation("androidx.core:core-ktx:${DependencyVersions.androidxCore}")
}

fun DependencyHandler.activity() {
    implementation("androidx.activity:activity:${DependencyVersions.activity}")
    implementation("androidx.activity:activity-ktx:${DependencyVersions.activity}")
}

fun DependencyHandler.fragment() {
    implementation("androidx.fragment:fragment:${DependencyVersions.fragment}")
    implementation("androidx.fragment:fragment-ktx:${DependencyVersions.fragment}")
}

fun DependencyHandler.recyclerView() {
    implementation("androidx.recyclerview:recyclerview:${DependencyVersions.recyclerview}")
}

fun DependencyHandler.constraintLayout(dependencyTarget: (DependencyHandler.(Any) -> Dependency?) = DependencyHandler::implementation) {
    dependencyTarget("androidx.constraintlayout:constraintlayout:${DependencyVersions.constraintLayout}")
}

fun DependencyHandler.androidLifecycle() {
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${DependencyVersions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-livedata-core-ktx:${DependencyVersions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${DependencyVersions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${DependencyVersions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-service:${DependencyVersions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-process:${DependencyVersions.lifecycle}")
}

fun DependencyHandler.androidLifecycleCompiler() {
    kapt("androidx.lifecycle:lifecycle-compiler:${DependencyVersions.lifecycle}")
}

fun DependencyHandler.androidAppCompat() {
    implementation("androidx.appcompat:appcompat:${DependencyVersions.appcompat}")
    implementation("androidx.appcompat:appcompat-resources:${DependencyVersions.appcompat}")
}

fun DependencyHandler.androidNavigation(dependencyTarget: (DependencyHandler.(Any) -> Dependency?) = DependencyHandler::implementation) {
    dependencyTarget("androidx.navigation:navigation-fragment-ktx:${DependencyVersions.androidNavigation}")
    dependencyTarget("androidx.navigation:navigation-ui-ktx:${DependencyVersions.androidNavigation}")
}

fun DependencyHandler.slf4android() {
    implementation("com.github.bright:slf4android:${DependencyVersions.slf4android}")
}

fun DependencyHandler.slf4j(dependencyTarget: DependencyHandler.(Any) -> Dependency? = DependencyHandler::implementation) {
    dependencyTarget("org.slf4j:slf4j-api:${DependencyVersions.slf4j}")
}

// A simple binding that allows seeing logs produced with slf4j in unit tests. It shows only "info" level and above.
fun DependencyHandler.slf4jSimpleTestImpl(dependencyTarget: DependencyHandler.(Any) -> Dependency? = DependencyHandler::testImplementation) {
    dependencyTarget("org.slf4j:slf4j-simple:${DependencyVersions.slf4j}")
}

fun DependencyHandler.leakCanary() {
    debugImplementation("com.squareup.leakcanary:leakcanary-android:${DependencyVersions.leakCanary}")
}

fun DependencyHandler.junit(
    dependencyTarget: DependencyHandler.(Any) -> Dependency? = DependencyHandler::testImplementation
) {
    dependencyTarget("junit:junit:${DependencyVersions.junit}")
}

fun DependencyHandler.kotlinCoroutinesTest(
    dependencyTarget: DependencyHandler.(Any) -> Dependency? = DependencyHandler::testImplementation
) {
    dependencyTarget("org.jetbrains.kotlinx:kotlinx-coroutines-test:${DependencyVersions.kotlinCoroutines}")
}

fun DependencyHandler.compose() {
    implementation("androidx.compose.ui:ui:${DependencyVersions.compose}")
    implementation("androidx.compose.material:material:${DependencyVersions.compose}")
    implementation("androidx.compose.ui:ui-tooling-preview:${DependencyVersions.compose}")
}

fun DependencyHandler.composeActivity() {
    implementation("androidx.activity:activity-compose:${DependencyVersions.composeActivity}")
}

/** Unit tests dependencies */
fun DependencyHandler.ut() {
    testImplementation("junit:junit:4.13.2")
}
