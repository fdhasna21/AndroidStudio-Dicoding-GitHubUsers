package com.fdhasna21.githubusers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fdhasna21.githubusers.model.entity.HistoryDb
import com.fdhasna21.githubusers.model.response.UserResponse
import com.fdhasna21.githubusers.repository.HistoryRepositoryImp
import com.fdhasna21.githubusers.repository.UserRepositoryImp
import kotlinx.coroutines.launch

/**
 * Updated by Fernanda Hasna on 27/09/2024.
 */

class MainActivityViewModel(
    private val userRepository: UserRepositoryImp,
    private val historyRepository: HistoryRepositoryImp
) : BaseViewModel() {

    private var _allUsers : MutableLiveData<ArrayList<UserResponse>> =
        MutableLiveData<ArrayList<UserResponse>>(arrayListOf())
    val allUsers : LiveData<ArrayList<UserResponse>> get() = _allUsers

    private var _lastID : MutableLiveData<Int> = MutableLiveData(0)
    val lastID : LiveData<Int> get() = _lastID

    private var _lastPage : MutableLiveData<Int> = MutableLiveData(0)
    val lastPage : LiveData<Int> get() = _lastPage

    private var _keyword : MutableLiveData<String> = MutableLiveData("")

    fun setKeyword(keyword:String){
        if(keyword != _keyword.value) {
            _keyword.value = keyword
        } else {
            _lastPage.value = (_lastPage.value ?: 0) + 1
        }
        getUsersByKeywordFromRepository(
            keyword = _keyword.value ?: "",
            lastPage = _lastPage.value ?: 0
        )
    }

    private fun getUsersByKeywordFromRepository(keyword:String, lastPage:Int){
        startLoading()
        userRepository.getUsersByKeyword(
            key = keyword,
            page = lastPage,
            onSuccess = { result ->
                result.data.let{
                    it.userResponses?.let { users ->
                        _allUsers.value = users
                        _lastPage.value = (this@MainActivityViewModel.lastPage.value ?: 0) + 1
                    }
                }
                endLoading()
            },
            onFailed = {
                endLoading()
            }
        )
    }

    fun getUsersFromRepository(){
        startLoading()
        val lastID = lastID.value ?: 0
        userRepository.getUsers(
            lastID = lastID,
            onSuccess = { result ->
                result.data.let { users ->
                    _allUsers.value = users ?: arrayListOf()
                    _lastPage.value = (this@MainActivityViewModel.lastPage.value ?: 0) + 1
                    _lastID.value = users[users.size-1].id ?: 0
                }
                endLoading()
            },
            onFailed = {
                endLoading()
            }
        )
    }

    fun insertOrUpdateHistoryToRepository(user: UserResponse){
        startLoading()
        user.id?.let {
            viewModelScope.launch {
                val username = user.username ?: ""
                val userPict = user.imageCachePath ?: ""
                historyRepository.insertOrUpdateHistory(
                    history = HistoryDb(username = username, userId = it, photoProfile = userPict, timestamp = System.currentTimeMillis()),
                    onSuccess = {
                        endLoading()
                    },
                    onFailed = {
                        endLoading()
                    }
                )
            }
        }
    }

    fun resetUsers(){
        _lastID.value = 0
    }
}