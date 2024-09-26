package com.fdhasna21.githubusers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fdhasna21.githubusers.model.entity.toUserResponse
import com.fdhasna21.githubusers.model.response.UserResponse
import com.fdhasna21.githubusers.repository.HistoryRepositoryImp
import com.fdhasna21.githubusers.utility.type.convertToArrayList
import kotlinx.coroutines.launch

/**
 * Created by Fernanda Hasna on 26/09/2024.
 */

class HistoryViewModel(private val repository: HistoryRepositoryImp) : BaseViewModel(){
    private var _allHistories : MutableLiveData<ArrayList<UserResponse>> =
        MutableLiveData<ArrayList<UserResponse>>(arrayListOf())
    val allHistories : LiveData<ArrayList<UserResponse>> get() = _allHistories

    fun getAllHistoriesFromRepository(){
        viewModelScope.launch {
            repository.getAllHistories(
                onSuccess = { histories ->
                    val userResponses = histories.convertToArrayList { it.toUserResponse() }
                    _allHistories.value = userResponses
                }
            )
        }
    }

    fun deleteAllHistories(){
        viewModelScope.launch {
            repository.deleteAllHistories {
                getAllHistoriesFromRepository()
            }
        }
    }
}