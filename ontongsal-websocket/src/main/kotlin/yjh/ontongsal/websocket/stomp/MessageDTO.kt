package yjh.ontongsal.websocket.stomp

data class ChattingMessage(val message: String = "")

data class ChattingPubSubDTO(
    val roomId: Long,
    val message: String,
)

data class ChattingResponse(val content: String)
