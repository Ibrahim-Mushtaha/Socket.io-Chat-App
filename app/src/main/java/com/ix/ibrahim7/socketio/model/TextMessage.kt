package com.ix.ibrahim7.socketio.model

import java.util.*

data class TextMessage(
        val message: String = "",
        val SenderName: String = "",
        val date: Date? = null,
        val type: String = ""
)