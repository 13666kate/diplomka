package DataClass

data class MassageUsers(
    val senderId: String = "",
    val messageText: String = "",
    val timestamp: Long = 0  // Временная метка отправки
)
