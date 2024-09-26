package com.fdhasna21.githubusers.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fdhasna21.githubusers.model.entity.toUserResponse
import com.fdhasna21.githubusers.model.response.UserResponse
import com.fdhasna21.githubusers.repository.HistoryRepositoryImp
import com.fdhasna21.githubusers.utility.type.TAG
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

    fun deleteAllHistoriesFromRepository(){
        viewModelScope.launch {
            repository.deleteAllHistories {
                getAllHistoriesFromRepository()
            }
        }
    }

    fun deleteHistoryFromRepository(user: UserResponse){
        user.id?.let{
            viewModelScope.launch {
                repository.deleteHistory(
                    userId = it,
                    onSuccess = {
                        Log.d(TAG, "deleted ${user.username}")
                    }
                )
            }
        }
    }
}