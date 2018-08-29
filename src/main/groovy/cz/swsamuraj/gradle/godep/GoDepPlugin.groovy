/*
 * Copyright (c) 2018, Vít Kotačka
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package cz.swsamuraj.gradle.godep

import groovy.transform.CompileStatic
import org.gradle.api.*

@CompileStatic
class GoDepPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def isGoModule = isGoModule(project)

        GoDepExtension extension = project.extensions.create('godep', GoDepExtension, project)

        project.tasks.create('clean', CleanTask)

        project.tasks.create('cleanVendors', CleanVendorsTask)

        PrepareWorkspaceTask prepareWorkspaceTask = project.tasks.create('prepareWorkspace', PrepareWorkspaceTask) {
            it.importPath = extension.importPath
        }

        GoDepTask depTask = project.tasks.create('dep', GoDepTask) {
            it.importPath = extension.importPath
        }

        project.tasks.create('proprietaryVendors', ProprietaryVendorsTask) {
            it.importPath = extension.importPath
            it.proprietaryVendors = extension.proprietaryVendors
        }

        project.tasks.create('test', GoTestTask) {
            it.importPath = extension.importPath
        }

        project.tasks.create('build', GoBuildTask) {
            it.importPath = extension.importPath
        }

        project.gradle.addProjectEvaluationListener(new ProjectEvaluationListener() {
            @Override
            void beforeEvaluate(Project proj) {
            }

            @Override
            void afterEvaluate(Project proj, ProjectState projectState) {
                if (proj.tasks.findByPath('dep') != null) {
                    if (extension.depOptional.get()) {
                        if (extension.proprietaryVendorsOptional.get()) {
                            proj.tasks.getByName('test').setDependsOn(taskList(prepareWorkspaceTask))
                        } else {
                            proj.tasks.getByName('proprietaryVendors').setDependsOn(taskList(prepareWorkspaceTask))
                        }
                    }
                }
                if (proj.tasks.findByPath('proprietaryVendors') != null) {
                    if (extension.proprietaryVendorsOptional.get()) {
                        if (extension.depOptional.get()) {
                            proj.tasks.getByName('test').setDependsOn(taskList(prepareWorkspaceTask))
                        } else {
                            proj.tasks.getByName('test').setDependsOn(taskList(depTask))
                        }
                    }
                }
            }
        })
    }

    boolean isGoModule(Project project) {
        project.file("go.mod").exists()
    }

    List<Task> taskList(Task task) {
        return Arrays.asList(task)
    }
}
