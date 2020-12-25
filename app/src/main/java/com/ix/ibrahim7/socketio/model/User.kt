package com.ix.ibrahim7.socketio.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        val id: String = "",
        val username: String = "",
        val image: String = "",
        var isOnline: Boolean = false
) : Parcelable