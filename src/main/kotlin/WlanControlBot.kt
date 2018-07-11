import io.vertx.core.Vertx
import io.vertx.kotlin.core.VertxOptions
import io.vertx.kotlin.core.http.HttpClientOptions
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.toList
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.util.*

private fun err(msg: String): Nothing {
    System.err.println(msg)
    System.exit(1)
    throw IllegalStateException()
}

fun main(args: Array<String>) {
    val argsMap = args
            .map { it.split('=', limit = 2) }
            .map { it.first() to it.lastOrNull() }
            .toMap()
    System.setProperty(
            io.vertx.core.logging.LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME,
            io.vertx.core.logging.SLF4JLogDelegateFactory::class.java.name)
    LoggerFactory.getLogger(io.vertx.core.logging.LoggerFactory::class.java)
    configureLogging(argsMap["--log-level"]?.toInt() ?: 0)
    val log = LoggerFactory.getLogger("main")

    val configFileName = argsMap["--config"] ?: "telegram-wlan-control.conf"
    val config = Properties().also { config ->
        FileInputStream(configFileName).use(config::load)
    }

    val token = (config.getProperty("token") ?: err("no token configured")).trim()
    val wlanChat = (config.getProperty("chatId") ?: err("no chatId configured")).trim().toInt()

    val localStateDir = File(System.getProperty("localStateDir") ?: ".")
    val updateLogFile = File(localStateDir, "update.log")

    val vx = Vertx.vertx(VertxOptions(
            blockedThreadCheckInterval = 10_000L,
            eventLoopPoolSize = 1))

    val httpClient = vx.createHttpClient(HttpClientOptions(
            ssl = true,
            maxPoolSize = 4,
            logActivity = true))

    launch(vx.dispatcher()) {
        TelegramClient(vx, httpClient, token, updateLogFile).use { bot ->
            val wlanAdmins = bot.getChatAdministrators(wlanChat).toList()
            log.info("wlan admins: ${wlanAdmins.map { it.user.firstName }}")

            var keepRunning = true
            var currentUser: User? = null
            var currentMessage: Message? = null
            var turnOn = false
            var turnOff = false

            while (keepRunning) {
                try {
                    for (update in bot.getUpdates()) {
                        val message = update.message
                        if (message?.chat?.id == wlanChat) {
                            if (message.from == null) {
                                bot.sendMessage(message.chat.id, "hello stranger", message.messageId)
                            } else {
                                val isAdmin = message.from in wlanAdmins.map { it.user }
                                when (message.text?.substringBefore("@")?.toLowerCase()) {
                                    "/start" -> {
                                        bot.sendMessage(message.chat.id, "Hi! Use /on or /off to control the WLAN")
                                    }
                                    "/on" -> {
                                        if (isAdmin) {
                                            currentUser = message.from
                                            currentMessage = message
                                            turnOn = true
                                            turnOff = false
                                        } else {
                                            bot.sendMessage(message.chat.id, "Hope dies last!", message.messageId)
                                        }
                                    }
                                    "/off" -> {
                                        currentUser = message.from
                                        currentMessage = message
                                        turnOff = true
                                        turnOn = false
                                    }
                                }
                            }
                        } else {
                            log.info("ignored update #${update.updateId}: chatId != $wlanChat")
                        }
                    }
                } catch (ex: Exception) {
                    log.warn("error while processing updates: ${ex.message}", ex)
                    delay(10_000)
                }
                if (turnOff) {
                    val rc = async(CommonPool) {
                        try {
                            spawn("/etc/wlan-control", "off").process.waitFor()
                        } catch(ex: Throwable) {
                            log.warn("error turning off WLAN: $ex")
                            1
                        }
                    }
                    if (rc.await() == 0) {
                        bot.sendMessage(wlanChat, "WLAN turned off by ${currentUser?.firstName}")
                    } else {
                        bot.sendMessage(wlanChat, "Failed to turn off WLAN. Contact admin!", currentMessage?.messageId)
                    }
                } else if (turnOn) {
                    val rc = async(CommonPool) {
                        try {
                            spawn("/etc/wlan-control", "on").process.waitFor()
                        } catch (ex: Throwable) {
                            log.warn("error turning on WLAN: $ex")
                            1
                        }
                    }
                    if (rc.await() == 0) {
                        bot.sendMessage(wlanChat, "WLAN turned on by ${currentUser?.firstName}")
                    } else {
                        bot.sendMessage(wlanChat, "Failed to turn on WLAN. Contact admin!", currentMessage?.messageId)
                    }
                }
                turnOn = false
                turnOff = false
            }
        }
    }.invokeOnCompletion {
        vx.close()
    }
}
