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
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec

@CompileStatic
class GoDepTask extends DefaultTask {

    final Property<String> importPath = project.objects.property(String)

    GoDepTask() {
        group = 'go & dep'
        description = 'Builds the Go project.'
        dependsOn "prepareWorkspace"
    }

    @TaskAction
    void goDep() {
        File gopkgToml = new File(project.projectDir, "Gopkg.toml")
        String depCommand

        if (!gopkgToml.exists()) {
            depCommand = 'init'
        } else {
            depCommand = 'ensure'
        }

        File packageDir = new File(project.buildDir, "go/src/${importPath.get()}")

        logger.info("[godep] dep ${depCommand}")

        project.exec(new Action<ExecSpec>() {
            @Override
            void execute(ExecSpec execSpec) {
                execSpec.environment('GOPATH', "${project.buildDir}/go")
                execSpec.commandLine('/bin/sh', '-c', "cd ${packageDir} && dep ${depCommand}")
            }
        })
    }
}
