package com.fdhasna21.githubusers.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.fdhasna21.githubusers.BuildConfig
import com.fdhasna21.githubusers.R
import com.fdhasna21.githubusers.browserIntent
import com.fdhasna21.githubusers.dataResolver.User
import com.fdhasna21.githubusers.dataResolver.getBitmapFromView
import com.fdhasna21.githubusers.dataResolver.getImageID
import com.fdhasna21.githubusers.databinding.ActivityUserDetailBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.title = ""
        supportActionBar?.elevation = 0f

        user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        val views = arrayListOf(
            binding.detailImage,           //0
            binding.detailUsername,        //1
            binding.detailName,            //2
            binding.detailCompany,         //3
            binding.detailLocation,        //4
            binding.detailRepositories,    //5
            binding.detailFollowers,       //6
            binding.detailFollowing,       //7
            binding.btnDetailGithub        //8
        )

        views.forEachIndexed { idx: Int, it: View? ->
            if (idx == 0) {
                Glide.with(this)
                    .load(getImageID((user.avatar.toString()).substringAfterLast("/"), this))
                    .circleCrop()
                    .into(binding.detailImage)
            } else {
                if (idx > 2) {
                    it?.setOnClickListener(this)
                }
                if (idx != 8) {
                    when (idx) {
                        6 -> binding.detailFollowersCount.text = user.follower.toString()
                        7 -> binding.detailFollowingCount.text = user.following.toString()
                        else -> (it as TextView).text = when (idx) {
                            1 -> user.username
                            2 -> user.name
                            3 -> user.company
                            4 -> user.location
                            5 -> getString(R.string.repo, user.repository)
                            else -> null
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_share -> {
                try {
                    val file = File(this.externalCacheDir, "${user.username}.png")
                    val fout = FileOutputStream(file)
                    getBitmapFromView(binding.detailShareable).compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        fout
                    )
                    fout.flush()
                    fout.close()
                    file.setReadable(true, false)
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    val photoUri = FileProvider.getUriForFile(
                        applicationContext,
                        BuildConfig.APPLICATION_ID + ".provider",
                        file
                    )
                    intent.putExtra(Intent.EXTRA_STREAM, photoUri)
                    intent.putExtra(
                        Intent.EXTRA_TEXT,
                        "See ${user.username} on GitHub via https://github.com/${user.username}"
                    )
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    intent.type = "image/png"
                    this.startActivity(intent)

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.detail_company -> browserIntent(
                "https://www.google.com/search?q=${
                    user.company!!.replace(
                        ' ',
                        '+'
                    )
                }", this
            )
            R.id.detail_location -> browserIntent(
                "https://www.google.com/maps?q=${
                    user.location!!.replace(
                        ' ',
                        '+'
                    )
                }", this
            )
            R.id.detail_repositories -> browserIntent(
                "https://github.com/${user.username}?tab=repositories",
                this
            )
            R.id.detail_followers -> browserIntent(
                "https://github.com/${user.username}?tab=followers",
                this
            )
            R.id.detail_following -> browserIntent(
                "https://github.com/${user.username}?tab=following",
                this
            )
            R.id.btn_detail_github -> browserIntent("https://github.com/${user.username}", this)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}