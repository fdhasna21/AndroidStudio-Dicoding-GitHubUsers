package com.fdhasna21.githubusers.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.fdhasna21.githubusers.utility.IntentUtils
import com.fdhasna21.githubusers.viewmodel.BaseViewModel

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */

abstract class BaseActivity<VB: ViewBinding, VM: BaseViewModel>(
    private val inflate: (LayoutInflater) -> VB,
    private val viewModelClass: Class<VM>
) : AppCompatActivity() {
    lateinit var binding: VB
    protected abstract val viewModel: VM

    var isConfigChange = false
    lateinit var intentUtils : IntentUtils

    open fun setupUIWhenConfigChange(){
        setupData()
    }
    open fun setupUIWithoutConfigChange(){}
    open fun setupData(){}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)

        intentUtils = IntentUtils(this)

        if(!isConfigChange){
            setupUIWhenConfigChange()
        }

        setupUIWithoutConfigChange()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        viewModel.setConfiguration(newConfig.orientation)
        viewModel.activityConfig.observe(this) {
            isConfigChange = (it != newConfig.orientation)
        }
        super.onConfigurationChanged(newConfig)
    }
}