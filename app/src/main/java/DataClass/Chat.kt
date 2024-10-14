package DataClass

data class Chat(
    val chatId: String = "",
    val participants: List<String> = emptyList()
)
