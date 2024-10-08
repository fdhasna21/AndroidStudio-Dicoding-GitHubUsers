package com.fdhasna21.githubusers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fdhasna21.githubusers.model.entity.HistoryDb
import com.fdhasna21.githubusers.model.entity.toUserResponse
import com.fdhasna21.githubusers.model.response.UserResponse
import com.fdhasna21.githubusers.repository.HistoryRepositoryImp
import com.fdhasna21.githubusers.utility.ext.convertToArrayList
import kotlinx.coroutines.launch

/**
 * Created by Fernanda Hasna on 26/09/2024.
 * Updated by Fernanda Hasna on 27/09/2024.
 */

class HistoryViewModel(private val repository: HistoryRepositoryImp) : BaseViewModel(){
    private var _allHistories : MutableLiveData<ArrayList<UserResponse>> =
        MutableLiveData<ArrayList<UserResponse>>(arrayListOf())
    val allHistories : LiveData<ArrayList<UserResponse>> get() = _allHistories

    fun getAllHistoriesFromRepository(){
        viewModelScope.launch {
            startLoading()
            repository.getAllHistories(
                onSuccess = { histories ->
                    val userResponses = histories.convertToArrayList { it.toUserResponse() }
                    _allHistories.value = userResponses
                    endLoading()
                },
                onFailed = {
                    endLoading()
                }
            )
        }
    }

    fun deleteAllHistoriesFromRepository(){
        viewModelScope.launch {
            startLoading()
            repository.deleteAllHistories(
                onSuccess = {
                    getAllHistoriesFromRepository()
                },
                onFailed = {
                    endLoading()
                }
            )
        }
    }

    fun deleteHistoryFromRepository(user: UserResponse){
        user.id?.let{
            viewModelScope.launch {
                startLoading()
                repository.deleteHistory(
                    userId = it,
                    onSuccess = {
                        getAllHistoriesFromRepository()
                    },
                    onFailed = {
                        endLoading()
                    }
                )
            }
        }
    }

    fun restoreHistoryToRepository(user: UserResponse){
        user.id?.let {
            viewModelScope.launch {
                val username = user.username ?: ""
                val userPict = user.imageCachePath ?: ""
                val userTimestamp = user.timestampAsLong ?: 0
                startLoading()
                repository.insertOrUpdateHistory(
                    history = HistoryDb(username = username, userId = it, photoProfile = userPict, timestamp = userTimestamp),
                    onSuccess = {
                        getAllHistoriesFromRepository()
                    },
                    onFailed = {
                        endLoading()
                    }
                )
            }
        }
    }
}