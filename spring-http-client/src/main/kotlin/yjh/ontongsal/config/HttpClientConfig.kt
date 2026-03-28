package yjh.ontongsal.config

import java.io.IOException
import java.lang.reflect.InvocationHandler
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import org.aopalliance.intercept.MethodInterceptor
import org.springframework.aop.framework.ProxyFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.invoker.HttpExchangeAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import yjh.ontongsal.exception.HttpClient4xxException
import yjh.ontongsal.exception.HttpClient5xxException
import yjh.ontongsal.http.BookClient
import yjh.ontongsal.http.CarClient
import yjh.ontongsal.http.DogClient

@Configuration
class HttpClientConfig {

    // @Bean
    fun createBookClient(): BookClient {
        // BookClient HttpMethod + URI 정보 캐싱
        val metadataMap = mutableMapOf<String, Pair<String, String>>()
        for (method in BookClient::class.java.methods) {
            val getAnnotation: GetExchange? = method.getAnnotation(GetExchange::class.java)
            val httpMethod = if (getAnnotation != null) "GET" else "UNKNOWN"
            val url = getAnnotation?.value ?: "UNKNOWN"

            // 인터페이스 이름 + 메서드 이름 조합으로 키 생성
            val key = "${BookClient::class.java.name}.${method.name}"
            metadataMap[key] = httpMethod to url
        }

        // HttpServiceProxyFactory 를 활용해 RestClient Proxy 객체 생성 (JdkDynamicProxy)
        val restClient = RestClient.builder()
            .baseUrl("http://localhost:8080/api")
            .build();
        val adapter: HttpExchangeAdapter = RestClientAdapter.create(restClient)
        val httpServiceProxyFactory: HttpServiceProxyFactory = HttpServiceProxyFactory.builderFor(adapter).build()
        val originalProxyClient = httpServiceProxyFactory.createClient(BookClient::class.java)

        // 테스트를 위한 출력
        println("프록시 클래스 이름 : ${originalProxyClient::class.java.name}")
        println("JDK Proxy 여부 : ${Proxy.isProxyClass(originalProxyClient.javaClass)}")
        println("CGLIB 여부 : ${originalProxyClient.javaClass.name.contains("EnhancerByCGLIB")}")
        println("-------------------------")

        // 방법1. java reflect 라이브러리의 Proxy 사용한 Proxy 객체 한번더 감싸기(BookClient 가 인터페이스 기반이라서 가능)
        val jdkDynamicProxy = Proxy.newProxyInstance(
            BookClient::class.java.classLoader,
            arrayOf(BookClient::class.java),
            InvocationHandler { proxy, method, args ->
                try {
                    println(">>> 호출 메서드: ${method.name}")

                    // 실제 originalClient로 위임
                    val result = method.invoke(originalProxyClient, *(args ?: emptyArray()))

                    println("<<< 결과 반환: $result")
                    result
                } catch (e: java.lang.reflect.InvocationTargetException) {
                    println("!!! 예외 발생: ${e.targetException.javaClass.simpleName}")
                    throw e.targetException
                }
            }
        ) as BookClient
        println("프록시 클래스 이름 : ${jdkDynamicProxy::class.java.name}")
        println("JDK Proxy 여부 : ${Proxy.isProxyClass(jdkDynamicProxy.javaClass)}")
        println("CGLIB 여부 : ${jdkDynamicProxy.javaClass.name.contains("EnhancerByCGLIB")}")
        println("-------------------------")

        // 방법2. CGLIB 라이브러리의 Proxy 사용
        val cglibProxy = org.springframework.cglib.proxy.Proxy.newProxyInstance(
            BookClient::class.java.classLoader,
            arrayOf(BookClient::class.java),
            object : org.springframework.cglib.proxy.InvocationHandler {
                override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any? {
                    try {
                        println(">>> 호출 메서드: ${method.name}")

                        // 실제 호출을 원하면 originalClient에 위임
                        val result = method.invoke(originalProxyClient, *(args ?: emptyArray()))

                        println("<<< 결과 반환: $result")
                        return result
                    } catch (e: InvocationTargetException) {
                        println("!!! 예외 발생: ${e.targetException.javaClass.simpleName}")
                        throw e.targetException
                    }
                }
            }
        ) as BookClient
        println("프록시 클래스 이름 : ${cglibProxy::class.java.name}")
        println("JDK Proxy 여부 : ${Proxy.isProxyClass(cglibProxy.javaClass)}")
        println("CGLIB 여부 : ${cglibProxy.javaClass.name.contains("EnhancerByCGLIB")}")
        println("-------------------------")

        // ProxyFactory 를 사용해서 Proxy 로 한번더 감싸기 JdyDynamicProxy -> JdyDynamicProxy
        val proxyFactory = ProxyFactory(originalProxyClient)

        // 1. 로깅 기능 추가
        proxyFactory.addAdvice(LoggingInterceptor())

        // 2-1. 공통 예외 핸들 기능 추가
        proxyFactory.addAdvice(ExceptionHandleInterceptor(metadataMap))

        // 2-2. 공통 예외 핸들 기능 추가(람다)
        proxyFactory.addAdvice(MethodInterceptor { invocation ->
            val key = "${BookClient::class.java.name}.${invocation.method.name}"
            val (httpMethod, url) = metadataMap[key] ?: ("UNKNOWN" to "UNKNOWN")

            try {
                println("호출 : [method=$httpMethod, url=$url]")
                invocation.proceed()
            } catch (e: HttpClientErrorException) {
                println("HTTP 4xx 오류: ${e.message} [method=$httpMethod, url=$url]")
                throw HttpClient4xxException()
            } catch (e: HttpServerErrorException) {
                println("HTTP 5xx 오류: ${e.message} [method=$httpMethod, url=$url]")
                throw HttpClient5xxException()
            } catch (e: IOException) {
                println("IO 오류: ${e.message} [method=$httpMethod, url=$url]")
                throw e
            } catch (e: Exception) {
                println("기타 오류: ${e.message} [method=$httpMethod, url=$url]")
                throw e
            }
        })

        var finalProxy = proxyFactory.proxy as BookClient;
        println("프록시 클래스 이름 : ${finalProxy::class.java.name}")
        println("JDK Proxy 여부 : ${Proxy.isProxyClass(finalProxy.javaClass)}")
        println("CGLIB 여부 : ${finalProxy.javaClass.name.contains("EnhancerByCGLIB")}")
        println("-------------------------")

        return finalProxy
    }

