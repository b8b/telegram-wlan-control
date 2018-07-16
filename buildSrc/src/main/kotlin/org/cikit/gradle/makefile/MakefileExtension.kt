package org.cikit.gradle.makefile

import org.gradle.api.Project

open class MakefileExtension(project: Project) {

    var name: String = project.name
    var version: String = project.version.toString()

    var rcVar: String = project.name.replace("[^a-zA-Z0-9]".toRegex(), "_")

    var user = "nobody"
    var group = "nogroup"

    var prefix: String

    var sysConfDir: String
    var confDir = "$(SYSCONFDIR)"
    var rcDir = "$(SYSCONFDIR)/rc.d"
    var systemdDir = "$(SYSCONFDIR)/systemd/system"
    var dataDir: String
    var pidFile = "/var/run/$(NAME).pid"
    var localStateDir: String
    var logDir = "/var/log/$(NAME)"
    var tmpDir = "/var/tmp/$(NAME)"

    val jvm = "$(PREFIX)/openjdk8/bin/java"
    var jvmArgs: MutableList<String> = mutableListOf("-XX:CICompilerCount=2", "-XX:+UseSerialGC", "-Xmx20m")
    var args: MutableList<String> = mutableListOf("-config=$(SYSCONFDIR)/$(NAME).conf")

    init {
        when (System.getProperty("os.name")) {
            "FreeBSD" -> {
                prefix = "/usr/local"
                sysConfDir = "$(PREFIX)/etc"
                dataDir = "$(PREFIX)/share/$(NAME)"
                localStateDir = "/var/db/$(NAME)"
            }
            else -> {
                prefix = "/opt/$(NAME)"
                sysConfDir = "/etc"
                dataDir = "$(PREFIX)"
                localStateDir = "/var/lib/$(NAME)"

            }
        }
    }
}
