package org.cikit.gradle.daemon

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import javax.inject.Inject

open class CreateMakefile @Inject constructor(config: DaemonExtension) : DefaultTask() {

    init {
        group = "daemon"
    }

    @get:Input
    val daemonName: String by lazy { config.daemonName }

    @get:Input
    val rcVar: String by lazy { config.rcVar }

    @get:Input
    val daemonVersion: String by lazy { config.daemonVersion }

    @get:Input
    val daemonUser: String by lazy { config.daemonUser }

    @get:Input
    val daemonGroup: String by lazy { config.daemonGroup }

    @get:Input
    val prefix: String by lazy { config.prefix }

    @get:Input
    val sysConfDir: String by lazy { config.sysConfDir }

    @get:Input
    val rcDir: String by lazy { config.rcDir }

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
    val jvmArgs: String by lazy { config.jvmArgs }

    @get:Input
    val args: String by lazy { config.args }

    @get:OutputFile
    val outputFile: File = File(project.buildDir, "Makefile")

    private object MakeVars {
        const val NAME = "\${NAME}"
        const val VERSION = "\${VERSION}"

        const val USER = "\${USER}"
        const val GROUP = "\${GROUP}"

        const val PREFIX = "\${PREFIX}"

        const val SYSCONFDIR = "\${SYSCONFDIR}"
        const val RC_DIR = "\${RC_DIR}"
        const val DATADIR = "\${DATADIR}"
        const val LOCALSTATEDIR = "\${LOCALSTATEDIR}"
        const val LOGDIR = "\${LOGDIR}"
        const val TMPDIR = "\${TMPDIR}"

        const val INSTALL_SYS = "\${INSTALL_SYS}"
        const val INSTALL_CONF = "\${INSTALL_CONF}"
        const val INSTALL_VAR = "\${INSTALL_VAR}"
    }

    private object ShVars {
        const val f = "\$\$f"
    }

    private fun quoteMeta(value: String): String {
        val sb = StringBuilder()
        value.forEach { ch ->
            when (ch) {
                '"', '\\', '$' -> sb.append('\\')
            }
            sb.append(ch)
        }
        return sb.toString();
    }

    private fun quoteDollar(value: String): String = value.replace("$", "$$")

    private fun tokenToMakeVar(value: String): String {
        return value.replace("@([^@]*)@".toRegex(), "\\\${\$1}")
    }

    private val rcScript: String = javaClass.getResourceAsStream("/freebsd-rc.sh").use {
        it.readBytes().toString(Charsets.UTF_8).split('\n').joinToString("\n") {
            "    echo \"${tokenToMakeVar(quoteDollar(quoteMeta(it)))}\"; \\"
        }
    }

    @TaskAction
    fun generateOutputFile() {
        FileOutputStream(outputFile).use { out ->
            OutputStreamWriter(out, Charsets.UTF_8).use { w ->
                w.append("""
                    |NAME=${tokenToMakeVar(daemonName)}
                    |RCVAR=${tokenToMakeVar(rcVar)}
                    |VERSION=${tokenToMakeVar(daemonVersion)}
                    |
                    |USER=${tokenToMakeVar(daemonUser)}
                    |GROUP=${tokenToMakeVar(daemonGroup)}
                    |
                    |PREFIX=${tokenToMakeVar(prefix)}
                    |SYSCONFDIR=${tokenToMakeVar(sysConfDir)}
                    |RC_DIR=${tokenToMakeVar(rcDir)}
                    |DATADIR=${tokenToMakeVar(dataDir)}
                    |PIDFILE=${tokenToMakeVar(pidFile)}
                    |LOCALSTATEDIR=${tokenToMakeVar(localStateDir)}
                    |LOGDIR=${tokenToMakeVar(logDir)}
                    |TMPDIR=${tokenToMakeVar(tmpDir)}
                    |
                    |JAVAVM=${tokenToMakeVar(jvm)}
                    |JAVAARGS=${tokenToMakeVar(jvmArgs)}
                    |ARGS=${tokenToMakeVar(args)}
                    |
                    |INSTALL_SYS=install -o root -g wheel
                    |INSTALL_CONF=install -o root -g ${MakeVars.GROUP}
                    |INSTALL_VAR=install -o ${MakeVars.USER} -g ${MakeVars.GROUP}
                    |
                    |all:
                    |  @echo "to install ${MakeVars.NAME}, run \`make install\` as root"
                    |
                    |${MakeVars.RC_DIR}/${MakeVars.NAME}: Makefile
                    |  @( \
                    |$rcScript
                    |  ) > ${MakeVars.RC_DIR}/${MakeVars.NAME}
                    |  chmod 555 ${MakeVars.RC_DIR}/${MakeVars.NAME}
                    |
                    |install: ${MakeVars.NAME}.conf.sample lib/${MakeVars.NAME}-${MakeVars.VERSION}.jar ${MakeVars.RC_DIR}/${MakeVars.NAME}
                    |  ${MakeVars.INSTALL_CONF} -m 640 ${MakeVars.NAME}.conf.sample ${MakeVars.SYSCONFDIR}/
                    |  ${MakeVars.INSTALL_SYS} -d -m 755 ${MakeVars.DATADIR}
                    |  for f in lib/*; do ${MakeVars.INSTALL_SYS} -m 444 ${ShVars.f} ${MakeVars.DATADIR}; done
                    |  ${MakeVars.INSTALL_VAR} -d -m 750 ${MakeVars.LOCALSTATEDIR}
                    |  ${MakeVars.INSTALL_VAR} -d -m 750 ${MakeVars.TMPDIR}
                    |  ${MakeVars.INSTALL_VAR} -d -m 755 ${MakeVars.LOGDIR}
                    |""".trimMargin().replace("\n  ", "\n\t"))
                Unit
            }
        }
    }
}
