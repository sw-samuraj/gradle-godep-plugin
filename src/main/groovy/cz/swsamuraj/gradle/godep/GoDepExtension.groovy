package cz.swsamuraj.gradle.godep

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.provider.Property

@CompileStatic
class GoDepExtension {

    final Property<String> importPath
    final Property<Boolean> disableGoModule
    final Property<Boolean> depOptional
    final Property<Boolean> proprietaryVendorsOptional
    final Property<Map> proprietaryVendors

    GoDepExtension(Project project) {
        importPath = project.objects.property(String)
        importPath.set('github.com/user/package')

        disableGoModule = project.objects.property(Boolean)
        disableGoModule.set(false)

        depOptional = project.objects.property(Boolean)
        depOptional.set(false)

        proprietaryVendorsOptional = project.objects.property(Boolean)
        proprietaryVendorsOptional.set(false)

        proprietaryVendors = project.objects.property(Map)
        proprietaryVendors.set(Collections.emptyMap())
    }
}
