package org.cikit.gradle.daemon

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.distribution.DistributionContainer

class DaemonPlugin : Plugin<Project> {

    override fun apply(project: Project?) {
        project!!
        val ex = project.extensions.create("daemon", DaemonExtension::class.java, project)
        val createMakefile = project.tasks.create("createMakefile", CreateMakefile::class.java, ex)

        project.extensions.findByType(DistributionContainer::class.java)?.let { distEx ->
            val main = distEx.findByName("main")
            if (main != null) {
                main.contents {
                    it.from(createMakefile)
                }
            }
        }
    }

}
