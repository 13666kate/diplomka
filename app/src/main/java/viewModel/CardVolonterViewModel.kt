package viewModel

import DataClass.UserCard
import DataClass.UserCardAdd
import DataClass.UserCardFriend
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle

import androidx.compose.runtime.MutableState

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diplom1.R
import com.example.diplom1.ShedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import firebase.FireBaseIDCardUser
import firebase.FirebaseRegistrations
import firebase.FirebaseString
import firebase.NameCollactionFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CardVolonterViewModel : ViewModel() {

    val textSearch: MutableStateFlow<String> = MutableStateFlow("")
    val text = mutableStateOf("")
    val keysUserOrCollections = mutableStateListOf<String>()
    val imageStateAccountAvatar = mutableStateOf<ImageVector>(Icons.Default.AccountCircle)
    val imageAvatarValueNo = mutableStateOf(R.drawable.baseline_account_circle_24)
    val imageStateBitmap = mutableStateOf<Bitmap?>(null)

    val imageState = mutableStateOf<Bitmap?>(null)
    val uidFirestore = mutableStateListOf<String>()
    val list: MutableList<String> = mutableListOf()
    val imageStateCardUser = mutableStateOf<Bitmap?>(null)
    val nameStateCardUser = mutableStateOf("Name")
    val surnameStateCardUser = mutableStateOf("Фамилия")
    val emailStateCardUser = mutableStateOf("Email")
    val expVolonters = mutableStateOf("Годы в волонтерстве")
    val birhdayUserCards = mutableStateOf("День рождение")
    val numberUserCarsd = mutableStateOf("Телефон")
    val aboutmeUserCarsd = mutableStateOf("О себе ")
    val adresStateCardUser = mutableStateOf("Адрес проживания")
    val regionStateCardUser = mutableStateOf("Область ")
    val rayonStateCardUser = mutableStateOf("Район")

    val nameCollections = mutableStateOf("")
    val saveUidInformationsCard = mutableStateOf("")
    val nameCollection = mutableStateOf("")
    val userCards: MutableList<UserCard> = mutableStateListOf()
    val uniqueVolo: MutableList<UserCard> = mutableStateListOf()
    val friendList: MutableList<UserCardFriend> = mutableStateListOf()
    val uniqueListBlind: MutableList<UserCard> = mutableStateListOf()
    val uniqueSet = mutableSetOf<String>()

    val searchOrEmailUser: MutableState<String> = mutableStateOf("")
    val stateIdUserSerch: MutableState<String> = mutableStateOf("")

    //  val stateIdUserSerch2: MutableState<String> = mutableStateOf("")
    val request: MutableState<String> = mutableStateOf(FirebaseString.expectation)

    var listUserType: Flow<List<UserCard>> = MutableStateFlow(emptyList())
    val radioButton = mutableStateOf(true)
    val listUserAddMe = mutableStateListOf<UserCardAdd>()
    val listAddUserMe = mutableStateListOf<UserCardAdd>()
    val listAdd = mutableStateListOf<UserCardAdd>()


    fun loadData(cardVolonterViewModel: CardVolonterViewModel,
                         context: Context,
                         userType: UserType) {
        viewModelScope.launch {
            // Загрузите списки данных асинхронно
            val userAddList = NotificationsUserAdd(
                cardVolonterViewModel = cardVolonterViewModel,
                context = context,
                userType = userType,
            )
            val addList = UserViewingAddMe(
                cardVolonterViewModel = cardVolonterViewModel,
                context = context,
                userType = userType,
            )

            listAddUserMe.addAll(userAddList)
            listAdd.addAll(addList)
        }
    }
    /* fun getList(
         context: Context,
         cardVolonterViewModel: CardVolonterViewModel,
         userType: UserType
     ) {
         viewModelScope.launch {
             FireBaseIDCardUser().getUserList(context, cardVolonterViewModel, userType)
         }
     }*/

    fun idUserFireStore() {
        viewModelScope.launch {
            FireBaseIDCardUser().getAllUid(uidFirestore)
        }
    }

    //функция для удаления дубликатов в списке
    fun removeDuplicatesUserCards(
        list: List<UserCard>,
        newlist: MutableList<UserCard>
    ): List<UserCard> {
        // Здесь предполагается, что UserData имеет строковое свойство для определения уникальности, замените его на соответствующее свойство
        for (item in list) {
            if (uniqueSet.add(item.email)) { // Замените item.uniqueIdentifier на ваше свойство, которое определяет уникальность
                newlist.add(item)
            }
        }

        return newlist
    }

    fun removeDuplicatesUserCardsAdd(
        list: List<UserCardAdd>,
        newlist: MutableList<UserCardAdd>
    ): List<UserCardAdd> {
        // Здесь предполагается, что UserData имеет строковое свойство для определения уникальности, замените его на соответствующее свойство
        for (item in list) {
            if (uniqueSet.add(item.email)) { // Замените item.uniqueIdentifier на ваше свойство, которое определяет уникальность
                newlist.add(item)
            }
        }

        return newlist
    }
    fun removeDuplicatesUserCardsFriends(
        list: List<UserCardFriend>,
        newlist: MutableList<UserCardFriend>
    ): List<UserCardFriend> {
        // Здесь предполагается, что UserData имеет строковое свойство для определения уникальности, замените его на соответствующее свойство
        for (item in list) {
            if (uniqueSet.add(item.email)) { // Замените item.uniqueIdentifier на ваше свойство, которое определяет уникальность
                newlist.add(item)
            }
        }

        return newlist
    }


    private var dataLoaded = false
    fun loadDataIfNeeded() {
        if (!dataLoaded) {
            viewModelScope.launch {
                try {
                    val userCards = userCards
                    // Обновляем состояние userCardsState или что-то подобное, используя полученные данные
                    dataLoaded = true
                } catch (e: Exception) {
                    // Обработка ошибок при загрузке данных
                    Log.e("CardVolonterViewModel", "Ошибка при загрузке данных: ${e.message}")
                }
            }
        }
    }

    fun bitmabFireStore(): Bitmap? {
        viewModelScope.launch {
            val listUid = idUserFireStore().toString()
            for (idOne in listUid) {
                val bitmap = FireBaseIDCardUser().getBitmapById(idOne.toString())
                imageState.value = bitmap

            }

        }
        return imageState.value
    }

    fun nameCollections(context: Context, userType: UserType) {
        val statudUser = ShedPreferences.getUserType(context)
        if (statudUser == userType.UserVolonters.value) {
            nameCollection.value = NameCollactionFirestore.UsersBlind
        } else if (statudUser == userType.UserBlind.value) {
            nameCollection.value = NameCollactionFirestore.UsersVolonters
        }
    }

 /*   suspend fun doesDocumentWithFieldsExist(
        collectionName: String,
        fields: Map<String, Any>
    ): Boolean {
        return try {
            var query = FirebaseFirestore.getInstance().collection(collectionName).limit(1)

            for ((key, value) in fields) {
                query = query.whereEqualTo(key, value)
            }

            val querySnapshot = query.get().await()
            !querySnapshot.isEmpty // Если запрос вернул документы, то возвращаем true
        } catch (e: Exception) {
            Log.e("FirestoreQuery", "Ошибка при выполнении запроса: ${e.message}")
            false
        }
    }
*/
 suspend fun doesDocumentWithFieldsExistExcludingField(
     collectionName: String,
     data: Map<String, Any>,
     fieldToExclude: String
 ): Boolean {
     return try {
         val collectionRef = FirebaseFirestore.getInstance().collection(collectionName)
         var query: Query = collectionRef // Начальная инициализация с коллекцией

         data.forEach { (key, value) ->
             if (key != fieldToExclude) {
                 query = query.whereEqualTo(key, value)
             }
         }

         val querySnapshot = query.get().await()
         !querySnapshot.isEmpty
     } catch (e: Exception) {
         Log.e("FirestoreQuery", "Ошибка при выполнении запроса: ${e.message}")
         false
     }
 }
    fun requesAddUser(
        email: String,
        stateIdAuch: MutableState<String>,
        stateEmailUserSerch: MutableState<String>,
        nameCollection: String,
        context: Context,
        userType: UserType,
    ) {
        viewModelScope.launch {
            val uidAuthUser = FirebaseRegistrations().userID()
            if (uidAuthUser != null) {
                stateIdAuch.value = uidAuthUser
            }
            FireBaseIDCardUser().idByEmailSearch(
                stringSerch = email,
                nameCollections = nameCollection,
                uidByStringSave = stateEmailUserSerch,
                FirebaseString.email
            )

            val status = ShedPreferences.getUserType(context)
            val userCollections = if (status == userType.UserBlind.value) {
                NameCollactionFirestore.ReguestOrUsers
            } else {
                NameCollactionFirestore.ReguestOrVolonter
            }

            if (stateEmailUserSerch.value.isNotEmpty() && stateIdAuch.value.isNotEmpty()) {
                try {
                    val querySnapshot = FirebaseFirestore.getInstance()
                        .collection(nameCollection).get().await()
                    if (!querySnapshot.isEmpty) {
                        for (document in querySnapshot.documents) {
                            val uidAuch = document.getString(FirebaseString.uidUserAuch).toString()
                            if (uidAuch.contains(stateIdAuch.value)) {
                                val statusFriends = document.getString(FirebaseString.request).toString()

                                 if (statusFriends.contains(FirebaseString.expectation)){
                                     Toast.makeText(context, "Он уже в друзьях", Toast.LENGTH_SHORT)
                                         .show()
                                     }
                            }else {
                                nameCollections(context = context, userType = userType)
                                val newDocument = FirebaseFirestore.getInstance()
                                    .collection(userCollections)
                                    .document()

                                val data = hashMapOf(
                                    FirebaseString.uidUserAuch to stateIdAuch.value,
                                    FirebaseString.uidUserSearch to stateEmailUserSerch.value,
                                    FirebaseString.request to request.value,
                                    FirebaseString.email to email
                                )
                                val existDocument =
                                    doesDocumentWithFieldsExistExcludingField(userCollections,data, FirebaseString.request)
                                if (existDocument) {

                                    Toast.makeText(
                                        context,
                                        "Запрос уже отправлен",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    newDocument.set(data).await()
                                    Toast.makeText(context, "Запрос  отправлен", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }

                    } else {

                        val collectionsAdd = FirebaseFirestore.getInstance()
                            .collection(userCollections)
                            .document(/*stateEmailUserSerch.value*/)

                        val data = hashMapOf(
                            FirebaseString.uidUserAuch to stateIdAuch.value,
                            FirebaseString.uidUserSearch to stateEmailUserSerch.value,
                            FirebaseString.request to request.value,
                            FirebaseString.email to email
                        )
                        val existDocument =
                            doesDocumentWithFieldsExistExcludingField(userCollections,data, FirebaseString.request)
                        if (existDocument) {

                            Toast.makeText(
                                context,
                                "Запрос уже отправлен",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            collectionsAdd.set(data).await()
                            Toast.makeText(context, "Запрос  отправлен", Toast.LENGTH_SHORT)
                                .show()
                        }



                    }
                } catch (e: Exception) {
                    Log.e("reques", e.message.toString())
                }
            }
        }


    }

    fun collectionsRequestByIdUserVolonterStatusСhange(
        context: Context, userType: UserType
    ) {

        viewModelScope.launch {
            val status = ShedPreferences.getUserType(context)
            val requestCollrctions = if (status == userType.UserBlind.value) {
                NameCollactionFirestore.ReguestOrUsers
            } else {
                NameCollactionFirestore.UsersVolonters
            }
            FireBaseIDCardUser().collectionsRequestByIdStatusСhange(
                nameCollections = NameCollactionFirestore.ReguestOrUsers,
                userSerch = stateIdUserSerch,
                context = context,
                userType = userType

            )
        }
    }

    fun getDataInformationsOrCards(
        context: Context, userType: UserType,
        cardVolonterViewModel: CardVolonterViewModel
    ) {
        viewModelScope.launch {
            val informations = FireBaseIDCardUser().getDataInformationsCard(
                saveUId = saveUidInformationsCard,
                userType = userType,
                context = context,
                cardVolonterViewModel = cardVolonterViewModel

            )
            cardVolonterViewModel.adresStateCardUser.value = informations.adress
            cardVolonterViewModel.regionStateCardUser.value = informations.region
            cardVolonterViewModel.rayonStateCardUser.value = informations.rayon
            cardVolonterViewModel.aboutmeUserCarsd.value = informations.aboutMe
            cardVolonterViewModel.expVolonters.value = informations.opytVolonters
            cardVolonterViewModel.numberUserCarsd.value = informations.number
            cardVolonterViewModel.birhdayUserCards.value = informations.birhday

        }
    }

    fun image(
        image: MutableState<Bitmap?>,
        path: String
    ) {
        viewModelScope.launch {
            val bitmap = loadFirebaseImage(path)
            image.value = bitmap
        }

    }

    suspend fun loadFirebaseImage(path: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val storageRef = FirebaseStorage.getInstance().getReference(path)
                // Максимальный размер файла для загрузки (в данном случае 7 MB)
                val maxDownloadSizeBytes: Long = 7 * 1024 * 1024

                // Получение массива байтов изображения
                val byteArray = storageRef.getBytes(maxDownloadSizeBytes).await()

                // Преобразование массива байтов в Bitmap
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            } catch (e: Exception) {
                Log.e("imageError", e.message.toString())
                null
            }
        }
    }


    val userCardAdd: MutableList<UserCardAdd> = mutableStateListOf()

    //функция для просмотра, кому отправлен запрос
   fun UserViewingAddMe(
        cardVolonterViewModel: CardVolonterViewModel,
        context: Context,
        userType: UserType,
    ): MutableList<UserCardAdd> {
        viewModelScope.launch {
            val statusUser = ShedPreferences.getUserType(context = context)
            val nameRequestCollections = if (statusUser == userType.UserVolonters.value) {
                NameCollactionFirestore.ReguestOrVolonter
            } else {
                NameCollactionFirestore.ReguestOrUsers
            }

            val nameCollections = if (statusUser == userType.UserVolonters.value) {
                NameCollactionFirestore.UsersBlind
            } else {
                NameCollactionFirestore.UsersVolonters
            }
            val user = FireBaseIDCardUser().acceptOrNoUsersIsxodyachie(
                cardVolonterViewModel = cardVolonterViewModel,
                nameRequestCollections = nameRequestCollections,
                nameCollections = nameCollections,
                firebaseStringPoleAuth = FirebaseString.uidUserAuch,
                uidSerch = FirebaseString.uidUserSearch
            )
            val list: MutableList<UserCardAdd> = mutableStateListOf()

            if (userCardAdd.isNotEmpty()) {
                list.clear()
                userCardAdd.clear()
                Log.e("listAdd", "Лист короче пуст ")
            }
            list.addAll(user)
            userCardAdd.addAll(list)
            //   removeDuplicatesUserCardsAdd(list, userCardAdd)

        }
        return userCardAdd
    }

    //функкия для принятия волонтера или пользователя
   fun NotificationsUserAdd(
        cardVolonterViewModel: CardVolonterViewModel,
        context: Context,
        userType: UserType,
    ): MutableList<UserCardAdd> {
        viewModelScope.launch {
            val statusUser = ShedPreferences.getUserType(context = context)

            val nameRequestCollections = if (statusUser == userType.UserVolonters.value) {
                NameCollactionFirestore.ReguestOrUsers
            } else {
                NameCollactionFirestore.ReguestOrVolonter
            }

            val nameCollections = if (statusUser == userType.UserVolonters.value) {
                NameCollactionFirestore.UsersBlind
            } else {
                NameCollactionFirestore.UsersVolonters
            }


            val user = FireBaseIDCardUser().acceptOrNoUsersIsxodyachie(
                cardVolonterViewModel = cardVolonterViewModel,
                nameRequestCollections = nameRequestCollections,
                nameCollections = nameCollections,
                firebaseStringPoleAuth = FirebaseString.uidUserSearch,
                uidSerch = FirebaseString.uidUserAuch
            )
            val list: MutableList<UserCardAdd> = mutableStateListOf()

            if (userCardAdd.isNotEmpty()) {
                list.clear()
                userCardAdd.clear()
              //  Log.e("listAdd", "Лист короче пуст ")
            }
            list.addAll(user)
            userCardAdd.addAll(list)
            //   removeDuplicatesUserCardsAdd(list, userCardAdd)

        }
        return userCardAdd
    }



    fun requestYes(context: Context,
                   userType: UserType,
                   cardVolonterViewModel: CardVolonterViewModel){
        viewModelScope.launch {
            FireBaseIDCardUser().requestYes(
                context = context,
                cardVolonterViewModel = cardVolonterViewModel,
                userType = userType
            )
        }
    }

    fun requestNo(
        context: Context,
        userType: UserType,
        cardVolonterViewModel: CardVolonterViewModel
    ){
        viewModelScope.launch {
            FireBaseIDCardUser().requestNo(
                context = context,
                userType = userType,
                cardVolonterViewModel = cardVolonterViewModel,

            )
        }
    }
    fun FriendList(context: Context,userType: UserType,
                   cardVolonterViewModel: CardVolonterViewModel):MutableList<UserCardFriend>{
        val list = mutableListOf<UserCardFriend>()
        viewModelScope.launch {

           val listUser =  FireBaseIDCardUser().getFriendList(
                context = context,
                userType,
                cardVolonterViewModel = cardVolonterViewModel

            )
            list.addAll(listUser)
            if(list.isEmpty()){
                Log.e("add","лист  пуст ")
            }
        }
        return list
    }
    /* suspend fun requestNo(
       context: Context,
       userType: UserType,
       cardVolonterViewModel: CardVolonterViewModel,
   ){ val status = ShedPreferences.getUserType(context)
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
                       Log.e("list", "поле запроса совпали")
                       val documentId = document.id
                       val doc = FirebaseFirestore.getInstance().collection(nameCollectionsReguest)
                           .document(documentId)
                       doc.update(FirebaseString.request, ShedPreferences.no)
                       //удаляем документ с таким именем
                       doc.delete().await()
                       val userIdSearch = document.getString(FirebaseString.uidUserSearch)
                       val userIdAuch = document.getString(FirebaseString.uidUserAuch)
                       Log.e("list", "поле отклонено")
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
                               if (uidFriendAuch.toString().contains(userIdSearch.toString())) {
                                   Log.e("list", "поля атентификации совпали")
                                   if (uidFriendSearch.toString()
                                           .contains(userIdAuch.toString())
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
                                           //удаляем документ с таким именем
                                           docFriend.delete().await()
                                           Toast.makeText(
                                               context,
                                               "Запрос отклонен",
                                               Toast.LENGTH_SHORT
                                           ).show()
                                       } } } } } } } } } } }
*/


}