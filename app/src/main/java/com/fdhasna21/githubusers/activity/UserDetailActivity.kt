package com.fdhasna21.githubusers.activity

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fdhasna21.githubusers.BuildConfig
import com.fdhasna21.githubusers.resolver.DataUtils
import com.fdhasna21.githubusers.resolver.IntentData
import com.fdhasna21.githubusers.R
import com.fdhasna21.githubusers.activity.viewmodel.UserDetailActivityViewModel
import com.fdhasna21.githubusers.adapter.ViewPagerAdapter
import com.fdhasna21.githubusers.databinding.ActivityUserDetailBinding
import com.fdhasna21.githubusers.resolver.enumclass.DataType
import com.fdhasna21.githubusers.resolver.dataclass.Repository
import com.fdhasna21.githubusers.resolver.dataclass.User
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var viewModel: UserDetailActivityViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>
    private var intentData = IntentData(this)
    private var isConfigChange = false
    private var repository : ArrayList<Repository> = arrayListOf()
    private var starred : ArrayList<Repository> = arrayListOf()
    private var followers : ArrayList<User> = arrayListOf()
    private var following : ArrayList<User> = arrayListOf()

    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.elevation = 0f
        supportActionBar?.title = ""
    }

    private fun setupHeader(){
        val views = arrayListOf(
            binding.detailImage,           //0
            binding.detailUsername,        //1
            binding.detailName,            //2
            binding.detailBio,             //3
            binding.detailCompany,         //4
            binding.detailLocation,        //5
            binding.detailFollowers,       //6
            binding.detailFollowing,       //7
            binding.detailWebsite,         //8
            binding.detailEmail            //9
        )
        binding.detailResponse.progressCircular.visibility = View.VISIBLE
        viewModel.detailList.observe(this, { output ->
            binding.detailResponse.progressCircular.visibility = View.INVISIBLE
            if(output != null){
                binding.detailViewPager.visibility = View.VISIBLE
                binding.detailContent.visibility = View.VISIBLE
                setupTabLayout()
                setupBottomSheet()
                views.forEachIndexed { idx: Int, it: View? ->
                    if (idx == 0) {
                        Glide.with(this@UserDetailActivity)
                            .load(output.photo_profile)
                            .circleCrop()
                            .into(binding.detailImage)
                    } else {
                        if (idx > 3) {
                            it?.setOnClickListener(this@UserDetailActivity)
                        }
                        (it as TextView).text = when (idx) {
                            1 -> output.username
                            2 -> output.name
                            3 -> output.bio
                            4 -> output.company
                            5 -> output.location
                            6 -> listOf(DataUtils().withSuffix(output.followers!!), getString(R.string.followers)).joinToString(" ")
                            7 -> listOf(DataUtils().withSuffix(output.following!!), getString(R.string.following)).joinToString(" ")
                            8 -> output.website
                            9 -> output.email
                            else -> null
                        }

                        if(it.text.isNullOrBlank() || it.text.isNullOrEmpty()){
                            it.visibility = View.GONE
                        }
                    }
                }
            } else {
                val error : ArrayList<Int> = viewModel.errorDetail.type?.setError(this@UserDetailActivity)!!
                binding.detailResponse.layoutError.visibility = View.VISIBLE
                binding.detailResponse.errorImage.setImageDrawable(AppCompatResources.getDrawable(this, error[0]))
                binding.detailResponse.errorMessage.text = listOf(getString(error[1]), viewModel.errorDetail.code).joinToString(". Code:")
            }
        })
    }

    private fun setupTabLayout(){
        val tabLayoutAdapter = ViewPagerAdapter(this, arrayListOf(repository, starred), DataType.REPOSITORY)
        binding.detailViewPager.adapter = tabLayoutAdapter
        TabLayoutMediator(binding.detailTabRepo, binding.detailViewPager) { tab, position ->
            tab.text = resources.getString(REPO_TAB_TITLES[position])
        }.attach()

        viewModel.getRepositoryData()
        viewModel.repositoryList.observe(this@UserDetailActivity, {
            if(it == null || it.isEmpty()){
                val error : ArrayList<Int> = viewModel.errorRepo.type?.setError(this@UserDetailActivity)!!
                tabLayoutAdapter.setFragmentError(0, true,
                    drawableID = error[0],
                    messageID = error[1],
                    code = viewModel.errorRepo.code)
            } else {
                repository.addAll(it)
                tabLayoutAdapter.updateAdapter(0)
                tabLayoutAdapter.setFragmentError(0, false)
            }
        })

        viewModel.starredList.observe(this@UserDetailActivity, {
            if(it == null || it.isEmpty()){
                Log.i("userDetailActivity", "setupTabLayout: ${viewModel.errorStarred}")
                val error : ArrayList<Int> = viewModel.errorStarred.type?.setError(this@UserDetailActivity)!!
                tabLayoutAdapter.setFragmentError(1, true,
                    drawableID = error[0],
                    messageID = error[1],
                    code = viewModel.errorStarred.code)
            } else {
                starred.addAll(it)
                tabLayoutAdapter.updateAdapter(1)
                tabLayoutAdapter.setFragmentError(1, false)
            }
        })

        binding.detailTabRepo.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0 -> viewModel.getRepositoryData()
                    1 -> viewModel.getStarredData()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupBottomSheet(){
        bottomSheetBehavior = BottomSheetBehavior.from(binding.detailBottomSheet.followLayout).apply {
            peekHeight = 0
            state = BottomSheetBehavior.STATE_COLLAPSED
            isDraggable = false
        }

        val bottomSheetAdapter = ViewPagerAdapter(this, arrayListOf(followers, following), DataType.USER)
        binding.detailBottomSheet.followViewPager.adapter = bottomSheetAdapter
        TabLayoutMediator(binding.detailBottomSheet.followTabLayout, binding.detailBottomSheet.followViewPager) { tab, position ->
            tab.text = resources.getString(FOLLOW_TAB_TITLES[position])
        }.attach()

        viewModel.getFollowersData()
        viewModel.followersList.observe(this@UserDetailActivity, {
            if(it == null || it.isEmpty()){
                val error : ArrayList<Int> = viewModel.errorFollowers.type?.setError(this@UserDetailActivity)!!
                bottomSheetAdapter.setFragmentError(0, true,
                    drawableID = error[0],
                    messageID = error[1],
                    code = viewModel.errorFollowers.code)
            } else {
                followers.addAll(it)
                bottomSheetAdapter.updateAdapter(0)
                bottomSheetAdapter.setFragmentError(0, false)
            }
        })

        viewModel.followingList.observe(this@UserDetailActivity, {
            if(it == null || it.isEmpty()){
                val error : ArrayList<Int> = viewModel.errorFollowing.type?.setError(this@UserDetailActivity)!!
                bottomSheetAdapter.setFragmentError(1, true,
                    drawableID = error[0],
                    messageID = error[1],
                    code = viewModel.errorFollowing.code)
            } else {
                following.addAll(it)
                bottomSheetAdapter.updateAdapter(1)
                bottomSheetAdapter.setFragmentError(1, false)
            }
        })

        binding.detailBottomSheet.followTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0 -> viewModel.getFollowersData()
                    1 -> viewModel.getFollowingData()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(UserDetailActivityViewModel::class.java)
        if(!isConfigChange){
            viewModel.username = intent.getStringExtra(EXTRA_USER)!!
            binding.detailViewPager.visibility = View.INVISIBLE
            binding.detailContent.visibility = View.INVISIBLE
            binding.detailResponse.progressCircular.visibility = View.VISIBLE
            setupToolbar()
            setupHeader()
            viewModel.getDetailData()
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
                    val file = File(this.externalCacheDir, "${viewModel.username}.png")
                    val fileOutput = FileOutputStream(file)
                    DataUtils().getBitmapFromView(binding.detailShareable).compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        fileOutput
                    )
                    fileOutput.flush()
                    fileOutput.close()
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
                        "See ${viewModel.username} on GitHub via https://github.com/${viewModel.username}"
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
            R.id.menu_open_github -> intentData.openBrowser("https://github.com/${viewModel.username}")
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        viewModel.setConfiguration(newConfig.orientation)
        viewModel.getConfiguration().observe(this, {
            isConfigChange = (it != newConfig.orientation)
        })
        super.onConfigurationChanged(newConfig)
    }

    override fun onClick(v: View?) {
        viewModel.detailList.observe(this, {
            if(it != null){
                when (v?.id) {
                    R.id.detail_company -> intentData.openBrowser(
                        "https://www.google.com/search?q=${
                            it.company!!.replace(
                                ' ',
                                '+'
                            )
                        }"
                    )
                    R.id.detail_location -> intentData.openBrowser(
                        "https://www.google.com/maps?q=${
                            it.location!!.replace(
                                ' ',
                                '+'
                            )
                        }"
                    )
                    R.id.detail_followers -> isBottomSheetExpanded(true, R.string.followers)
                    R.id.detail_following -> isBottomSheetExpanded(true, R.string.following)
                    R.id.detail_website   -> intentData.openBrowser(it.website.toString())
                    R.id.detail_email     -> intentData.openEmail(it.email.toString())
                }
            }
        })
    }

    private fun isBottomSheetExpanded(state:Boolean, stringResource:Int=0){
        when(state){
            true -> {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                binding.detailContent.visibility = View.INVISIBLE
                binding.detailViewPager.visibility = View.INVISIBLE
                binding.detailBottomSheet.followViewPager.currentItem = FOLLOW_TAB_TITLES.indexOf(stringResource)
                binding.detailBottomSheet.followTabLayout.getTabAt(
                    if(stringResource == R.string.following) 1
                    else 0
                )?.select()
            }
            false -> {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
                binding.detailContent.visibility = View.VISIBLE
                binding.detailViewPager.visibility = View.VISIBLE
                binding.detailContent.setExpanded(true)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        val REPO_TAB_TITLES = intArrayOf(
            R.string.repositories,
            R.string.starred
        )
        val FOLLOW_TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}