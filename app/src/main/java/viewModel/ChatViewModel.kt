package viewModel


import DataClass.Message
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diplom1.ui.theme.Orange
import com.example.diplom1.ui.theme.colorOlivical
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChatViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var messages = MutableLiveData<List<Message>>()
    val emailStateCardUser = mutableStateOf("")
    val saveID = mutableStateOf("")
    val auchID = mutableStateOf("")
    var textLabelColorClick: MutableState<Color> =
        mutableStateOf(Orange)//устанавливаем цвет при нажатии на ввод
    var textLabelColor: MutableState<Color> =
        mutableStateOf(colorOlivical)//устанавливаем цвет при нажатии на ввод
    // private val chatsRef = db.child(FirebaseString.chats)
    val database = Firebase.database

    fun navigateToChatWithFriend(friendId: String) {
        viewModelScope.launch {
            try {
                val chatId = getOrCreateChatId(friendId)
                Log.d("MyApp", "ChatId получен успешно: $chatId")
            } catch (e: Exception) {
                Log.e("MyApp", "Ошибка при получении или создании ChatId: ${e.message}", e)
            }
        }
    }

    suspend fun getOrCreateChatId(friendId: String): String {
        val currentUserId = auchID.value
        if (currentUserId.isNullOrEmpty() || friendId.isNullOrEmpty()) {
            throw IllegalArgumentException("User ID и Friend ID не могут быть пустыми")
        }

        val chats = db.collection("chats")

        // Поиск чата, где участниками являются текущий пользователь и друг
        val query = chats
            .whereEqualTo("participants.$currentUserId", true)
            .whereEqualTo("participants.$friendId", true)
            .get()
            .await()

        return if (query.documents.isNotEmpty()) {
            val chatId = query.documents[0].id
            Log.d("MyApp", "Найден существующий чат для пользователей $currentUserId и $friendId")
            chatId
        } else {
            val chatData = hashMapOf(
                "participants" to hashMapOf(
                    currentUserId to true,
                    friendId to true
                )
            )
            val newChat = chats.add(chatData).await()
            val newChatId = newChat.id
            Log.d("MyApp", "Создан новый чат для пользователей $currentUserId и $friendId")
            newChatId
        }
    }

    fun sendMessage(chatId: String, message: Message) {
        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(message)
    }

    fun loadMessages(chatId: String) {
        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val msgs = snapshot.documents.mapNotNull { it.toObject(Message::class.java) }
                    messages.postValue(msgs)
                }
            }
    }
}


  /*  fun createUniqueChatId(): String {
        return chatsRef.push().key
            ?: throw IllegalStateException("Couldn't generate unique key")
    }

    fun sendMessage(chatId: String, message: MassageUsers) {
        val messageRef = chatsRef.child(chatId).child("messages").push()
        messageRef.setValue(message)
            .addOnSuccessListener {
                Log.d("ChatViewModel", "Message sent successfully")
            }
            .addOnFailureListener { exception ->
                Log.e("ChatViewModel", "Error sending message", exception)
            }
    }

*//*    fun createChat(participants: List<String>): String {
        val chatId = createUniqueChatId()
        val participantsMap = participants.associateWith { true }
        val chatData = mapOf(FirebaseString.parentCollection to participantsMap)
        try {
            chatsRef.child(chatId).setValue(chatData)
            Log.d("ChatViewModel", "Chat created successfully with ID: $chatId")
        } catch (e: Exception) {
            Log.e("ChatViewModel", "Error creating chat", e)
        }
        return chatId
    }*//*

    // Проверка существующего чата между пользователями
    suspend fun getChatIdForUsers(auchUser: String, freindId: String): String? {
        return try {
            // Получаем список чатов, в которых участвует пользователь с идентификатором userId1
            val chatQuery = db.child(FirebaseString.chats)
                .orderByChild("${FirebaseString.chats}/$auchUser")
                .equalTo(true)
                .get()
                .await()
            // Итерируем по всем чатам, возвращенным запросом
            for (snapshot in chatQuery.children) {
                // Получаем список участников чата
                val participants = snapshot.child(FirebaseString.chats).value as? Map<String, Boolean>
                // Проверяем, есть ли пользователь с идентификатором userId2 среди участников чата
                if (participants?.containsKey(freindId) == true) {
                    Log.d("ChatViewModel", "Existing chat found with ID: ${snapshot.key}")
                    return snapshot.key
                }
            }
            null
        } catch (e: Exception) {
            Log.e("ChatViewModel", "Error checking for existing chat", e)
            null
        }
    }

    fun getChatIdAndSendMessage(userId1: String, userId2: String, message: MassageUsers) {
        viewModelScope.launch {
            try {
                val chatId = getChatIdForUsers(userId1, userId2) ?: getChatIdForUsers(userId2, userId1)
                if (chatId != null) {
                    sendMessage(chatId, message)
                } else {
                    val newChatId = createChat(listOf(userId1, userId2))
                    sendMessage(newChatId, message)
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error getting chat ID and sending message", e)
            }
        }
    }

    fun observeMessages(chatId: String) {
        val messageQuery = chatsRef.child(chatId).child("messages")
        messageQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newMessages = mutableListOf<MassageUsers>()
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(MassageUsers::class.java)
                    message?.let {
                        newMessages.add(it)
                    }
                }
                messages.value = newMessages
                Log.d("ChatViewModel", "Messages updated: $newMessages")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatViewModel", "Error observing messages", error.toException())
            }
        })
    }*/




