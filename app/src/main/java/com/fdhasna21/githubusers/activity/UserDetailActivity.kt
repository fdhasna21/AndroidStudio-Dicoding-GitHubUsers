package com.fdhasna21.githubusers.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.fdhasna21.githubusers.BuildConfig
import com.fdhasna21.githubusers.DataUtils
import com.fdhasna21.githubusers.IntentData
import com.fdhasna21.githubusers.R
import com.fdhasna21.githubusers.adapter.ViewPagerAdapter
import com.fdhasna21.githubusers.databinding.ActivityUserDetailBinding
import com.fdhasna21.githubusers.dataclass.DataType
import com.fdhasna21.githubusers.dataclass.Repository
import com.fdhasna21.githubusers.dataclass.User
import com.fdhasna21.githubusers.server.ServerAPI
import com.fdhasna21.githubusers.server.ServerInterface
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var serverInterface: ServerInterface
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>
    private lateinit var username : String
    private lateinit var user: User
    private var intentData = IntentData(this)

    private var repository : ArrayList<Repository> = arrayListOf()
    private var starred : ArrayList<Repository> = arrayListOf()
    private var followers : ArrayList<User> = arrayListOf()
    private var following : ArrayList<User> = arrayListOf()

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
        binding.detailProgress.visibility = View.VISIBLE
        serverInterface.user(username).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    user = response.body()!!
                    binding.detailContent.visibility = View.VISIBLE
                    binding.detailProgress.visibility = View.INVISIBLE
                    views.forEachIndexed { idx: Int, it: View? ->
                        if (idx == 0) {
                            Glide.with(this@UserDetailActivity)
                                .load(user.photo_profile)
                                .circleCrop()
                                .into(binding.detailImage)
                        } else {
                            if (idx > 3) {
                                it?.setOnClickListener(this@UserDetailActivity)
                            }
                            (it as TextView).text = when (idx) {
                                1 -> user.username
                                2 -> user.name
                                3 -> user.bio
                                4 -> user.company
                                5 -> user.location
                                6 -> listOf(DataUtils().withSuffix(user.followers!!), getString(R.string.followers)).joinToString(" ")
                                7 -> listOf(DataUtils().withSuffix(user.following!!), getString(R.string.following)).joinToString(" ")
                                8 -> user.website
                                9 -> user.email
                                else -> null
                            }

                            if(it.text.isNullOrBlank() || it.text.isNullOrEmpty()){
                                it.visibility = View.GONE
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                //todo : kalo failure
                Toast.makeText(this@UserDetailActivity, t.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.elevation = 0f
        supportActionBar?.title = ""
    }

    private fun setupTabLayout(){
        val tabLayoutAdapter = ViewPagerAdapter(this, arrayListOf(repository, starred), DataType.REPOSITORY)
        binding.detailViewPager.adapter = tabLayoutAdapter
        TabLayoutMediator(binding.detailTabRepo, binding.detailViewPager) { tab, position ->
            tab.text = resources.getString(REPO_TAB_TITLES[position])
        }.attach()

        serverInterface.repository(username).enqueue(object : Callback<ArrayList<Repository>>{
            override fun onResponse(
                call: Call<ArrayList<Repository>>,
                response: Response<ArrayList<Repository>>
            ) {
                if(response.isSuccessful){
                    repository.addAll(response.body()!!)
                    tabLayoutAdapter.updateAdapter()
                }
            }

            override fun onFailure(call: Call<ArrayList<Repository>>, t: Throwable) {
                //todo : kalo failure
                Toast.makeText(this@UserDetailActivity, t.toString(), Toast.LENGTH_LONG).show()
            }

        })

        serverInterface.starred(username).enqueue(object : Callback<ArrayList<Repository>>{
            override fun onResponse(
                call: Call<ArrayList<Repository>>,
                response: Response<ArrayList<Repository>>
            ) {
                if(response.isSuccessful){
                    starred.addAll(response.body()!!)
                    tabLayoutAdapter.updateAdapter()
                }
            }

            override fun onFailure(call: Call<ArrayList<Repository>>, t: Throwable) {
                //todo : kalo failure
                Toast.makeText(this@UserDetailActivity, t.toString(), Toast.LENGTH_LONG).show()
            }

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

        serverInterface.followers(username).enqueue(object : Callback<ArrayList<User>>{
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if(response.isSuccessful){
                    followers.addAll(response.body()!!)
                    bottomSheetAdapter.updateAdapter()
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                //todo : kalo failure
                Toast.makeText(this@UserDetailActivity, t.toString(), Toast.LENGTH_LONG).show()
            }
        })

        serverInterface.following(username).enqueue(object : Callback<ArrayList<User>>{
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if(response.isSuccessful){
                    following.addAll(response.body()!!)
                    bottomSheetAdapter.updateAdapter()
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                //todo : kalo failure
                Toast.makeText(this@UserDetailActivity, t.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra(EXTRA_USER)!!

        val serverAPI = ServerAPI()
        serverInterface = serverAPI.getServerAPI(binding.detailProgress)!!.create(ServerInterface::class.java)
        binding.detailContent.visibility = View.INVISIBLE
        binding.detailProgress.visibility = View.VISIBLE

        setupToolbar()
        setupHeader()
        setupBottomSheet()
        setupTabLayout()
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
                    DataUtils().getBitmapFromView(binding.detailShareable).compress(
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
            R.id.menu_open_github -> intentData.openBrowser("https://github.com/${user.username}")
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.detail_company -> intentData.openBrowser(
                "https://www.google.com/search?q=${
                    user.company!!.replace(
                        ' ',
                        '+'
                    )
                }"
            )
            R.id.detail_location -> intentData.openBrowser(
                "https://www.google.com/maps?q=${
                    user.location!!.replace(
                        ' ',
                        '+'
                    )
                }"
            )
            R.id.detail_followers -> isBottomSheetExpanded(true, R.string.followers)
            R.id.detail_following -> isBottomSheetExpanded(true, R.string.following)
            R.id.detail_website   -> intentData.openBrowser(user.website.toString())
            R.id.detail_email     -> intentData.openEmail(user.email.toString())
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