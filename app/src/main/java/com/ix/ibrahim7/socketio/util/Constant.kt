package com.ix.ibrahim7.socketio.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.model.Message
import com.ix.ibrahim7.socketio.model.User
import java.io.ByteArrayOutputStream
import java.util.ArrayList

object Constant {

    const val TAG = "eee"
    const val USER = "user"
    const val START = "start"
    val JOIN = "join"
    val ALLUSERS = "AllUsers"
    val GROUPS = "Group"
    val ALLGROUPS = "AllGroup"
    val UPDATEUSER = "updateStatus"
    val UPDATEPROFILE = "UpdateProfile"
    const val ID = "id"
    const val NAME = "username"
    const val GROUPNAME = "name"
    const val USERGROUP = "userGroup"
    val TEXT = "Text"
    val IMAGE = "image"
    val MESSAGE = "message"
    val SOURCE_ID = "source_id"
    val DES_ID = "des_id"
    val TYPE = "type"
    val ISONLINE = "isOnline"

    fun getSharePref(context: Context) =
            context.getSharedPreferences("Share", Activity.MODE_PRIVATE)

    fun editor(context: Context) = getSharePref(context).edit()

    fun getUser(context: Context): User = Gson().fromJson(getSharePref(context).getString(USER, "").toString(), User::class.java)


    fun getBitmapImage(activity: Activity, path: ByteArray, imageView: ImageView) {
        Glide.with(activity)
                .asBitmap()
                .load(path)
                .into(imageView)
    }

    fun  decodeImage(imagePath:String): Bitmap? {
        val decodeString= android.util.Base64.decode(imagePath, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodeString, 0, decodeString.size)
    }

    


     fun convertToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val image: String =
                android.util.Base64.encodeToString(
                        byteArrayOutputStream.toByteArray(),
                        android.util.Base64.DEFAULT
                )
        return image
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setUpStatusBar(activity: Activity, types: Int) {
        val window: Window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        when (types) {
            1 -> window.statusBarColor = ContextCompat.getColor(activity, R.color.white)
            else -> window.statusBarColor = ContextCompat.getColor(activity, R.color.purple_700)
        }
    }

    fun <T> removeDuplicates(list: ArrayList<T>): ArrayList<T>? {
        val newList = ArrayList<T>()
        // Traverse through the first list
        for (element in list) {
            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {
                newList.add(element)
            }
        }
        // return the new list
        return newList
    }


}