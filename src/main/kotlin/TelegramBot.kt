import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.async.ByteArrayFeeder
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClient
import io.vertx.core.http.HttpMethod
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.streams.ReadStream
import io.vertx.kotlin.core.http.RequestOptions
import io.vertx.kotlin.coroutines.dispatcher
import io.vertx.kotlin.coroutines.toChannel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.channels.sendBlocking
import kotlinx.coroutines.experimental.channels.single
import org.apache.commons.io.input.ReversedLinesFileReader
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder

class TelegramBot(val vx: Vertx,
                  val httpClient: HttpClient,
                  val token: String,
                  val updateLogFile: File,
                  val port: Int = 443,
                  val host: String = "api.telegram.org",
                  val pollTimeout: Int = 70) : Closeable {

    private val log = LoggerFactory.getLogger("TelegramBot")

    private val om = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    private val updateWriter = om.writerFor(Update::class.java)

    private var lastUpdate: Update? = if (!updateLogFile.exists()) null else {
        val lastLine = ReversedLinesFileReader(updateLogFile, Charsets.UTF_8).use {
            it.readLine()?.trim() ?: ""
        }
        if (lastLine.isNotEmpty()) {
            val update = om.readValue(lastLine, Update::class.java)
            log.info("starting at offset ${update.updateId.inc()}")
            update
        } else {
            null
        }
    }

    private val updateLog = FileOutputStream(updateLogFile, true)

    private val requestOptions = RequestOptions(host, port, true)

    override fun close() {
        updateLog.close()
    }

    private fun encode(input: String): String {
        return URLEncoder.encode(input, "UTF-8")
    }

    private inline fun <reified T> readJsonResponse(vx: Vertx, readChannel: ReceiveChannel<Buffer>) = produce<T>(vx.dispatcher()) {
        try {
            val p = om.factory.createNonBlockingByteArrayParser()
            val objBuffer = JsonObjectBuffer()
            var st = 0

            var ok = false
            var error = 0
            var description: String? = null

            parse@ while (true) {
                val token = p.nextToken()
                when (token) {
                    null -> {
                        //EOF
                        break@parse
                    }
                    JsonToken.NOT_AVAILABLE -> {
                        with(p.nonBlockingInputFeeder as ByteArrayFeeder) {
                            val buffer = readChannel.receiveOrNull()
                            if (buffer == null) {
                                endOfInput()
                            } else {
                                val bytes = buffer.bytes
                                feedInput(bytes, 0, bytes.size)
                            }
                        }
                    }
                    else -> when (st) {
                        0 -> {
                            if (token == JsonToken.FIELD_NAME) when (p.currentName) {
                                "ok" -> {
                                    ok = p.nextBooleanValue()
                                }
                                "error_code" -> {
                                    error = p.nextIntValue(-1)
                                }
                                "description" -> {
                                    description = p.nextTextValue()
                                }
                                "result" -> {
                                    st = 1
                                }
                            }
                        }
                        1 -> {
                            objBuffer.processEvent(p)?.let {
                                sendBlocking(om.readValue(it.asParserOnFirstToken(), T::class.java))
                            }
                        }
                    }
                }
            }

            if (!ok) close(RuntimeException("received error $error from telegram: $description"))
        } finally {
            if (!readChannel.isClosedForReceive) readChannel.cancel()
        }
    }

    suspend fun getMe(): User {
        val resp = httpClient.request(vx, HttpMethod.GET, requestOptions.setURI("/bot$token/getMe")).awaitResponse()
        val readChannel = (resp as ReadStream<Buffer>).toChannel(vx)
        if (resp.statusCode() != 200) throw RuntimeException("invalid status code: ${resp.statusCode()}")
        return readJsonResponse<User>(vx, readChannel).single()
    }

    suspend fun getUpdates(): ReceiveChannel<Update> {
        val sb = StringBuilder("/getUpdates?timeout=").append(pollTimeout)
        lastUpdate?.let { sb.append("&offset=").append(it.updateId.inc()) }
        log.debug("GET $sb")
        val resp = httpClient.request(vx, HttpMethod.GET, requestOptions.setURI("/bot$token$sb")).awaitResponse()
        if (resp.statusCode() != 200) throw RuntimeException("invalid status code: ${resp.statusCode()}")
        val readChannel = (resp as ReadStream<Buffer>).toChannel(vx)
        val updates = readJsonResponse<Update>(vx, readChannel)
        return produce(vx.dispatcher()) {
            try {
                for (update in updates) {
                    updateLog.write(updateWriter.writeValueAsBytes(update))
                    updateLog.write('\n'.toByte().toInt())
                    lastUpdate = update
                    sendBlocking(update)
                }
            } finally {
                if (!readChannel.isClosedForReceive) readChannel.cancel()
            }
        }
    }

    suspend fun getChatAdministrators(chatId: Int): ReceiveChannel<ChatMember> {
        val resp = httpClient.request(vx, HttpMethod.GET, requestOptions
                .setURI("/bot$token/getChatAdministrators?chat_id=$chatId")).awaitResponse()
        if (resp.statusCode() != 200) throw RuntimeException("invalid status code: ${resp.statusCode()}")
        val readChannel = (resp as ReadStream<Buffer>).toChannel(vx)
        return readJsonResponse(vx, readChannel)
    }

    suspend fun sendMessage(chatId: Int, text: String, replyTo: Int? = null): Message {
        val sb = StringBuilder("/sendMessage?chat_id=")
                .append(chatId)
                .append("&text=")
                .append(encode(text))
        replyTo?.let { sb.append("&reply_to_message_id=").append(it) }
        val resp = httpClient.request(vx, HttpMethod.GET, requestOptions.setURI("/bot$token$sb")).awaitResponse()
        val readChannel = (resp as ReadStream<Buffer>).toChannel(vx)
        if (resp.statusCode() != 200) throw RuntimeException("invalid status code: ${resp.statusCode()}")
        return readJsonResponse<Message>(vx, readChannel).single()
    }
}
