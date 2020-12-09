package com.ix.ibrahim7.socketio.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import com.ix.ibrahim7.socketio.R
import com.ix.ibrahim7.socketio.databinding.ActivitySplashBinding
import com.ix.ibrahim7.socketio.util.Constant
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    lateinit var mBinding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        overridePendingTransition(
            R.anim.fade_in,
            R.anim.fade_out
        )

        imgSplashLogo.startAnimation(
            AnimationUtils.loadAnimation(this,
                R.anim.splash_in
            ))

        Handler().postDelayed({
            imgSplashLogo.startAnimation(
                AnimationUtils.loadAnimation(this,
                    R.anim.splash_out
                ))
            Handler().postDelayed({
                imgSplashLogo.visibility= View.GONE
                if (Constant.getSharePref(this).getBoolean(Constant.START,false)){
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    })
                }else{
                    startActivity(Intent(this, AuthActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    })
                }
            },500)
        },900)


    }





}