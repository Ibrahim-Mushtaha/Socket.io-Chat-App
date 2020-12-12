package com.ix.ibrahim7.socketio.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ix.ibrahim7.socketio.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var   appBarConfiguration : AppBarConfiguration
    lateinit var   navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setSupportActionBar(toolbar)

        navController = findNavController(R.id.fragment2)
        appBarConfiguration= AppBarConfiguration(setOf(
            R.id.homeFragment
        ))

        findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(navController,appBarConfiguration)


    }

}