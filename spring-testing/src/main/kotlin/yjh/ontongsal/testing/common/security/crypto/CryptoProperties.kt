package yjh.ontongsal.testing.common.security.crypto

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "crypto.encrypt")
data class CryptoProperties(
    val password: String,
    val salt: String,
)
