package wemade.ontongsal.springmodulith.shared.web

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import wemade.ontongsal.springmodulith.shared.logging.LogFilter
import wemade.ontongsal.springmodulith.shared.logging.MdcFilter

@Configuration
class FilterConfig {

    @Bean
    fun responseHeaderFilter(): FilterRegistrationBean<ResponseHeaderFilter> {
        val registration = FilterRegistrationBean(ResponseHeaderFilter())
        registration.order = Ordered.HIGHEST_PRECEDENCE
        return registration
    }

    @Bean
    fun mdcFilter(): FilterRegistrationBean<MdcFilter> {
        val registration = FilterRegistrationBean(MdcFilter())
        registration.order = Ordered.HIGHEST_PRECEDENCE + 1
        return registration
    }

    @Bean
    fun logFilter(): FilterRegistrationBean<LogFilter> {
        val registration = FilterRegistrationBean(LogFilter())
        registration.order = Ordered.HIGHEST_PRECEDENCE + 2
        return registration
    }
}