package com.fdhasna21.githubusers

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_about_me.*
import java.lang.String
import android.content.pm.PackageManager

import android.content.pm.PackageInfo
import android.view.View
import android.widget.Toast


class AboutMeActivity : AppCompatActivity() {
    private lateinit var findMeMenu : NavigationView
    private lateinit var creditMenu : NavigationView

    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.title = "About Me"

        about_description.text = getString(R.string.tab)+getString(R.string.profile_description)

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
                        Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                    }
                    true }
                R.id.about_dicoding -> {
                    intent.addCategory(Intent.CATEGORY_BROWSABLE)
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse(String.format("https://www.dicoding.com/users/fernandahasna"))
                    startActivity(intent)
                    true }
                R.id.about_email ->{
                    intent.action = Intent.ACTION_SENDTO
                    intent.type = "message/rfc822"
                    intent.data = Uri.parse("mailto:fernanda.daymara.hasna@gmail.com")
                    startActivity(Intent.createChooser(intent, "Send email with"))
                    true }
                R.id.about_github -> {
                    intent.addCategory(Intent.CATEGORY_BROWSABLE)
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse(String.format("https://github.com/fdhasna21"))
                    startActivity(intent)
                    true }
                else -> false
            }
        }

        creditMenu = findViewById(R.id.about_credit)
        creditMenu.itemIconTintList = null
        creditMenu.setNavigationItemSelectedListener {
            val intent = Intent()
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.action = Intent.ACTION_VIEW
            when (it.itemId) {
//                R.id.detail_kpopwiki -> {
//                    intent.data = Uri.parse(String.format("https://kpop.fandom.com/wiki/Main_Page"))
//                }
                R.id.about_lottie -> {
                    intent.data = Uri.parse(String.format("https://lottiefiles.com/10008-music-note-character"))
                }
                R.id.about_freepik -> {
                    intent.data = Uri.parse(String.format("https://www.freepik.com/"))
                }
                else -> false
            }
            startActivity(intent)
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}