package yjh.ontongsal.api.common.utils.mask

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode

object SensitiveMaskingUtil {
    private val mapper = ObjectMapper().findAndRegisterModules()

    private const val MASK = "***"

    private val SENSITIVE_KEYS = setOf(
        "password",
        "passwd",
        "pwd",
        "token",
        "email",
        "phone",
        "mobile",
        "ssn",
        "cardNumber",
        "accountNumber"
    )

    /**
     * json 마스킹 처리
     */
    fun maskFrom(json: String): String {
        return try {
            val tree = mapper.readTree(json)
            val masked = maskNode(tree)
            mapper.writeValueAsString(masked)
        } catch (e: Exception) {
            json // JSON 아닐 때 그냥 원문
        }
    }

    /**
     * Map 마스크 처리
     */
    fun maskFrom(map: Map<String, Any>): Map<String, Any> {
        return map.mapValues { (key, value) ->
            when {
                SENSITIVE_KEYS.any { it.equals(key, ignoreCase = true) } -> MASK
                else -> value
            }
        }
    }

    private fun maskNode(node: JsonNode): JsonNode {
        return when {
            node.isObject -> maskObject(node as ObjectNode)
            node.isArray -> maskArray(node as ArrayNode)
            else -> node
        }
    }

    private fun maskObject(obj: ObjectNode): ObjectNode {
        val fields = obj.fields()
        while (fields.hasNext()) {
            val (key, value) = fields.next()

            if (SENSITIVE_KEYS.any { it.equals(key, ignoreCase = true) }) {
                obj.put(key, MASK)          // ← 민감키는 value를 ***로 치환
            } else {
                obj.set<JsonNode>(key, maskNode(value))  // ← 재귀
            }
        }
        return obj
    }

    private fun maskArray(array: ArrayNode): ArrayNode {
        for (i in 0 until array.size()) {
            array.set(i, maskNode(array[i]))  // 배열도 재귀 처리
        }
        return array
    }
}
