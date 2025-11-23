package yjh.ontongsal.exception

class HttpClient4xxException(
    message: String = "receive http status 4xx response",
) : RuntimeException(message)

class HttpClient5xxException(
    message: String = "receive http status 5xx response",
) : RuntimeException(message)
