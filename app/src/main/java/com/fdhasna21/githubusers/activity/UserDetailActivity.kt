package com.fdhasna21.githubusers.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.fdhasna21.githubusers.BuildConfig
import com.fdhasna21.githubusers.R
import com.fdhasna21.githubusers.adapter.ViewPagerAdapter
import com.fdhasna21.githubusers.browserIntent
import com.fdhasna21.githubusers.dataclass.Account
import com.fdhasna21.githubusers.dataResolver.getBitmapFromView
import com.fdhasna21.githubusers.dataResolver.getImageID
import com.fdhasna21.githubusers.databinding.ActivityUserDetailBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import com.google.android.material.card.MaterialCardView


class UserDetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var account: Account
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObject()
        setupBottomSheet()
        setupToolbar()
        setupTabLayout()
    }

    private fun setupObject(){
        account = intent.getParcelableExtra<Account>(EXTRA_USER) as Account

        val views = arrayListOf(
            binding.detailImage,           //0
            binding.detailUsername,        //1
            binding.detailName,            //2
            binding.detailCompany,         //3
            binding.detailLocation,        //4
            binding.detailFollowers,       //5
            binding.detailFollowing        //6
        )

        views.forEachIndexed { idx: Int, it: View? ->
            if (idx == 0) {
                Glide.with(this)
                    .load(getImageID((account.avatar.toString()).substringAfterLast("/"), this))
                    .circleCrop()
                    .into(binding.detailImage)
            } else {
                if (idx > 2) {
                    it?.setOnClickListener(this)
                }
                if (idx != 8) {
                    (it as TextView).text = when (idx) {
                        1 -> account.username
                        2 -> account.name
                        3 -> account.company
                        4 -> account.location
                        5 -> listOf(account.follower, getString(R.string.followers)).joinToString(" ")
                        6 -> listOf(account.following, getString(R.string.following)).joinToString(" ")
                        else -> null
                    }
                }
            }
        }
    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.elevation = 0f
        supportActionBar?.title = ""

        var scrollRange = -1
        binding.detailContent.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if(scrollRange == -1){
                scrollRange = appBarLayout.totalScrollRange
            }
            if((scrollRange + verticalOffset == 0)){
                supportActionBar?.title = account.name
                supportActionBar?.subtitle = account.username
            }
            else {
                supportActionBar?.title = ""
                supportActionBar?.subtitle = ""
            }
        })
    }

    private fun setupTabLayout(){
        binding.detailViewPager.adapter = ViewPagerAdapter(this, 2)
        TabLayoutMediator(binding.detailTabRepo, binding.detailViewPager) { tab, position ->
            tab.text = resources.getString(REPO_TAB_TITLES[position])
        }.attach()
    }

    private fun setupBottomSheet(){
        bottomSheetBehavior = BottomSheetBehavior.from(binding.detailBottomSheet.followLayout).apply {
            peekHeight = 0
            state = BottomSheetBehavior.STATE_COLLAPSED
            isDraggable = false
        }
        binding.detailBottomSheet.followViewPager.adapter = ViewPagerAdapter(this, 2)
        TabLayoutMediator(binding.detailBottomSheet.followTabLayout, binding.detailBottomSheet.followViewPager) { tab, position ->
            tab.text = resources.getString(FOLLOW_TAB_TITLES[position])
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_share -> {
                try {
                    val file = File(this.externalCacheDir, "${account.username}.png")
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
                        "See ${account.username} on GitHub via https://github.com/${account.username}"
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
            R.id.menu_open_github -> browserIntent("https://github.com/${account.username}", this)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        if(bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED){
            isBottomSheetExpanded(false)
        }else{
            onBackPressed()
        }
        return true
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.detail_company -> browserIntent(
                "https://www.google.com/search?q=${
                    account.company!!.replace(
                        ' ',
                        '+'
                    )
                }", this
            )
            R.id.detail_location -> browserIntent(
                "https://www.google.com/maps?q=${
                    account.location!!.replace(
                        ' ',
                        '+'
                    )
                }", this
            )
            R.id.detail_followers -> isBottomSheetExpanded(true, R.string.followers)
            R.id.detail_following -> isBottomSheetExpanded(true, R.string.following)

        }
    }

    private fun isBottomSheetExpanded(state:Boolean, stringResource:Int=0){
        when(state){
            true -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                binding.detailContent.visibility = View.INVISIBLE
                binding.detailViewPager.visibility = View.INVISIBLE
                binding.detailBottomSheet.followViewPager.currentItem = FOLLOW_TAB_TITLES.indexOf(stringResource)
            }
            false -> {
                binding.detailContent.visibility = View.VISIBLE
                binding.detailViewPager.visibility = View.VISIBLE
                binding.detailContent.setExpanded(true)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        private val REPO_TAB_TITLES = intArrayOf(
            R.string.repositories,
            R.string.starred
        )
        private val FOLLOW_TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}