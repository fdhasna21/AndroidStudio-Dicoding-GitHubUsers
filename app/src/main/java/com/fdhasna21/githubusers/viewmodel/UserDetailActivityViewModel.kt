package com.fdhasna21.githubusers.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fdhasna21.githubusers.model.entity.HistoryDb
import com.fdhasna21.githubusers.model.response.RepoResponse
import com.fdhasna21.githubusers.model.response.UserResponse
import com.fdhasna21.githubusers.repository.HistoryRepositoryImp
import com.fdhasna21.githubusers.repository.UserRepositoryImp
import com.fdhasna21.githubusers.utility.type.TAG
import kotlinx.coroutines.launch

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */
class UserDetailActivityViewModel(
    private val userRepository: UserRepositoryImp,
    private val historyRepository: HistoryRepositoryImp
) : BaseViewModel() {

    private var _userDetail : MutableLiveData<UserResponse> =
        MutableLiveData<UserResponse>()
    val userDetail : LiveData<UserResponse> get() = _userDetail

    private var _allFollowings : MutableLiveData<ArrayList<UserResponse>> =
        MutableLiveData<ArrayList<UserResponse>>(arrayListOf())
    val allFollowings : LiveData<ArrayList<UserResponse>> get() = _allFollowings
    
    private var _allFollowers : MutableLiveData<ArrayList<UserResponse>> =
        MutableLiveData<ArrayList<UserResponse>>(arrayListOf())
    val allFollowers : LiveData<ArrayList<UserResponse>> get() = _allFollowers

    private var _allRepos : MutableLiveData<ArrayList<RepoResponse>> =
        MutableLiveData<ArrayList<RepoResponse>>(arrayListOf())
    val allRepos : LiveData<ArrayList<RepoResponse>> get() = _allRepos

    private var _allStars : MutableLiveData<ArrayList<RepoResponse>> =
        MutableLiveData<ArrayList<RepoResponse>>(arrayListOf())
    val allStars : LiveData<ArrayList<RepoResponse>> get() = _allStars

    private var _username : MutableLiveData<String> = MutableLiveData("")
    val username : LiveData<String> get() = _username

    fun setUsername(username: String){
        _username.value = username
        getUserDetail()
    }

    private fun getUserDetail(){
        _username.value?.let { username ->
            userRepository.getUserDetail(
                username = username,
                onSuccess = { result ->
                    result.data.let {
                        _userDetail.value = it
                    }
                }
            )
        }
    }

    fun getFollowingsFromRepository(){
        _username.value?.let { username ->
            userRepository.getUserFollowings(
                username = username,
                onSuccess = { result ->
                    result.data.let {
                        _allFollowings.value = it
                    }
                }
            )
        }
    }

    fun getFollowersFromRepository(){
        _username.value?.let { username ->
            userRepository.getUserFollowers(
                username = username,
                onSuccess = { result ->
                    result.data.let {
                        _allFollowers.value = it
                    }
                }
            )
        }
    }

    fun getReposFromRepository(){
        _username.value?.let { username ->
            userRepository.getUserRepos(
                username = username,
                onSuccess = { result ->
                    result.data.let {
                        _allRepos.value = it
                    }
                }
            )
        }
    }

    fun getStarsFromRepository(){
        _username.value?.let { username ->
            userRepository.getUserStars(
                username = username,
                onSuccess = { result ->
                    result.data.let {
                        _allStars.value = it
                    }
                }
            )
        }
    }

    fun updateHistoryFromRepository(userPictCachePath: String){
        userDetail.value?.let { user ->
            user.id?.let{
                viewModelScope.launch {
                    val username = user.username ?: ""
                    val newTimestamp = System.currentTimeMillis()
                    val updated = HistoryDb(username = username, userId = it, photoProfile = userPictCachePath, timestamp = newTimestamp)
//                    Log.d(TAG, "updateHistoryFromRepository: $newTimestamp")
//                    Log.d(TAG, "updateHistoryFromRepository: $updated")
                    historyRepository.updateHistory(
                        history = updated,
                        onSuccess = {
                            Log.d(TAG, "$username updated to history.")
                        }
                    )
                }
            }
        }
    }
}