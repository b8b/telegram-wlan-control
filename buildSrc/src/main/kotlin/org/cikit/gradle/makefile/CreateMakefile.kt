package org.cikit.gradle.makefile

import org.gradle.api.DefaultTask
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import javax.inject.Inject

open class CreateMakefile @Inject constructor(config: MakefileExtension) : DefaultTask() {

    init {
        group = "makefile"
        description = "Create project Makefile."
    }

    @get:Input
    val appName: String by lazy { config.name }

    @get:Input
    val appVersion: String by lazy { config.version }

    @get:Input
    val rcVar: String by lazy { config.rcVar }

    @get:Input
    val sysUser: String by lazy { config.user }

    @get:Input
    val sysGroup: String by lazy { config.group }

    @get:Input
    val prefix: String by lazy { config.prefix }

    @get:Input
    val sysConfDir: String by lazy { config.sysConfDir }

    @get:Input
    val confDir: String by lazy { config.confDir }

    @get:Input
    val rcDir: String by lazy { config.rcDir }

    @get:Input
    val systemdDir: String by lazy { config.systemdDir }

    @get:Input
    val dataDir: String by lazy { config.dataDir }

    @get:Input
    val pidFile: String by lazy { config.pidFile }

    @get:Input
    val localStateDir: String by lazy { config.localStateDir }

    @get:Input
    val logDir: String by lazy { config.logDir }

    @get:Input
    val tmpDir: String by lazy { config.tmpDir }

    @get:Input
    val jvm: String by lazy { config.jvm }

    @get:Input
    val jvmArgs: MutableList<String> by lazy { config.jvmArgs }

    @get:Input
    val args: MutableList<String> by lazy { config.args }

    @get:OutputFile
    val outputFile: File = File(project.projectDir, "Makefile")

    private fun quoteMeta(value: String): String {
        val sb = StringBuilder()
        value.forEach { ch ->
            when (ch) {
                '"', '\\', '$', '`' -> sb.append('\\')
            }
            sb.append(ch)
        }
        return sb.toString()
    }

    private fun loadResource(name: String): String = javaClass.getResourceAsStream(name).use {
        it.readBytes().toString(Charsets.UTF_8).split('\n').joinToString("\n") {
            val quoted = it.split("\$(").joinToString("$(") { quoteMeta(it) }
            "    echo \"$quoted\"; \\"
        }
    }

    private fun loadResources(relativeBuildDir: String): String = listOf("freebsd-rc.sh", "systemd-service.conf").joinToString("\n\n") {
        """
        |${quoteMeta(relativeBuildDir)}/${it}:
        |  @( \
        |${loadResource("/${it}")}
        |  ) > ${quoteMeta(relativeBuildDir)}/${it}
        """.trimMargin()
    }

    private fun dependencies() = project.configurations.findByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME)?.map { it.name } ?: emptyList<String>()

    @TaskAction
    fun generateOutputFile() {
        val relativeBuildDir = quoteMeta(project.buildDir.canonicalPath.substringAfter(
                project.projectDir.canonicalPath + "/"))
        val mainJar = "${relativeBuildDir}/libs/$(NAME)-$(VERSION).jar"
        val deps = dependencies().map { "${relativeBuildDir}/libs/$it" }
        FileOutputStream(outputFile).use { out ->
            OutputStreamWriter(out, Charsets.UTF_8).use { w ->
                w.append("""
                    |NAME=${appName}
                    |VERSION=${appVersion}
                    |
                    |RCVAR=${rcVar}
                    |
                    |USER=${sysUser}
                    |GROUP=${sysGroup}
                    |
                    |PREFIX=${prefix}
                    |SYSCONFDIR=${sysConfDir}
                    |CONFDIR=${confDir}
                    |RC_DIR=${rcDir}
                    |SYSTEMD_DIR=${systemdDir}
                    |DATADIR=${dataDir}
                    |PIDFILE=${pidFile}
                    |LOCALSTATEDIR=${localStateDir}
                    |LOGDIR=${logDir}
                    |TMPDIR=${tmpDir}
                    |
                    |JAVAVM=${jvm}
                    |JAVAARGS=${jvmArgs.joinToString(" ")}
                    |ARGS=${args.joinToString(" ")}
                    |
                    |INSTALL_SYS=install -o root -g wheel
                    |INSTALL_CONF=install -o root -g $(GROUP)
                    |INSTALL_VAR=install -o $(USER) -g $(GROUP)
                    |
                    |DEPS=${deps.joinToString(" \\\n   ")}
                    |
                    |SCRIPTS=$relativeBuildDir/freebsd-rc.sh \
                    |      $relativeBuildDir/systemd-service.conf
                    |
                    |rebuild:
                    |  ./gradlew createMakefile jar
                    |
                    |clean:
                    |  ./gradlew clean
                    |
                    |${loadResources(relativeBuildDir)}
                    |
                    |install: $(NAME).conf.sample $mainJar $(DEPS) $(SCRIPTS)
                    |  $(INSTALL_CONF) -m 640 $(NAME).conf.sample $(DESTDIR)$(CONFDIR)/
                    |  $(INSTALL_SYS) -d -m 755 $(DESTDIR)$(DATADIR)
                    |  $(INSTALL_SYS) -m 644 $mainJar $(DEPS) $(DESTDIR)$(DATADIR)/
                    |  if ! [ -d $(RC_DIR) ]; then true; else \
                    |    $(INSTALL_SYS) -m 555 $relativeBuildDir/freebsd-rc.sh $(DESTDIR)$(RC_DIR)/$(NAME); \
                    |  fi
                    |  if ! [ -d $(SYSTEMD_DIR) ]; then true; else \
                    |    $(INSTALL_SYS) -m 444 $relativeBuildDir/systemd-service.conf $(DESTDIR)$(SYSTEMD_DIR)/$(NAME).service; \
                    |  fi
                    |  $(INSTALL_VAR) -d -m 750 $(DESTDIR)$(LOCALSTATEDIR)
                    |  $(INSTALL_VAR) -d -m 750 $(TMPDIR)
                    |  $(INSTALL_VAR) -d -m 755 $(LOGDIR)
                    |
                    |.PHONY: rebuild clean install $(SCRIPTS)
                    |""".trimMargin()
                        .replace("\n  ", "\n\t")
                        .split("\$(").joinToString("$(") { it.replace("$", "$$") })
                Unit
            }
        }
    }
}
