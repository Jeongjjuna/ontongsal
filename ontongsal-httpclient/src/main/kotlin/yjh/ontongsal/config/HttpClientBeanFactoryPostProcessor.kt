package yjh.ontongsal.config

import org.springframework.aop.framework.ProxyFactory
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.invoker.HttpServiceProxyFactory


class HttpClientBeanFactoryPostProcessor(
    private val applicationContext: ApplicationContext,
) : BeanFactoryPostProcessor {

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        val scanner = object : ClassPathScanningCandidateComponentProvider(false, applicationContext.environment) {
            override fun isCandidateComponent(beanDefinition: AnnotatedBeanDefinition): Boolean {
                return beanDefinition.metadata.isInterface && beanDefinition.metadata.hasAnnotation(HttpClient::class.java.name)
            }
        }
        // HttpClient 어노테이션이 적용된 대상 목록들에 대해, isCandidateComponent 메서드를 거쳐서 스캔된다.
        scanner.addIncludeFilter(AnnotationTypeFilter(HttpClient::class.java))

        val basePackage = "yjh.ontongsal"
        val candidates: Set<BeanDefinition> = scanner.findCandidateComponents(basePackage)

        candidates.forEach { bd ->
            val clazz = Class.forName(bd.beanClassName!!) // class.name = yjh.ontongsal.http.BookClient
            val annotation = clazz.getAnnotation(HttpClient::class.java)
            val url: String = annotation.url            // http://localhost:8080/api
            val type: HttpClientType = annotation.type  // REST_CLIENT

            println("Scanning HttpClient interface: ${clazz.name}, url=$url, type=$type")

            // 동적 프록시 생성
            val httpClientProxy = when (type) {

                HttpClientType.REST_CLIENT -> {
                    val restClient = RestClient.builder()
                        .baseUrl(url)
                        .build()

                    val adapter = RestClientAdapter.create(restClient)
                    val factory = HttpServiceProxyFactory
                        .builderFor(adapter)
                        .build()

                    factory.createClient(clazz)
                }

                HttpClientType.WEB_CLIENT -> {
                    val webClient = WebClient.builder()
                        .baseUrl(url)
                        .build()

                    val adapter = WebClientAdapter.create(webClient)
                    val factory = HttpServiceProxyFactory
                        .builderFor(adapter)
                        .build()

                    factory.createClient(clazz)
                }
            }

            // DogClient HttpMethod + URI 정보 캐싱
            val metadataMap = mutableMapOf<String, Pair<String, String>>()
            for (method in clazz.methods) {
                val getAnnotation: GetExchange? = method.getAnnotation(GetExchange::class.java)
                val httpMethod = if (getAnnotation != null) "GET" else "UNKNOWN"
                val url = getAnnotation?.value ?: "UNKNOWN"

                // 인터페이스 이름 + 메서드 이름 조합으로 키 생성
                val key = "${clazz.name}.${method.name}"
                metadataMap[key] = httpMethod to url
            }

            val proxyFactory = ProxyFactory(httpClientProxy)
            proxyFactory.isProxyTargetClass = true
            proxyFactory.addAdvice(LoggingInterceptor())
            proxyFactory.addAdvice(ExceptionHandleInterceptor(metadataMap))

            // BeanDefinition 등록 -> 타입을 추론할 수 없음.
            // return proxyFactory.proxy as DogClient
            beanFactory.registerSingleton(clazz.simpleName, proxyFactory.proxy)

        }
    }
}
