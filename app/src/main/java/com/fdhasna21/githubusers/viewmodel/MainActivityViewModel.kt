package com.fdhasna21.githubusers.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fdhasna21.githubusers.model.entity.HistoryDb
import com.fdhasna21.githubusers.model.response.UserResponse
import com.fdhasna21.githubusers.repository.HistoryRepositoryImp
import com.fdhasna21.githubusers.repository.UserRepositoryImp
import com.fdhasna21.githubusers.utility.type.TAG
import kotlinx.coroutines.launch

/**
 * Updated by Fernanda Hasna on 26/09/2024.
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
            }
        )
    }

    fun getUsersFromRepository(){
        val lastID = lastID.value ?: 0
        userRepository.getUsers(
            lastID = lastID,
            onSuccess = { result ->
                result.data.let { users ->
                    _allUsers.value = users ?: arrayListOf()
                    _lastPage.value = (this@MainActivityViewModel.lastPage.value ?: 0) + 1
                    _lastID.value = users[users.size-1].id ?: 0
                }
            })
    }

    fun insertHistoryToRepository(username:String){
        viewModelScope.launch {
            historyRepository.insertHistory(
                history = HistoryDb(username = username, photoProfile = ""),
                onSuccess = {
                    Log.d(TAG, "$username added to history.")
                }
            )
        }
    }

    fun resetUsers(){
        _lastID.value = 0
    }
}