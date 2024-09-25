package com.fdhasna21.githubusers.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.fdhasna21.githubusers.BuildConfig
import com.fdhasna21.githubusers.R
import com.fdhasna21.githubusers.adapter.ViewPagerAdapter
import com.fdhasna21.githubusers.adapter.observeData
import com.fdhasna21.githubusers.databinding.ActivityUserDetailBinding
import com.fdhasna21.githubusers.model.response.RepoResponse
import com.fdhasna21.githubusers.model.response.UserResponse
import com.fdhasna21.githubusers.repository.UserRepositoryImp
import com.fdhasna21.githubusers.utility.DataUtils
import com.fdhasna21.githubusers.utility.Key
import com.fdhasna21.githubusers.utility.type.DataType
import com.fdhasna21.githubusers.utility.type.dpToPx
import com.fdhasna21.githubusers.viewmodel.UserDetailActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */

class UserDetailActivity : BaseActivity<ActivityUserDetailBinding, UserDetailActivityViewModel, UserRepositoryImp>(
    ActivityUserDetailBinding::inflate,
    UserDetailActivityViewModel::class.java
) {
    override val viewModel: UserDetailActivityViewModel by viewModel()
    override val repository: UserRepositoryImp by inject()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>
    override fun setupData() {
        intent.getStringExtra(Key.INTENT.USERNAME)?.let {
            viewModel.setUsername(it)
            viewModel.getReposFromRepository()
            viewModel.getFollowersFromRepository()
        }
    }

    override fun setupUIWhenConfigChange() {
        setupToolbar()
        setupHeader()
        setupTabLayout()
        setupBottomSheet()
        binding.apply {
            detailViewPager.visibility = View.INVISIBLE
            detailContent.visibility = View.INVISIBLE
            detailResponse.progressCircular.visibility = View.VISIBLE
        }
        super.setupUIWhenConfigChange()
    }

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
        viewModel.userDetail.observe(this){
            binding.detailResponse.progressCircular.visibility = View.INVISIBLE
            if(it == null){
//                val error : ArrayList<Int> = viewModel.errorDetail.type?.setError(this@UserDetailActivity)!!
//                binding.detailResponse.layoutError.visibility = View.VISIBLE
//                binding.detailResponse.errorImage.setImageDrawable(AppCompatResources.getDrawable(this, error[0]))
//                binding.detailResponse.errorMessage.text = listOf(getString(error[1]), viewModel.errorDetail.code).joinToString(". Code:")
            } else {
                binding.detailViewPager.visibility = View.VISIBLE
                binding.detailContent.visibility = View.VISIBLE
                views.forEachIndexed { idx: Int, view: View ->
                    if (idx == 0) {
                        Glide.with(this@UserDetailActivity)
                            .load(it.photo_profile)
                            .circleCrop()
                            .into(binding.detailImage)
                    } else {
                        if (idx > 3) {
                            view.setOnClickListener { detailView ->
                                when (detailView?.id) {
                                    R.id.detail_company -> intentUtils.openBrowser(
                                        "https://www.google.com/search?q=${
                                            it.company!!.replace(
                                                ' ',
                                                '+'
                                            )
                                        }"
                                    )
                                    R.id.detail_location -> intentUtils.openBrowser(
                                        "https://www.google.com/maps?q=${
                                            it.location!!.replace(
                                                ' ',
                                                '+'
                                            )
                                        }"
                                    )
                                    R.id.detail_followers -> isBottomSheetExpanded(true, R.string.followers)
                                    R.id.detail_following -> isBottomSheetExpanded(true, R.string.following)
                                    R.id.detail_website   -> intentUtils.openBrowser(it.website.toString())
                                    R.id.detail_email     -> intentUtils.openEmail(it.email.toString())
                                }
                            }
                        }
                        (view as TextView).text = when (idx) {
                            1 -> it.username
                            2 -> it.name
                            3 -> it.bio
                            4 -> it.company
                            5 -> it.location
                            6 -> listOf(
                                DataUtils().withSuffix(it.followers!!),
                                getString(R.string.followers)
                            ).joinToString(" ")

                            7 -> listOf(
                                DataUtils().withSuffix(it.following!!),
                                getString(R.string.following)
                            ).joinToString(" ")

                            8 -> it.website
                            9 -> it.email
                            else -> null
                        }

                        if (view.text.isNullOrBlank() || view.text.isNullOrEmpty()) {
                            view.visibility = View.GONE
                        }
                    }


                }
            }
        }
    }

    private fun setupTabLayout(){
        val tabLayoutAdapter = ViewPagerAdapter(this, arrayListOf(arrayListOf<RepoResponse>(), arrayListOf<RepoResponse>()), DataType.REPOSITORY)
        binding.detailViewPager.adapter = tabLayoutAdapter
        TabLayoutMediator(binding.detailTabRepo, binding.detailViewPager) { tab, position ->
            tab.text = resources.getString(REPO_TAB_TITLES[position])
        }.attach()

        binding.detailTabRepo.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0 -> viewModel.getReposFromRepository()
                    1 -> viewModel.getStarsFromRepository()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        tabLayoutAdapter.observeData(this, viewModel.allRepos, 0){
//            val error : ArrayList<Int> = viewModel.errorRepo.type?.setError(this@UserDetailActivity)!!
//            tabLayoutAdapter.setFragmentError(0, true,
//                drawableID = error[0],
//                messageID = error[1],
//                code = viewModel.errorRepo.code)
        }

        tabLayoutAdapter.observeData(this, viewModel.allStars, 1) {
//            val error : ArrayList<Int> = viewModel.errorStarred.type?.setError(this@UserDetailActivity)!!
//            tabLayoutAdapter.setFragmentError(1, true,
//                drawableID = error[0],
//                messageID = error[1],
//                code = viewModel.errorStarred.code)
        }
    }

    private fun setupBottomSheet(){
        bottomSheetBehavior = BottomSheetBehavior.from(binding.detailBottomSheet.followLayout).apply {
            peekHeight = 0
            state = BottomSheetBehavior.STATE_COLLAPSED
            isDraggable = false
        }

        val bottomSheetAdapter = ViewPagerAdapter(this, arrayListOf(arrayListOf<UserResponse>(), arrayListOf<UserResponse>()), DataType.USER)
        binding.detailBottomSheet.followViewPager.adapter = bottomSheetAdapter
        TabLayoutMediator(binding.detailBottomSheet.followTabLayout, binding.detailBottomSheet.followViewPager) { tab, position ->
            tab.text = resources.getString(FOLLOW_TAB_TITLES[position])
        }.attach()

        binding.detailBottomSheet.followTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0 -> viewModel.getFollowersFromRepository()
                    1 -> viewModel.getFollowingsFromRepository()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        bottomSheetAdapter.observeData(this, viewModel.allFollowers, 0){
//            val error: ArrayList<Int> = viewModel.errorFollowers.type?.setError(this@UserDetailActivity)!!
//            bottomSheetAdapter.setFragmentError(
//                0, true,
//                drawableID = error[0],
//                messageID = error[1],
//                code = viewModel.errorFollowers.code
//            )
        }

        bottomSheetAdapter.observeData(this, viewModel.allFollowings, 1){
//            val error: ArrayList<Int> = viewModel.errorFollowing.type?.setError(this@UserDetailActivity)!!
//            bottomSheetAdapter.setFragmentError(
//                1, true,
//                drawableID = error[0],
//                messageID = error[1],
//                code = viewModel.errorFollowing.code
//            )
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
                    val temporaryLayout = LinearLayout(this).apply {
                        orientation = LinearLayout.VERTICAL
                        setPadding(0, 12.dpToPx(), 0, 0)
                    }
                    temporaryLayout.apply {
                        val parent = binding.detailShareable.parent as? ViewGroup
                        parent?.removeView(binding.detailShareable)
                        addView(binding.detailShareable)
                        measure(
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                        )
                        layout(0, 0, measuredWidth, measuredHeight)
                    }
                    val bitmap = Bitmap.createBitmap(temporaryLayout.measuredWidth, temporaryLayout.measuredHeight, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    temporaryLayout.draw(canvas)

                    val file = File(this.externalCacheDir, "${viewModel.username.value}.png")
                    val fileOutput = FileOutputStream(file)

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutput)
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
                        "See ${viewModel.username.value} on GitHub via https://github.com/${viewModel.username.value}"
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
            R.id.menu_open_github -> intentUtils.openBrowser("https://github.com/${viewModel.username.value}")
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