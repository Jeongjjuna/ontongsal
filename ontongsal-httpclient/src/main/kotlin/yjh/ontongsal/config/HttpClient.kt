package yjh.ontongsal.config

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class HttpClient(
    val url: String,
    val type: HttpClientType,
)
