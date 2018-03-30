package cz.swsamuraj.gradle.godep

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.provider.Property

@CompileStatic
class GoDepExtension {

    final Property<String> importPath

    GoDepExtension(Project project) {
        importPath = project.objects.property(String)
        importPath.set('github.com/user/package')
    }
}
