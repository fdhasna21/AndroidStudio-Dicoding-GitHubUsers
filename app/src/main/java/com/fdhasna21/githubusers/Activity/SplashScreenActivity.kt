package com.fdhasna21.githubusers.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fdhasna21.githubusers.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val timer = object : Thread() {
            override fun run() {
                try {
                    synchronized(this) { sleep(5000) }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        timer.start()
    }
}