#!/bin/sh
# PROVIDE: @NAME@
# REQUIRE: LOGIN
# KEYWORD: shutdown
#
# Add the following lines to /etc/rc.conf.local or /etc/rc.conf
# to enable this service:
#
# @RCVAR@_enable (bool):       Set to NO by default
#                                     Set it to YES to enable @NAME@
# @RCVAR@_daemonargs (string): Set additional daemon arguments
#                                     Default is "-c -u @USER@ -o @LOGDIR@/@NAME@.out"
# @RCVAR@_javavm (string):     Set path to java
#                                     Default is "@JAVAVM@"
# @RCVAR@_javaargs (string):   Set additional jvm arguments
#                                     Default is "@JAVAARGS@"
# @RCVAR@_args (string):       Set additional command line arguments
#                                     Default is "@ARGS@"

. /etc/rc.subr

name=@RCVAR@
rcvar=@RCVAR@_enable

load_rc_config "$name"

: ${@RCVAR@_enable:="NO"}
: ${@RCVAR@_daemonargs:="-c -u @USER@ -o @LOGDIR@/@NAME@.out"}
: ${@RCVAR@_javavm:="@JAVAVM@"}
: ${@RCVAR@_javaargs:="@JAVAARGS@"}
: ${@RCVAR@_args:="@ARGS@"}

pidfile="@PIDFILE@"
command="/usr/sbin/daemon"
procname="${@RCVAR@_javavm}"
command_args="-p ${pidfile} ${@RCVAR@_daemonargs} \
  ${procname} \
  -DlocalStateDir="@LOCALSTATEDIR@" \
  -DlogDir="@LOGDIR@" \
  -Djava.io.tmpdir="@TMPDIR@" \
  ${@RCVAR@_javaargs} \
  -jar "@DATADIR@/@NAME@-@VERSION@.jar" \
  ${@RCVAR@_args}"

load_rc_config "$name"
run_rc_command "$1"
