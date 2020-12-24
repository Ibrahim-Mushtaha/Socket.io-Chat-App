package com.ix.ibrahim7.socketio.util

import android.app.Application
import android.util.Log
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import java.lang.RuntimeException
import java.net.URISyntaxException

class ChatApplication : Application() {

    private var mSocket: Socket? = null

    init {
        mSocket = try {
            IO.socket("http://192.168.0.102:4000")
        } catch (e: URISyntaxException) {
            throw RuntimeException()
        }
    }

    fun getSocket(): Socket? {
        return mSocket
    }

    fun getEmitterListener(event: String, listener: Emitter.Listener): Emitter? {
        return getSocket()!!.on(event, listener)
    }

}