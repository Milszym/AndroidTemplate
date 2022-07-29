import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

fun DependencyHandler.projectModules(
    modules: List<Module>,
    dependencyTarget: DependencyHandler.(Any) -> Dependency? = DependencyHandler::implementation
) {
    modules.forEach {
        dependencyTarget(project(it.moduleName))
    }
}

fun DependencyHandler.projectModule(
    module: Module,
    dependencyTarget: DependencyHandler.(Any) -> Dependency? = DependencyHandler::implementation
) {
    dependencyTarget(project(module.moduleName))
}