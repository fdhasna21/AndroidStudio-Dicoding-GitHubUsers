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

    private var _isLoading : MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading : LiveData<Boolean> get() = _isLoading
    fun startLoading(){
        _isLoading.value = true
    }

    fun endLoading(){
        _isLoading.value = false
    }
}