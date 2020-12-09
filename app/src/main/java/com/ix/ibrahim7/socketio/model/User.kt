package com.ix.ibrahim7.socketio.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (var username:String) : Parcelable{
    constructor():this("")
}