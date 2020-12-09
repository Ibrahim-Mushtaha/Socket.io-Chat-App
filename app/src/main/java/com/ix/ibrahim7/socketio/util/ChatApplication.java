package com.ix.ibrahim7.socketio.util;

import android.app.Application;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class ChatApplication extends Application {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.0.102:4000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }


}
