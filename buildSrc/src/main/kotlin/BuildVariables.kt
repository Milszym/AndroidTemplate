import kotlin.reflect.KProperty0

fun getEnv(name: String) = System.getenv(name)?.let { if (it.isBlank()) null else it }

/**
 * Base for the full application ID that gets constructed by Android Gradle plugin depending on
 * the flavor, e.g. flavor stage will add ".stage" suffix.
 */
const val APPLICATION_ID_BASE = "com.template.app"

object BuildVariables {
    val minSdkVersion get() = 22
    val targetSdkVersion get() = 31
    val compileSdkVersion get() = targetSdkVersion
    val versionCode get() = getEnv("BUILD_COUNTER")?.toIntOrNull() ?: 1
    val vcsVersion get() = getEnv("VCS_VERSION") ?: "development"
    val versionName get() = "1.0.0"
    val ndkVersion get() = "21.0.6113669"
    val prodKeystorePassword get() = getEnv("PROD_KEYSTORE_PASSWORD")
    val prodKeystoreKeyAlias get() = getEnv("PROD_KEYSTORE_KEY_ALIAS")
    val prodKeystoreKeyPassword get() = getEnv("PROD_KEYSTORE_KEY_PASSWORD")
    val stageKeystorePassword get() = getEnv("STAGE_KEYSTORE_PASSWORD")
    val stageKeystoreKeyAlias get() = getEnv("STAGE_KEYSTORE_KEY_ALIAS")
    val stageKeystoreKeyPassword get() = getEnv("STAGE_KEYSTORE_KEY_PASSWORD")

    fun toBuildConfigFields() = listOf<KProperty0<Any>>(
        BuildVariables::minSdkVersion,
        BuildVariables::targetSdkVersion,
        BuildVariables::compileSdkVersion,
        BuildVariables::vcsVersion
    ).map {
        val value = it.get()
        val (type, literal) = when (value) {
            is String -> "String" to "\"$value\""
            else -> value.javaClass.name to "$value"
        }
        BuildConfigField(it.name, type, literal)
    }
}

data class BuildConfigField(val name: String, val type: String, val value: String)
