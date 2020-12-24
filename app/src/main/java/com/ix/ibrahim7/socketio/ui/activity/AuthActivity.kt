package com.ix.ibrahim7.socketio.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.util.Constant
import com.ix.ibrahim7.socketio.util.Constant.setUpStatusBar


class AuthActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setUpStatusBar(this, 2)

    }

}
