import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.PatternLayout
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.encoder.LayoutWrappingEncoder
import org.slf4j.LoggerFactory

fun configureLogging(logLevel: Int = 0) {
    val lc = LoggerFactory.getILoggerFactory() as LoggerContext
    val rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME)
    rootLogger.level = Level.INFO

    if (logLevel >= 10) lc.getLogger(TelegramClient::class.java).level = Level.DEBUG

    val layout = PatternLayout()

    layout.pattern = "%d{\"yyyy-MM-dd'T'HH:mm:ss.SSSXXX\"} %thread %level %logger{15} %msg%n"
    layout.context = lc
    layout.start()

    val encoder = LayoutWrappingEncoder<ILoggingEvent>()
    encoder.context = lc
    encoder.layout = layout
    encoder.start()

    val console = ConsoleAppender<ILoggingEvent>()
    console.context = lc
    console.name = "console"
    console.encoder = encoder
    console.isImmediateFlush = true
    console.start()

    rootLogger.detachAndStopAllAppenders()
    rootLogger.addAppender(console)
}
