package firebase

import DataClass.UserCard
import DataClass.UserCardAdd
import DataClass.UserCardFriend
import DataClass.UserSCardInformations
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.diplom1.ShedPreferences
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
//import androidx.compose.runtime.MutableState
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import viewModel.CardVolonterViewModel
import viewModel.UserType

class FireBaseIDCardUser {


    suspend fun searchInFirestore(
        searchText: String,
        results: MutableList<String>
    ): List<String> {
        val firestore = Firebase.firestore

        // Выполнение запроса к коллекции Firestore
        val querySnapshot = firestore.collection("usersVolonters")
            .whereGreaterThanOrEqualTo("email", searchText)
            .whereLessThanOrEqualTo("email", searchText + "\uf8ff")
            .get()
            .await()

        results.clear()

        // Обработка результатов запроса
        for (document in querySnapshot.documents) {
            val fieldValue = document.getString("email")
            fieldValue?.let {
                results.add(it)
            }
        }

        return results
    }

    //функция для получения Uid по  Email
    suspend fun idByEmailSearch(
        stringSerch: String,
        nameCollections: String,
        uidByStringSave: MutableState<String>,
        nameFileInCollectionSearch: String,

        ) {
        try {
            if (stringSerch.isNotEmpty()) {
                val query = FirebaseFirestore.getInstance().collection(nameCollections)
                    .whereEqualTo(nameFileInCollectionSearch, stringSerch).get().await()
                if (!query.isEmpty) {
                    val uid = query.documents[0].id
                    uidByStringSave.value = uid
                } else {
                    Log.e("noDocument", "Дщкумент не найден")
                }
            }

        } catch (e: Exception) {
            Log.e("UidError", "${e.message}")
        }

    }

    //получение пути с катринки
    //  val id =  cardVolonterViewModel.uidByEmailSeaech()
    fun storageFireStore(userId: String): String {
        val storage = FirebaseStorage.getInstance().reference
        val image = storage.child("images/${userId}/profile.jpg")
        return image.path
    }

