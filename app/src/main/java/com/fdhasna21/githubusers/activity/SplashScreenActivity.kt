package com.fdhasna21.githubusers.activity

import android.content.Intent
import com.fdhasna21.githubusers.databinding.ActivitySplashScreenBinding
import com.fdhasna21.githubusers.viewmodel.GeneralActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */

class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding, GeneralActivityViewModel>(
    ActivitySplashScreenBinding::inflate,
    GeneralActivityViewModel::class.java
) {

    override val viewModel: GeneralActivityViewModel by viewModel()
    override fun setupUIWithoutConfigChange() {
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