    @Bean
    fun createCarClient(): CarClient {
        // CarClient HttpMethod + URI 정보 캐싱
        val metadataMap = mutableMapOf<String, Pair<String, String>>()
        for (method in CarClient::class.java.methods) {
            val getAnnotation: GetExchange? = method.getAnnotation(GetExchange::class.java)
            val httpMethod = if (getAnnotation != null) "GET" else "UNKNOWN"
            val url = getAnnotation?.value ?: "UNKNOWN"

            // 인터페이스 이름 + 메서드 이름 조합으로 키 생성
            val key = "${CarClient::class.java.name}.${method.name}"
            metadataMap[key] = httpMethod to url
        }

        val restClient = RestClient.builder()
            .baseUrl("http://localhost:8080/api")
            .build();
        val adapter: HttpExchangeAdapter = RestClientAdapter.create(restClient)
        val httpServiceProxyFactory: HttpServiceProxyFactory = HttpServiceProxyFactory.builderFor(adapter).build()
        val originalProxyClient = httpServiceProxyFactory.createClient(CarClient::class.java)

        // ProxyFactory 를 사용해서 LoggingInterceptor, ExceptionHandleInterceptor 추가
        val proxyFactory = ProxyFactory(originalProxyClient)
        proxyFactory.addAdvice(LoggingInterceptor())
        proxyFactory.addAdvice(ExceptionHandleInterceptor(metadataMap))
        return proxyFactory.proxy as CarClient
    }

    @Bean
    fun createDogClient(): DogClient {
        // DogClient HttpMethod + URI 정보 캐싱
        val metadataMap = mutableMapOf<String, Pair<String, String>>()
        for (method in DogClient::class.java.methods) {
            val getAnnotation: GetExchange? = method.getAnnotation(GetExchange::class.java)
            val httpMethod = if (getAnnotation != null) "GET" else "UNKNOWN"
            val url = getAnnotation?.value ?: "UNKNOWN"

            // 인터페이스 이름 + 메서드 이름 조합으로 키 생성
            val key = "${DogClient::class.java.name}.${method.name}"
            metadataMap[key] = httpMethod to url
        }

        val webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/api")
            .build();
        val adapter: HttpExchangeAdapter = WebClientAdapter.create(webClient)
        val httpServiceProxyFactory: HttpServiceProxyFactory = HttpServiceProxyFactory.builderFor(adapter).build()
        val originalProxyClient = httpServiceProxyFactory.createClient(DogClient::class.java)

        val proxyFactory = ProxyFactory(originalProxyClient)
        proxyFactory.addAdvice(LoggingInterceptor())
        proxyFactory.addAdvice(ExceptionHandleInterceptor(metadataMap))
        return proxyFactory.proxy as DogClient
    }
}
