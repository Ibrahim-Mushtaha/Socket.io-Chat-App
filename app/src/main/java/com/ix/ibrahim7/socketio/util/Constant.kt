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
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.model.TextMessage
import java.util.ArrayList

object Constant {

    const val USER ="user"
    const val USERID ="userid"
    const val START = "start"
    val TEXT ="Text"
    val IMAGE ="image"
    val MESSAGE ="message"

    fun getSharePref(context: Context) =
        context.getSharedPreferences("Share", Activity.MODE_PRIVATE)

    fun editor(context: Context) = getSharePref(context).edit()


    fun getImage(activity: Activity,path:String,imageView: ImageView){
        Glide.with(activity)
            .load(path)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(imageView)
    }


    fun getBitmapImage(activity: Activity,path:ByteArray,imageView: ImageView){
        Glide.with(activity)
            .asBitmap()
            .load(path)
            .into(imageView)
    }

    fun decodeImage(date: ArrayList<TextMessage>, position: Int):ByteArray {
        return android.util.Base64.decode(date[position].message, android.util.Base64.DEFAULT)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setUpStatusBar(activity: Activity, types: Int){
        val window: Window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (types == 1) {
            window.statusBarColor = ContextCompat.getColor(activity, R.color.white)
        }else{
            window.statusBarColor = ContextCompat.getColor(activity, R.color.purple_700)
        }
    }

}