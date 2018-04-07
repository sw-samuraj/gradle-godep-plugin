package cz.swsamuraj.gradle.godep

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.provider.Property

@CompileStatic
class GoDepExtension {

    final Property<String> importPath
    final Property<Boolean> depOptional

    GoDepExtension(Project project) {
        importPath = project.objects.property(String)
        importPath.set('github.com/user/package')

        depOptional = project.objects.property(Boolean)
        depOptional.set(false)
    }
}
