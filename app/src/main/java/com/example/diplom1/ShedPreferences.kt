package com.example.diplom1

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object ShedPreferences {
    private const val PREFS_NAMEUSER = "AuchUser"
    private const val PREFS_NAMEVOLONTER = "AuchVolonter"

    //ключ для доступа к аутентифицированному пользователю
    private const val KEY_AUTH = "auth"

    /*val statusNoAuth = "no"
    val statusTrue = "true"
    val statusFalse = "false"
    var nullableBoolean: Boolean? = null*/
    val statusNoAuth: MutableState<String> = mutableStateOf("no")
    val statusListUser: MutableState<String> = mutableStateOf("")

    //   val statusNoAuth = "no"
    const val userFile = "userfile"
    const val userFileStatus = "userfileStatus"
    const val userTypes = "usertype"
    const val userTrue = "true"
    const val userFalse = "false"

    const val  yes = "yes"
    const val  no = "no"

    const val  FileCollectionsList = "FileCollectionsList"
    const val  FilenameList = "FileNameList"

    const val  FileCollectionsListFriend = "FileCollectionsListFriend"
    val FileListAdd = "FileListAdd"
    val listAddYes = "listAddYes"
    val listAddNo = "listAddNo"

    const val listUserSee = "listUserSee"
    const val keylistUserAdd = "listUserAdd"
    fun saveUserType(context: Context, userType: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(userFile, Context.MODE_PRIVATE)
        prefs.edit().putString(userTypes, userType).apply()
    }

    // Получите тип текущего пользователя из SharedPreferences
    fun getUserType(context: Context): String? {
        val prefs: SharedPreferences = context.getSharedPreferences(userFile, Context.MODE_PRIVATE)
        return prefs.getString(userTypes, statusNoAuth.value)
    }

    fun saveShedPreferences(
        context: Context,
        UserFileCollections: String,
        keyFile: String, value: String
    ) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(UserFileCollections, Context.MODE_PRIVATE)
        prefs.edit().putString(keyFile, value).apply()
    }

    // Получите тип текущего пользователя из SharedPreferences
    fun getShedPreferences(context: Context, UserFileCollections: String,
                           keyFile: String): String? {
        val prefs: SharedPreferences =
            context.getSharedPreferences(UserFileCollections, Context.MODE_PRIVATE)
        return prefs.getString(keyFile, statusListUser.value)
    }
    fun saveUserTypeStatus(context: Context, userType: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(userFileStatus, Context.MODE_PRIVATE)
        prefs.edit().putString(userFileStatus, userType).apply()
    }

    // Получите тип текущего пользователя из SharedPreferences
    fun getUserTypeStatus(context: Context): String? {
        val prefs: SharedPreferences = context.getSharedPreferences(userFileStatus, Context.MODE_PRIVATE)
        return prefs.getString(userFileStatus, statusNoAuth.value)
    }

    fun logoutUser(context: Context) {
        ShedPreferences.saveUserType(context, ShedPreferences.statusNoAuth.value)
    }
    //метод сохранения пользователя
    /*    fun setAuthenticated(context: Context, isAuthenticated: Boolean, PREFS_NAME:String) {
            val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putBoolean(KEY_AUTH, isAuthenticated).apply()
        }
        //функция для проверки статуса идентификации
        fun isAuthenticated(context: Context, PREFS_NAME:String): Boolean {
            val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return prefs.getBoolean(KEY_AUTH, false)
        }*/
}