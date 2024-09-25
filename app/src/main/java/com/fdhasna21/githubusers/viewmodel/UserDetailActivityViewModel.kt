package com.fdhasna21.githubusers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fdhasna21.githubusers.model.RepoResponse
import com.fdhasna21.githubusers.model.UserResponse
import com.fdhasna21.githubusers.repository.UserRepositoryImp

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */
class UserDetailActivityViewModel(private val repository: UserRepositoryImp) : BaseViewModel() {

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
            repository.getUserDetail(
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
            repository.getUserFollowings(
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
            repository.getUserFollowers(
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
            repository.getUserRepos(
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
            repository.getUserStars(
                username = username,
                onSuccess = { result ->
                    result.data.let {
                        _allStars.value = it
                    }
                }
            )
        }
    }
}