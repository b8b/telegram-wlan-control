package org.cikit.gradle.daemon

import org.gradle.api.Project

open class DaemonExtension(project: Project) {

    var daemonName: String = project.name
    var rcVar: String = project.name.replace("[^a-zA-Z0-9]".toRegex(), "_")
    var daemonVersion: String = project.version.toString()

    var daemonUser = "nobody"
    var daemonGroup = "nogroup"

    var prefix = "/usr/local"

    var sysConfDir = "@PREFIX@/etc"
    var rcDir = "@SYSCONFDIR@/rc.d"
    var dataDir = "@PREFIX@/share/@NAME@"
    var pidFile = "/var/run/@NAME@.pid"
    var localStateDir = "/var/db/@NAME@"
    var logDir = "/var/log/@NAME@"
    var tmpDir = "/vra/tmp/@NAME@"

    val jvm = "@PREFIX@/openjdk8/bin/java"
    var jvmArgs: String = "-XX:CICompilerCount=2 -XX:+UseSerialGC -Xmx20m"
    var args: String = "-config=@SYSCONFDIR@/@NAME@.conf"

}
