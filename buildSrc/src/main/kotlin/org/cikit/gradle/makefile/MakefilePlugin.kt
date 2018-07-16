package org.cikit.gradle.makefile

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.Sync

class MakefilePlugin : Plugin<Project> {

    override fun apply(project: Project?) {
        project!!
        val ex = project.extensions.create("makefile", MakefileExtension::class.java, project)

        val jar = project.getTasks().getAt(JavaPlugin.JAR_TASK_NAME);

        val copyDependenciesTask = project.tasks.create("copyDependencies", Sync::class.java)
        copyDependenciesTask.description = "Copy dependencies into build folder."
        copyDependenciesTask.group = "makefile"
        copyDependenciesTask.from(project.configurations.getByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME))
        copyDependenciesTask.into(jar.outputs.files.first().parentFile)

        val createMakefile = project.tasks.create("createMakefile", CreateMakefile::class.java, ex)
        createMakefile.dependsOn.add(copyDependenciesTask)
    }

}
