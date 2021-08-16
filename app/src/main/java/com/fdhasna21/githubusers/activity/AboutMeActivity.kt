package com.fdhasna21.githubusers.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import android.content.pm.PackageManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.fdhasna21.githubusers.R
import com.fdhasna21.githubusers.browserIntent
import com.fdhasna21.githubusers.databinding.ActivityAboutMeBinding


class AboutMeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutMeBinding
    private lateinit var findMeMenu : NavigationView
    private lateinit var creditMenu : NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutMeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.title = getString(R.string.about_me)

        binding.aboutDescription.text = getString(R.string.tab)+getString(R.string.profile_description)

        Glide.with(this)
            .load(R.drawable.profile_photo)
            .circleCrop()
            .into(binding.profilePicture)

        findMeMenu = findViewById(R.id.about_find_me)
        findMeMenu.itemIconTintList = null
        findMeMenu.setNavigationItemSelectedListener {
            val intent = Intent()
            when(it.itemId) {
                R.id.about_whatsapp -> {
                    try{
                        applicationContext.packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA)
                        intent.action = Intent.ACTION_VIEW
                        intent.type = "text/plain"
                        intent.data = Uri.parse("https://wa.me/6281212719895")
                        intent.setPackage("com.whatsapp")
                        startActivity(intent)
                    } catch (e : PackageManager.NameNotFoundException) {
                        Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
                    }
                    true }
                R.id.about_dicoding -> {
                    browserIntent("https://www.dicoding.com/users/fernandahasna", this)
                    true }
                R.id.about_email ->{
                    intent.action = Intent.ACTION_SENDTO
                    intent.type = "message/rfc822"
                    intent.data = Uri.parse("mailto:fernanda.daymara.hasna@gmail.com")
                    startActivity(Intent.createChooser(intent, "Send email with"))
                    true }
                R.id.about_github -> {
                    browserIntent("https://github.com/fdhasna21", this)
                    true }
                else -> false
            }
        }

        creditMenu = findViewById(R.id.about_credit)
        creditMenu.itemIconTintList = null
        creditMenu.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.about_dicoding_content -> browserIntent("https://www.dicoding.com/academies/14", this)
                R.id.about_lottie -> browserIntent("https://lottiefiles.com/6637-github-logo", this)
                R.id.about_freepik -> browserIntent("https://www.freepik.com/", this)
            }
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}