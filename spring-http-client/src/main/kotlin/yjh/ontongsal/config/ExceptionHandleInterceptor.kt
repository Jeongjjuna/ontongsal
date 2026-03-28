package yjh.ontongsal.config

import java.io.IOException
import kotlin.to
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.reactive.function.client.WebClientResponseException
import yjh.ontongsal.exception.HttpClient4xxException
import yjh.ontongsal.exception.HttpClient5xxException
import yjh.ontongsal.http.BookClient

class ExceptionHandleInterceptor(
    val metadataMap: MutableMap<String, Pair<String, String>>,
) : MethodInterceptor {

    override fun invoke(invocation: MethodInvocation): Any? {
        val key = "${BookClient::class.java.name}.${invocation.method.name}"
        val (httpMethod, url) = metadataMap[key] ?: ("UNKNOWN" to "UNKNOWN")

        try {
            println("호출 : [method=$httpMethod, url=$url]")
            return invocation.proceed()
        } catch (e: HttpClientErrorException) {
            println("HTTP 4xx 오류: ${e.message} [method=$httpMethod, url=$url]")
            throw HttpClient4xxException()
        } catch (e: HttpServerErrorException) {
            println("HTTP 5xx 오류: ${e.message} [method=$httpMethod, url=$url]")
            throw HttpClient5xxException()
        } catch (e: WebClientResponseException) {
            println("WebClientResponseException 오류: ${e.message} [method=$httpMethod, url=$url]")
            throw e
        } catch (e: IOException) {
            println("IO 오류: ${e.message} [method=$httpMethod, url=$url]")
            throw e
        } catch (e: Exception) {
            println("기타 오류: ${e.message} [method=$httpMethod, url=$url]")
            throw e
        }
    }
}
