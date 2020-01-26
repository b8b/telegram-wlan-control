import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.apache.commons.io.input.ReversedLinesFileReader
import org.slf4j.LoggerFactory
import java.io.*
import java.net.URL
import java.net.URLEncoder

class TelegramClient(private val token: String,
                     private val updateLogFile: File,
                     private val port: Int = 443,
                     private val host: String = "api.telegram.org",
                     private val pollTimeout: Int = 70) : Closeable {

    private val json = Json(configuration = JsonConfiguration.Stable.copy(strictMode = false))
    private val log = LoggerFactory.getLogger(javaClass)

    private var lastUpdate: Update? = if (!updateLogFile.exists()) null else {
        val lastLine = ReversedLinesFileReader(updateLogFile, Charsets.UTF_8).use {
            it.readLine()?.trim() ?: ""
        }
        if (lastLine.isNotEmpty()) {
            val update = json.parse(Update.serializer(), lastLine)
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

    private fun copySubTree(p: JsonParser, w: StringWriter) {
        JsonFactory().createGenerator(w).use { dest ->
            var depth = 0
            while (true) {
                when (p.currentToken) {
                    JsonToken.START_OBJECT, JsonToken.START_ARRAY -> {
                        depth++
                    }
                    JsonToken.END_OBJECT, JsonToken.END_ARRAY -> {
                        depth--
                    }
                    else -> {
                    }
                }
                if (depth < 0) break
                dest.copyCurrentEvent(p)
                p.nextToken()
                if (depth == 0) break
            }
        }
    }

    private fun readJson(resp: InputStream): Sequence<String> {
        val p = JsonFactory().createParser(resp)

        var ok = false
        var error = 0
        var description: String? = null

        parse@ while (true) {
            when (p.nextToken()) {
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
                        val nextToken = p.nextToken()
                        val w = StringWriter()
                        if (nextToken == JsonToken.START_ARRAY) {
                            p.nextToken()
                            return generateSequence {
                                if (p.currentToken() == JsonToken.END_ARRAY)
                                    return@generateSequence null
                                copySubTree(p, w)
                                val result = w.toString()
                                w.buffer.setLength(0)
                                result
                            }.constrainOnce()
                        } else {
                            copySubTree(p, w)
                            return sequenceOf(w.toString())
                        }
                    }
                }
                else -> {
                }
            }
        }

        if (!ok) throw RuntimeException("received error $error from telegram: $description")
        throw RuntimeException("no result in payload")
    }

    private fun req(uri: String): Sequence<String> {
        URL("https://$host:$port/bot$token$uri").openStream().use { resp ->
            return readJson(resp)
        }
    }

    fun getMe(): User = json.parse(User.serializer(), req("/getMe").single())

    fun getUpdates(): Sequence<Update> {
        val sb = StringBuilder("/getUpdates?timeout=").append(pollTimeout)
        lastUpdate?.let { sb.append("&offset=").append(it.updateId.inc()) }
        return req(sb.toString()).map {
            val update = json.parse(Update.serializer(), it)
            updateLog.write(it.toByteArray(Charsets.UTF_8))
            updateLog.write('\n'.toByte().toInt())
            lastUpdate = update
            update
        }
    }

    fun getChatAdministrators(chatId: Int): Sequence<ChatMember> =
        req("/getChatAdministrators?chat_id=$chatId").map {
            json.parse(ChatMember.serializer(), it)
        }

    fun sendMessage(chatId: Int, text: String, replyTo: Int? = null): Message {
        val sb = StringBuilder("/sendMessage?chat_id=")
                .append(chatId)
                .append("&text=")
                .append(encode(text))
        replyTo?.let { sb.append("&reply_to_message_id=").append(it) }
        return json.parse(Message.serializer(), req(sb.toString()).single())
    }

}

