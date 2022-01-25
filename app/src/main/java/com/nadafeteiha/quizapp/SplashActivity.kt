package com.nadafeteiha.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.nadafeteiha.quizapp.data.User
import com.nadafeteiha.quizapp.databinding.ActivitySplashBinding
import java.util.zip.Inflater
import com.nadafeteiha.quizapp.utility.Constant
import com.nadafeteiha.quizapp.utility.Constant.username
import com.nadafeteiha.quizapp.utility.MyCache


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            getFromPreferences()
            moveToNext()
        }
    }

    private fun getFromPreferences(){
        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", android.content.Context.MODE_PRIVATE)
        User.userName = sharedPreference.getString(Constant.username,"")!!
        User.profileImg = sharedPreference.getString(Constant.imgProfile,"")!!

    }

    private fun moveToNext(){
        val intent = if (User.userName.isEmpty()){
            Intent(this, LoginActivity::class.java)
        }else{
            Intent(this, MainActivity::class.java)
        }
        startActivity(intent)
        this@SplashActivity.finish()
    }
}