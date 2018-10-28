import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.commons.io.input.ReversedLinesFileReader
import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder

class TelegramClient(private val client: HttpHandler,
                     private val token: String,
                     private val updateLogFile: File,
                     private val port: Int = 443,
                     private val host: String = "api.telegram.org",
                     private val pollTimeout: Int = 70) : Closeable {

    private val log = LoggerFactory.getLogger(javaClass)

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

    override fun close() {
        updateLog.close()
    }

    private fun encode(input: String): String {
        return URLEncoder.encode(input, "UTF-8")
    }

    private fun readJson(p: JsonParser) {
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
                JsonToken.FIELD_NAME -> when (p.currentName) {
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
                        return
                    }
                }
                else -> {
                }
            }
        }

        if (!ok) throw RuntimeException("received error $error from telegram: $description")
        throw java.lang.RuntimeException("no result in payload")
    }

    private inline fun <reified T> req(uri: String): T {
        val resp = client(Request(Method.GET, "https://$host:$port/bot$token$uri"))
        if (!resp.status.successful) throw RuntimeException("request failed: ${resp.status}")
        val p = om.factory.createParser(resp.body.stream)
        readJson(p)
        p.nextToken()
        return om.readValue(p, T::class.java)
    }

    private inline fun <reified T> reqSequence(uri: String): Sequence<T> {
        val resp = client(Request(Method.GET, "https://$host:$port/bot$token$uri"))
        if (!resp.status.successful) throw RuntimeException("request failed: ${resp.status}")
        val p = om.factory.createParser(resp.body.stream)
        readJson(p)
        return if (p.nextToken() == JsonToken.START_ARRAY) {
            om.readValues<T>(p, T::class.java).asSequence()
        } else {
            sequenceOf(om.readValue(p, T::class.java))
        }
    }

    fun getMe(): User = req("/getMe")

    fun getUpdates(): Sequence<Update> {
        val sb = StringBuilder("/getUpdates?timeout=").append(pollTimeout)
        lastUpdate?.let { sb.append("&offset=").append(it.updateId.inc()) }
        return reqSequence<Update>(sb.toString()).map { update ->
            updateLog.write(updateWriter.writeValueAsBytes(update))
            updateLog.write('\n'.toByte().toInt())
            lastUpdate = update
            update
        }
    }

    fun getChatAdministrators(chatId: Int): Sequence<ChatMember> =
        reqSequence("/getChatAdministrators?chat_id=$chatId")

    fun sendMessage(chatId: Int, text: String, replyTo: Int? = null): Message {
        val sb = StringBuilder("/sendMessage?chat_id=")
                .append(chatId)
                .append("&text=")
                .append(encode(text))
        replyTo?.let { sb.append("&reply_to_message_id=").append(it) }
        return req(sb.toString())
    }

}

