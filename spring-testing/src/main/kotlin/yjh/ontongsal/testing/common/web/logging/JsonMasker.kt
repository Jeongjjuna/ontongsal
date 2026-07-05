package wemade.ontongsal.springmodulith.shared.logging

import tools.jackson.databind.JsonNode
import tools.jackson.databind.json.JsonMapper
import tools.jackson.databind.node.ArrayNode
import tools.jackson.databind.node.ObjectNode

object JsonMasker {
    private val mapper = JsonMapper()

    private const val MASK = "***"

    private val SENSITIVE_KEYS = setOf(
        // body fields
        "password",
        "passwd",
        "pwd",
        "token",
        "email",
        "phone",
        "mobile",
        "ssn",
        "cardnumber",
        "accountnumber",
        // HTTP headers
        "authorization",
        "cookie",
        "set-cookie",
        "proxy-authorization",
    )

    private fun isSensitive(key: String): Boolean = key.lowercase() in SENSITIVE_KEYS

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
            if (isSensitive(key)) MASK else value
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
        val result = obj.objectNode()

        obj.forEachEntry { key, value ->
            if (isSensitive(key)) {
                result.put(key, MASK)
            } else {
                result.set(key, maskNode(value))
            }
        }

        return result
    }

    private fun maskArray(array: ArrayNode): ArrayNode {
        for (i in 0 until array.size()) {
            array.set(i, maskNode(array[i]))  // 배열도 재귀 처리
        }
        return array
    }
}