    suspend fun getAllUid(uids: MutableList<String>): List<String> {
        try {
            val querySnapshot =
                FirebaseFirestore.getInstance().collection(NameCollactionFirestore.UsersVolonters)
                    .get().await()
            for (document in querySnapshot.documents) {
                uids.add(document.id)
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error getting emails: ${e.message}")
        }

        return uids
    }

    suspend fun getAllUserKeys(
        collectionName: String,
        keys: MutableList<String>
    ): List<String> {
        try {
            val querySnapshot = withContext(Dispatchers.IO) {
                FirebaseFirestore.getInstance()
                    .collection(collectionName)
                    .get()
                    .await()
            }

            for (document in querySnapshot.documents) {
                keys.add(document.id)
            }
        } catch (e: Exception) {
            // Обработка ошибок
            Log.e("FirestoreError", "Error getting document keys: ${e.message}")
        }
        return keys
    }

    suspend fun getBitmapById(uid: String): Bitmap? {
        try {
            val imageRef =
                FirebaseStorage.getInstance().reference.child("images/${uid}/profile.jpg")
            val byteArray = imageRef.getBytes(Long.MAX_VALUE).await()
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            return bitmap
        } catch (e: Exception) {
            Log.e("StorageError", "Error getting image: ${e.message}")
        }

        return null
    }

    suspend fun getDataUserFireStore(
        nameCollectionFireStore: String,
        uid: String,
        cardVolonterViewModel: CardVolonterViewModel
    ): UserCard = withContext(Dispatchers.IO) {
        try {
            val document =
                FirebaseFirestore.getInstance().collection(nameCollectionFireStore).document(uid)
                    .get().await()
            val firestorename = document.getString(FirebaseString.name)

            val firestoreEmail = document.getString(FirebaseString.email)
            val firestoreSurname = document.getString(FirebaseString.surname)
            val bitmap = getBitmapById(uid)

            return@withContext UserCard(
                name = firestorename!!,
                surname = firestoreSurname!!,
                email = firestoreEmail!!,
                bitmap = bitmap
            )
        } catch (e: Exception) {
            Log.e("list", e.message.toString())
        }

        return@withContext UserCard(name = "", surname = "", email = "", bitmap = null)
    }

    suspend fun getDataInformationsCard(
        userType: UserType,
        saveUId: MutableState<String>,
        context: Context,
        cardVolonterViewModel: CardVolonterViewModel
    ): UserSCardInformations = withContext(Dispatchers.IO) {
        try {
            val nameCollections: MutableState<String> = mutableStateOf("")
            val statusUser = ShedPreferences.getUserType(context = context)
            if (statusUser == userType.UserBlind.value) {
                nameCollections.value = NameCollactionFirestore.UsersVolonters
            } else if (statusUser == userType.UserVolonters.value) {
                nameCollections.value = NameCollactionFirestore.UsersBlind
            }
            idByEmailSearch(
                cardVolonterViewModel.emailStateCardUser.value,
                nameCollections.value,
                saveUId,
                //cardVolonterViewModel.emailStateCardUser.value
                FirebaseString.email,
            )
            val document =
                FirebaseFirestore.getInstance().collection(nameCollections.value)
                    .document(saveUId.value)
                    .get().await()

            val adress = document.getString(FirebaseString.adress)
            val rayon = document.getString(FirebaseString.rayon)
            val region = document.getString(FirebaseString.region)
            val aboutMe = document.getString(FirebaseString.aboutMe)
            val number = document.getString(FirebaseString.phone)
            val birhday = document.getString(FirebaseString.birdhday)
            val expVolonters = document.getString(FirebaseString.experience)

            return@withContext UserSCardInformations(
                rayon = rayon.toString(),
                region = region.toString(),
                number = number.toString(),
                aboutMe = aboutMe.toString(),
                opytVolonters = expVolonters.toString(),
                birhday = birhday.toString(),
                adress = adress.toString()

            )
        } catch (e: Exception) {
            Log.e("list", e.message.toString())
        }
        return@withContext UserSCardInformations(
            region = "",
            rayon = "",
            number = "",
            opytVolonters = "",
            birhday = "",
            adress = "",
            aboutMe = ""
        )
    }

    suspend fun doesDataExist(collectionName: String, poleData: String, pole: String): Boolean {
        return try {
            val querySnapshot = FirebaseFirestore.getInstance()
                .collection(collectionName)
                .whereEqualTo(pole, poleData)
                .get()
                .await()

            // Если querySnapshot не пустой, значит есть совпадения
            !querySnapshot.isEmpty
            return true
        } catch (e: Exception) {
            Log.e("EmailCheck", "Ошибка при проверке email: ${e.message}")
            false
        }
    }

    fun removeMatchingElements(list: MutableList<UserCard>, valueToRemove: String) {
        list.removeAll { it.email == valueToRemove }
    }

    suspend fun getUserList(
        context: Context,
        cardVolonterViewModel: CardVolonterViewModel,
        userType: UserType
    ) {
        coroutineScope {
            try {


                // Загружаем данные асинхронно
                val deferred = launch {
                    val statusUser = ShedPreferences.getUserType(context = context)
                    if (cardVolonterViewModel.userCards.isNotEmpty()) {
                        cardVolonterViewModel.userCards.clear()
                        cardVolonterViewModel.keysUserOrCollections.clear()

                        //   cardVolonterViewModel.uniqueList.clear()


                    } else {

                        if (statusUser == userType.UserBlind.value) {
                            getAllUserKeys(
                                collectionName = NameCollactionFirestore.UsersVolonters,
                                keys = cardVolonterViewModel.keysUserOrCollections
                            )

                            for (idOne in cardVolonterViewModel.keysUserOrCollections) {
                                val user = getDataUserFireStore(
                                    nameCollectionFireStore = NameCollactionFirestore.UsersVolonters,
                                    uid = idOne,
                                    cardVolonterViewModel = cardVolonterViewModel
                                )
                                cardVolonterViewModel.userCards.add(user)
                                cardVolonterViewModel.removeDuplicatesUserCards(
                                    cardVolonterViewModel.userCards,
                                    cardVolonterViewModel.uniqueVolo
                                )
                            }
                        } else if (statusUser == userType.UserVolonters.value) {
                            getAllUserKeys(
                                collectionName = NameCollactionFirestore.UsersBlind,
                                keys = cardVolonterViewModel.keysUserOrCollections
                            )

                            for (idOne in cardVolonterViewModel.keysUserOrCollections) {
                                val user = getDataUserFireStore(
                                    nameCollectionFireStore = NameCollactionFirestore.UsersBlind,
                                    uid = idOne,
                                    cardVolonterViewModel = cardVolonterViewModel
                                )
                                cardVolonterViewModel.userCards.add(user)
                                cardVolonterViewModel.removeDuplicatesUserCards(
                                    cardVolonterViewModel.userCards,
                                    cardVolonterViewModel.uniqueListBlind
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("list", e.message.toString())

            }
        }

    }


    /*
*/

    //функция для отправки запроса дружбы ппользователю  или волонтеру
    suspend fun collectionsRequestByIdStatusСhange(
        nameCollections: String,
        userSerch: MutableState<String>,
        context: Context,
        userType: UserType,
    ) {
        delay(3000)
        try {
            val status = ShedPreferences.getUserType(context = context)

            val userCollections = if (status == userType.UserBlind.value) {
                NameCollactionFirestore.ReguestOrVolonter
            } else {
                NameCollactionFirestore.ReguestOrUsers
            }
            val documentsValue =
                FirebaseFirestore.getInstance().collection(userCollections)
                    .document(userSerch.value)
                    .get().await()
            if (documentsValue.exists()) {
                val request = documentsValue.getString(FirebaseString.request)
                if (request != null) {
                    FirebaseFirestore.getInstance().collection(userCollections)
                        .document(userSerch.value)
                        .update(FirebaseString.request, FirebaseString.expectation).await()
                } else {
                    Log.e("status", "Request field is null")
                }
            } else {
                Log.e("status", "Document does not exist")
            }
        } catch (e: Exception) {
            Log.e("status", "ошибка при смене поля" + e.message.toString())
        }
    }

    /*   suspend fun  keyAsCollections(cardVolonterViewModel: CardVolonterViewModel,
                                     userType: UserType){
           try {
               val collectionName =
                   if (cardVolonterViewModel.nameCollections.value == userType.UserBlind.value) {
                       NameCollactionFirestore.UsersBlind
                   } else {
                       NameCollactionFirestore.UsersVolonters
                   }
   */

    //получить документы из коллекции
    /*            val userDocuments = FirebaseFirestore.getInstance()
                    .collection(collectionName)
                    .get()
                    .await()
                val documentId = mutableStateOf("")
                if (!userDocuments.isEmpty) {
                    // Проверяем каждый документ из коллекции в коллекции RequestOrUsers
                    userDocuments.documents.forEach { document ->
                        documentId.value = document.id
                        val requestDocument = FirebaseFirestore.getInstance()
                            .collection(NameCollactionFirestore.ReguestOrUsers)
                            .document(documentId.value)
                            .get().await()

                        if(requestDocument.exists()){

                        }
                    }


                }

            }catch (e:Exception){
                Log.e("status" , e.message.toString())
            }
        }*/
    /*suspend fun doesDocumentWithUserUIDExist(
        collectionName: String,
        userUID: String
    ): Boolean {
        return try {
            val querySnapshot = FirebaseFirestore.getInstance()
                .collection(collectionName)
                .whereEqualTo("uidUserAuch", userUID)
                .limit(1)
                .get().await()

            !querySnapshot.isEmpty // Если запрос вернул документы, то возвращаем true
        } catch (e: Exception) {
            Log.e("FirestoreQuery", "Ошибка при выполнении запроса: ${e.message}")
            false
        }
    }*/
    suspend fun getDocumentsWithUserUID(
        collectionName: String,
        userUID: String
    ): List<DocumentSnapshot> {
        return try {
            val querySnapshot = FirebaseFirestore.getInstance()
                .collection(collectionName)
                .whereEqualTo("uidUserAuch", userUID)
                .get().await()

            querySnapshot.documents // Возвращаем список документов
        } catch (e: Exception) {
            Log.e("FirestoreQuery", "Ошибка при выполнении запроса: ${e.message}")
            emptyList() // В случае ошибки возвращаем пустой список
        }
    }

    suspend fun getDocumentsWithField(
        collectionName: String,
        field: String,
        value: Any
    ): List<DocumentSnapshot> {
        return try {
            val querySnapshot = FirebaseFirestore.getInstance()
                .collection(collectionName)
                .whereEqualTo(field, value)
                .get().await()

            querySnapshot.documents // Возвращаем список документов, удовлетворяющих запросу
        } catch (e: Exception) {
            Log.e("FirestoreQuery", "Ошибка при выполнении запроса: ${e.message}")
            emptyList() // В случае ошибки возвращаем пустой список
        }
    }

    //функция для принятия или отказа на заброс


    //для исходящих,

    //функция для отображения и просмотра листов
    suspend fun acceptOrNoUsersIsxodyachie(
        cardVolonterViewModel: CardVolonterViewModel,
        nameRequestCollections: String,
        nameCollections: String,
        firebaseStringPoleAuth: String,
        uidSerch: String,
    ): List<UserCardAdd> = withContext(Dispatchers.IO) {
        val users = mutableListOf<UserCardAdd>()
        try {

            val uid = FirebaseRegistrations().userID()

            val documents = getDocumentsWithField(
                nameRequestCollections,
                field = firebaseStringPoleAuth,
                uid.toString(),
            )
            if (documents.isNotEmpty()) {
                //      Log.e("list", "Лист заполнен")
            } else {
                //    Log.e("list", "Лист пуст")
            }

            for (document in documents) {
                if (document.exists()) {
                    val request = document.getString(FirebaseString.request)
                    if (request == FirebaseString.expectation) {
                        val email = document.getString(FirebaseString.email)
                        val uidAuch = document.getString(uidSerch)
                        //  Log.e("list", "есть запросы с таким полем ")
                        // Log.e("list", "email: $email")

                        val documentUserAdd =
                            FirebaseFirestore.getInstance().collection(nameCollections)
                                .document(uidAuch.toString()).get().await()
                        if (documentUserAdd.exists()) {
                            //узнаем путь к картинке через uid
                            val pathBitmap = storageFireStore(uidAuch.toString())
                            if (pathBitmap.isEmpty()) {
                                //   Log.e("list", "КАртинка пуста ")
                            }

                            val bitmap = cardVolonterViewModel.loadFirebaseImage(pathBitmap)

                            if (bitmap != null) {
                                //     Log.e("list", "КАртинка есть ")
                            } else {
                                //      Log.e("list", "КАртинка нет  ")
                            }
                            val name = documentUserAdd.getString(FirebaseString.name)
                            val surname = documentUserAdd.getString(FirebaseString.surname)
                            val emailUser = documentUserAdd.getString(FirebaseString.email)

                            users.add(
                                UserCardAdd(
                                    name = name!!,
                                    surname = surname!!,
                                    email = emailUser!!,
                                    bitmap = bitmap
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("userAdd", "Ошибка: ${e.message}")
        }

        return@withContext users
    }

    val userIDSearch = mutableStateOf("")
    val userIDAuch = mutableStateOf("")

    //функция для отправки
    suspend fun requestYes(
        context: Context,
        userType: UserType,
        cardVolonterViewModel: CardVolonterViewModel,
    ) {
        val status = ShedPreferences.getUserType(context)
        val nameCollectionsReguest =
            if (status == userType.UserVolonters.value) {
                NameCollactionFirestore.ReguestOrVolonter
            } else {
                NameCollactionFirestore.ReguestOrUsers
            }
        val nameCollectionsOrFrendReguest =
            if (status == userType.UserVolonters.value) {
                NameCollactionFirestore.ReguestOrUsers
            } else {
                NameCollactionFirestore.ReguestOrVolonter
            }

        val requestDocument =
            FirebaseFirestore.getInstance().collection(nameCollectionsReguest).get().await()
        for (document in requestDocument.documents) {
            if (document.exists()) {
                val email = document.getString(FirebaseString.email)
                if (cardVolonterViewModel.emailStateCardUser.value.contains(email.toString())) {
                    Log.e("list", "Email совпал")
                    val request = document.getString(FirebaseString.request)
                    if (request.toString().contains(FirebaseString.expectation)) {
                        Log.e("list", "поле запроса совпало")
                        val documentId = document.id
                        val doc = FirebaseFirestore.getInstance().collection(nameCollectionsReguest)
                            .document(documentId)
                        doc.update(FirebaseString.request, ShedPreferences.yes)
                        val userIdSearch = document.getString(FirebaseString.uidUserSearch)
                        val userIdAuch = document.getString(FirebaseString.uidUserAuch)
                        userIDSearch.value = userIdSearch.toString()
                        userIDAuch.value = userIdAuch.toString()
                        ShedPreferences.saveShedPreferences(
                            context = context,
                            UserFileCollections = ShedPreferences.FileCollectionsListFriend,
                            keyFile = ShedPreferences.FileListAdd,
                            value = ShedPreferences.listAddYes
                        )
                        val statusList = ShedPreferences.getShedPreferences(
                            context = context,
                            UserFileCollections = ShedPreferences.FileCollectionsListFriend,
                            keyFile = ShedPreferences.FileListAdd
                        )
                        Toast.makeText(
                            context,
                            statusList.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("list", "поле &&& принято")
                    }
                }
            }
        }

        //прошлис по коллекции запрос от друзей
        val requestFriend = FirebaseFirestore.getInstance()
            .collection(nameCollectionsOrFrendReguest).get().await()
        for (friendDocument in requestFriend) {
            if (friendDocument.exists()) {
                // получили поле друга
                val uidFriendAuch =
                    friendDocument.getString(FirebaseString.uidUserAuch)
                val uidFriendSearch =
                    friendDocument.getString(FirebaseString.uidUserSearch)
                if (uidFriendAuch.toString().contains(userIDSearch.value)) {
                    Log.e("list", "поля атентификации совпали")
                    if (uidFriendSearch.toString()
                            .contains(userIDAuch.value)
                    ) {
                        val requestFriends =
                            friendDocument.getString(FirebaseString.request)
                        if (requestFriends.toString()
                                .contains(FirebaseString.expectation)
                        ) {
                            val friendDocumentId = friendDocument.id
                            val docFriend = FirebaseFirestore.getInstance()
                                .collection(nameCollectionsOrFrendReguest)
                                .document(friendDocumentId)
                            docFriend.update(
                                FirebaseString.request,
                                ShedPreferences.yes
                            )
                            ShedPreferences.saveShedPreferences(
                                context = context,
                                UserFileCollections = ShedPreferences.FileCollectionsListFriend,
                                keyFile = ShedPreferences.FileListAdd,
                                value = ShedPreferences.listAddYes
                            )
                            val statusList = ShedPreferences.getShedPreferences(
                                context = context,
                                UserFileCollections = ShedPreferences.FileCollectionsListFriend,
                                keyFile = ShedPreferences.FileListAdd
                            )
                            Toast.makeText(
                                context,
                                "Запрос успешно принят" + statusList.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
            }
        }
    }

    suspend fun requestNo(
        context: Context,
        userType: UserType,
        cardVolonterViewModel: CardVolonterViewModel
    ) {
        val status = ShedPreferences.getUserType(context)
        val nameCollectionsReguest =
            if (status == userType.UserVolonters.value) {
                NameCollactionFirestore.ReguestOrVolonter
            } else {
                NameCollactionFirestore.ReguestOrUsers
            }
        val nameCollectionsOrFrendReguest =
            if (status == userType.UserVolonters.value) {
                NameCollactionFirestore.ReguestOrUsers
            } else {
                NameCollactionFirestore.ReguestOrVolonter
            }

        val requestDocument =
            FirebaseFirestore.getInstance().collection(nameCollectionsReguest).get().await()
        for (document in requestDocument.documents) {
            if (document.exists()) {
                val email = document.getString(FirebaseString.email)
                if (cardVolonterViewModel.emailStateCardUser.value.contains(email.toString())) {
                    Log.e("list", "Email совпал")
                    val request = document.getString(FirebaseString.request)
                    if (request.toString().contains(FirebaseString.expectation)) {
                        Log.e("list", "поле запроса совпало")
                        val documentId = document.id
                        val doc = FirebaseFirestore.getInstance().collection(nameCollectionsReguest)
                            .document(documentId)
                        doc.update(FirebaseString.request, ShedPreferences.no)
                        val userIdSearch = document.getString(FirebaseString.uidUserSearch)
                        val userIdAuch = document.getString(FirebaseString.uidUserAuch)
                        doc.delete().await()
                        ShedPreferences.saveShedPreferences(
                            context = context,
                            UserFileCollections = ShedPreferences.FileCollectionsListFriend,
                            keyFile = ShedPreferences.FileListAdd,
                            value = ShedPreferences.listAddNo

                        )
                        val statusList = ShedPreferences.getShedPreferences(
                            context = context,
                            UserFileCollections = ShedPreferences.FileCollectionsListFriend,
                            keyFile = ShedPreferences.FileListAdd
                        )
                        userIDSearch.value = userIdSearch.toString()
                        userIDAuch.value = userIdAuch.toString()
                        Toast.makeText(
                            context,
                            statusList.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("list", "поле &&& принято")
                    }
                }
            }
        }

        //прошлис по коллекции запрос от друзей
        val requestFriend = FirebaseFirestore.getInstance()
            .collection(nameCollectionsOrFrendReguest).get().await()
        for (friendDocument in requestFriend) {
            if (friendDocument.exists()) {
                // получили поле друга
                val uidFriendAuch =
                    friendDocument.getString(FirebaseString.uidUserAuch)
                val uidFriendSearch =
                    friendDocument.getString(FirebaseString.uidUserSearch)
                if (uidFriendAuch.toString().contains(userIDSearch.value)) {
                    Log.e("list", "поля атентификации совпали")
                    if (uidFriendSearch.toString()
                            .contains(userIDAuch.value)
                    ) {
                        val requestFriends =
                            friendDocument.getString(FirebaseString.request)
                        if (requestFriends.toString()
                                .contains(FirebaseString.expectation)
                        ) {
                            val friendDocumentId = friendDocument.id
                            val docFriend = FirebaseFirestore.getInstance()
                                .collection(nameCollectionsOrFrendReguest)
                                .document(friendDocumentId)
                            docFriend.update(
                                FirebaseString.request,
                                ShedPreferences.no
                            )
                            docFriend.delete().await()
                            ShedPreferences.saveShedPreferences(
                                context = context,
                                UserFileCollections = ShedPreferences.FileCollectionsListFriend,
                                keyFile = ShedPreferences.FileListAdd,
                                value = ShedPreferences.listAddNo
                            )
                            val statusList = ShedPreferences.getShedPreferences(
                                context = context,
                                UserFileCollections = ShedPreferences.FileCollectionsListFriend,
                                keyFile = ShedPreferences.FileListAdd
                            )

                            Toast.makeText(
                                context,
                                "Запрос отклонен" + statusList.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    suspend fun getFriendList(
        context: Context,
        userType: UserType,
        cardVolonterViewModel: CardVolonterViewModel,
    ): MutableList<UserCardFriend> = withContext(Dispatchers.IO) {
        val userCardFriend: MutableList<UserCardFriend> = mutableListOf()

        val status = ShedPreferences.getUserType(context)
        try {
            val nameCollectionsReguest = if (status == userType.UserVolonters.value) {
                NameCollactionFirestore.ReguestOrVolonter
            } else {
                NameCollactionFirestore.ReguestOrUsers
            }

            val nameCollections = if (status == userType.UserVolonters.value) {
                NameCollactionFirestore.UsersBlind
            } else {
                NameCollactionFirestore.UsersVolonters
            }

            val uid = FirebaseRegistrations().userID()
            val documents =
                FirebaseFirestore.getInstance().collection(nameCollectionsReguest).get().await()

            for (document in documents.documents) {
                val uidUserAuch = document.getString(FirebaseString.uidUserAuch)
                if (uidUserAuch != null && uidUserAuch.contains(uid.toString())) {
                    Log.e("add", "поля аутентификации совпали")
                    val request = document.getString(FirebaseString.request)
                    if (request != null && request.contains(ShedPreferences.yes)) {
                        Log.e("add", "нашлись люди")
                        val documentId =
                            document.getString(FirebaseString.uidUserSearch).toString()
                        val documentsUserAdd = FirebaseFirestore.getInstance()
                            .collection(nameCollections)
                            .document(documentId).get().await()

                        if (documentsUserAdd.exists()) {
                            Log.e("add", "документ существует")
                            val name = documentsUserAdd.getString(FirebaseString.name)
                            val surname = documentsUserAdd.getString(FirebaseString.surname)
                            val rayon = documentsUserAdd.getString(FirebaseString.rayon)
                            val region = documentsUserAdd.getString(FirebaseString.region)
                            val birhday = documentsUserAdd.getString(FirebaseString.birdhday)
                            val number = documentsUserAdd.getString(FirebaseString.phone)
                            val address = documentsUserAdd.getString(FirebaseString.adress)
                            val aboutMe = documentsUserAdd.getString(FirebaseString.aboutMe)
                            val yersVolonters =
                                documentsUserAdd.getString(FirebaseString.experience)
                            val email = documentsUserAdd.getString(FirebaseString.email)
                            val pathBitmap = storageFireStore(documentId)
                            if (pathBitmap.isEmpty()) {
                                   Log.e("list", "КАртинка пуста ")
                            }

                            val bitmap = cardVolonterViewModel.loadFirebaseImage(pathBitmap)
                            if (name != null && surname != null && birhday != null && number != null && address != null && email != null && yersVolonters != null && rayon != null && region != null && aboutMe != null) {
                                ShedPreferences.saveShedPreferences(
                                   context, ShedPreferences.FileCollectionsListFriend,
                                   keyFile = ShedPreferences.FileListAdd,
                                   value = ShedPreferences.listAddYes
                               )
                                val list = UserCardFriend(
                                    nane = name,
                                    surname = surname,
                                    birhday = birhday,
                                    number = number,
                                    adress = address,
                                    email = email,
                                    yersVolonters = yersVolonters,
                                    rayon = rayon,
                                    region = region,
                                    bitmap = bitmap,
                                    anoutMe = aboutMe
                                )
                                userCardFriend.add(list)
                            }
                        } else {
                            Log.e("add", "документ не существует")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("FriendList", e.message.toString())
        }

        val statusList = ShedPreferences.getShedPreferences(
            context, UserFileCollections = ShedPreferences.FileCollectionsListFriend,
            keyFile = ShedPreferences.FileListAdd
        )
        if (userCardFriend.isEmpty()) {
            if (statusList == ShedPreferences.listAddYes) {
                ShedPreferences.saveShedPreferences(
                    context, UserFileCollections = ShedPreferences.FileCollectionsListFriend,
                    keyFile = ShedPreferences.FileListAdd,
                    value = ShedPreferences.listAddNo
                )
            }
        }

        return@withContext userCardFriend
    }

    fun listTrue (){
       // documents()
    }

}

