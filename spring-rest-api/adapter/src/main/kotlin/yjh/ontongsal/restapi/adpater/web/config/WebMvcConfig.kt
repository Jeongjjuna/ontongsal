package yjh.ontongsal.restapi.adpater.web.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import yjh.ontongsal.restapi.adpater.web.resolver.ActorArgumentResolver

@Configuration
class WebMvcConfig(
    private val actorArgumentResolver: ActorArgumentResolver,
) : WebMvcConfigurer {

    override fun addArgumentResolvers(
        resolvers: MutableList<HandlerMethodArgumentResolver>,
    ) {
        resolvers.add(actorArgumentResolver)
    }
}
