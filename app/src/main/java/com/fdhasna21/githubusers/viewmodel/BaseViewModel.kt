package com.fdhasna21.githubusers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */

open class BaseViewModel : ViewModel() {
    private var _activityConfig : MutableLiveData<Int> = MutableLiveData()
    val activityConfig : LiveData<Int> get() = _activityConfig
    fun setConfiguration(config : Int){
        _activityConfig.value = config
    }
}