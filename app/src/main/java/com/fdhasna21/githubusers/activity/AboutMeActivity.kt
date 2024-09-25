package com.fdhasna21.githubusers.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.bumptech.glide.Glide
import com.fdhasna21.githubusers.BuildConfig
import com.fdhasna21.githubusers.R
import com.fdhasna21.githubusers.databinding.ActivityAboutMeBinding
import com.fdhasna21.githubusers.repository.GeneralRepositoryImp
import com.fdhasna21.githubusers.utility.Key
import com.fdhasna21.githubusers.viewmodel.GeneralActivityViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */

class AboutMeActivity : BaseActivity<ActivityAboutMeBinding, GeneralActivityViewModel, GeneralRepositoryImp>(
    ActivityAboutMeBinding::inflate,
    GeneralActivityViewModel::class.java
) {

    override val viewModel: GeneralActivityViewModel by viewModel()
    override val repository: GeneralRepositoryImp by inject()

    override fun setupUIWhenConfigChange() {
        setupToolbar()
        setupHeader()
        setupFindMe()
        setupCredit()
        super.setupUIWhenConfigChange()
    }

    private fun setupToolbar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.title = getString(R.string.about_me)
    }

    private fun setupHeader(){
        binding.aboutEmail.text = BuildConfig.CREATOR_EMAIL
        binding.aboutDescription.text = listOf(getString(R.string.tab), getString(R.string.profile_description)).joinToString(" ")
        Glide.with(this)
            .load(R.drawable.profile_photo)
            .circleCrop()
            .into(binding.profilePicture)
    }

    private fun setupFindMe(){
        binding.aboutFindMe.itemIconTintList = null
        binding.aboutFindMe.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.about_whatsapp -> {
                    val url = "https://api.whatsapp.com/send?phone=${BuildConfig.CREATOR_WHATSAPP}"
                    try {
                        applicationContext.packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                        applicationContext.startActivity(Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(url)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    } catch (e: PackageManager.NameNotFoundException) {
                        applicationContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    }
                    true }
//                R.id.about_dicoding -> {
//                    intentUtils.openBrowser(BuildConfig.CREATOR_GITHUB)
//                    true }
                R.id.about_email ->{
                    intentUtils.openEmail(BuildConfig.CREATOR_EMAIL)
                    true }
                R.id.about_github -> {
                    val intent = Intent(this, UserDetailActivity::class.java)
                    intent.putExtra(Key.INTENT.USERNAME, "fdhasna21")
                    startActivity(intent)
                    true }
                else -> false
            }
        }
    }

    private fun setupCredit() {
        binding.aboutCredit.itemIconTintList = null
        binding.aboutCredit.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.about_github_content -> intentUtils.openBrowser("https://docs.github.com/en/rest/guides/getting-started-with-the-rest-api")
                R.id.about_lottie -> intentUtils.openBrowser("https://lottiefiles.com/6637-github-logo")
                R.id.about_pixeltrue -> intentUtils.openBrowser("https://www.pixeltrue.com/free-packs/error-state")
                R.id.about_freepik -> intentUtils.openBrowser("https://www.freepik.com/")
            }
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}