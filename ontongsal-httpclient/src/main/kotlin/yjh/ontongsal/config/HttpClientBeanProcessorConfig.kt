package yjh.ontongsal.config

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HttpClientBeanProcessorConfig {

    @Bean
    fun httpClientBeanFactoryPostProcessor(
        // ApplicationContext(=BeanFactory) 초기화 이후에, BeanFactory 안의 BeanDefinitions 정보들을 보고 @Bean 등록이 시작된다.
        applicationContext: ApplicationContext,
    ): HttpClientBeanFactoryPostProcessor {
        return HttpClientBeanFactoryPostProcessor(applicationContext = applicationContext)
    }
}
