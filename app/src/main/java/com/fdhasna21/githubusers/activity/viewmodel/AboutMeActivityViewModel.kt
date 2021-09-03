package com.fdhasna21.githubusers.activity.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AboutMeActivityViewModel : ViewModel() {
    private var activityConfig : MutableLiveData<Int> = MutableLiveData()

    fun getConfiguration() : MutableLiveData<Int> {
        return activityConfig
    }

    fun setConfiguration(config : Int){
        activityConfig.value = config
    }
}