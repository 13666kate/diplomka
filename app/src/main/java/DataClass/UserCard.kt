package DataClass

import android.graphics.Bitmap

data class UserCard(
    val name:String,
    val surname:String,
    val email:String,
    val bitmap: Bitmap?
)
