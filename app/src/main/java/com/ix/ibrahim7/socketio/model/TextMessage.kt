package com.ix.ibrahim7.socketio.model

import java.util.*

data class TextMessage(
        var message: String,
        var SenderName: String,
        var date: Date?,
        var type: String
) {
    constructor() : this("",  "", null, "")
}