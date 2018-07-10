import io.vertx.core.Vertx
import io.vertx.core.http.*
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.experimental.CancellableContinuation
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.suspendCancellableCoroutine

class CoroutineHttpRequest internal constructor(val deferredResponse: Deferred<HttpClientResponse>,
                                                req: HttpClientRequest) : HttpClientRequest by req {
    suspend fun awaitResponse(): HttpClientResponse {
        end()
        return deferredResponse.await()
    }
}

fun HttpClient.request(vx: Vertx, method: HttpMethod, options: RequestOptions): CoroutineHttpRequest {
    lateinit var cont: CancellableContinuation<HttpClientResponse>
    val deferredResponse = async(vx.dispatcher()) {
        suspendCancellableCoroutine<HttpClientResponse> { cont = it }
    }
    val req = request(method, options) {
        cont.resume(it)
    }
    req.exceptionHandler {
        cont.resumeWithException(it)
    }
    return CoroutineHttpRequest(deferredResponse, req)
}
