package com.ix.ibrahim7.socketio.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Groups (var Group_name:String,var channelId:String) : Parcelable{
    constructor():this("","")
}