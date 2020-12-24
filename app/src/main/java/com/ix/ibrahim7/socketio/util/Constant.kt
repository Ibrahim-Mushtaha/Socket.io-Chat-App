package com.ix.ibrahim7.socketio.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.model.TextMessage
import com.ix.ibrahim7.socketio.model.User
import java.util.ArrayList

object Constant {

    const val TAG = "eee"
    const val USER = "user"
    const val USERID = "userid"
    const val START = "start"
    val JOIN = "join"
    val ALLUSERS = "AllUsers"
    val GROUPS = "Group"
    val ALLGROUPS = "AllGroup"
    const val ID = "id"
    const val NAME = "username"
    const val GROUPNAME = "name"
    const val USER_GROUP = "userGroup"
    val TEXT = "Text"
    val IMAGE = "image"
    val MESSAGE = "message"
    val SOURCE_ID = "source_id"
    val DES_ID = "des_id"
    val TYPE = "type"

    fun getSharePref(context: Context) =
            context.getSharedPreferences("Share", Activity.MODE_PRIVATE)

    fun editor(context: Context) = getSharePref(context).edit()


    fun getImage(activity: Activity, path: String, imageView: ImageView) {
        Glide.with(activity)
                .load(path)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageView)
    }

    fun getUser(context: Context): User = Gson().fromJson(getSharePref(context).getString(USER, "").toString(), User::class.java)


    fun getBitmapImage(activity: Activity, path: ByteArray, imageView: ImageView) {
        Glide.with(activity)
                .asBitmap()
                .load(path)
                .into(imageView)
    }

    fun decodeImage(date: ArrayList<TextMessage>, position: Int): ByteArray {
        return android.util.Base64.decode(date[position].message, android.util.Base64.DEFAULT)
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

        // Create a new ArrayList
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