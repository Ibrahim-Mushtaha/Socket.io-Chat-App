package com.ix.ibrahim7.socketio.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.nkzawa.socketio.client.Socket
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.databinding.ActivityMainBinding
import com.ix.ibrahim7.socketio.util.Constant
import com.ix.ibrahim7.socketio.util.Constant.ID
import com.ix.ibrahim7.socketio.util.Constant.ISONLINE
import com.ix.ibrahim7.socketio.util.Constant.UPDATEUSER
import com.ix.ibrahim7.socketio.util.Constant.setUpStatusBar
import com.ix.ibrahim7.socketio.network.SocketManager
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController
    private var mSocket: Socket? = null


    override fun onStart() {
        super.onStart()
        mSocket!!.emit(UPDATEUSER, JSONObject().apply {
            put(ID, Constant.getUser(this@MainActivity).id)
            put(ISONLINE, true)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SocketManager().apply {
            mSocket = getSocket()
        }

        setUpStatusBar(this, 2)
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.fragment2)
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.mainFragment
        ))

        setupActionBarWithNavController(navController, appBarConfiguration)

        findViewById<Toolbar>(R.id.toolbar)
                .setupWithNavController(navController, appBarConfiguration)

    }

    override fun onDestroy() {
        if (mSocket != null)
            mSocket!!.emit(UPDATEUSER, JSONObject().apply {
                put(ID, Constant.getUser(this@MainActivity).id)
                put(ISONLINE, false)
            })
        super.onDestroy()
    }

}