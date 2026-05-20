package yjh.ontongsal.testing.common.web.security.crypto.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import yjh.ontongsal.testing.common.web.security.crypto.CryptoService

@Converter
class EncryptConverter(
    private val cryptoService: CryptoService
) : AttributeConverter<String, String> {

    override fun convertToDatabaseColumn(attribute: String?): String? {
        return attribute?.let { cryptoService.encrypt(it) }
    }

    override fun convertToEntityAttribute(dbData: String?): String? {
        return dbData?.let { cryptoService.decrypt(it) }
    }
}
