package com.fdhasna21.githubusers.activity.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fdhasna21.githubusers.dataclass.Repository
import com.fdhasna21.githubusers.dataclass.User
import com.fdhasna21.githubusers.server.ServerAPI
import com.fdhasna21.githubusers.server.ServerInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivityViewModel : ViewModel() {
    private var serverInterface : ServerInterface = ServerAPI().getServerAPI()!!.create(ServerInterface::class.java)
    private var activityConfig : MutableLiveData<Int> = MutableLiveData()
    var detailList : MutableLiveData<User> = MutableLiveData()
    var repositoryList : MutableLiveData<ArrayList<Repository>> = MutableLiveData()
    var starredList : MutableLiveData<ArrayList<Repository>> = MutableLiveData()
    var followersList : MutableLiveData<ArrayList<User>> = MutableLiveData()
    var followingList : MutableLiveData<ArrayList<User>> = MutableLiveData()
    var username : String = ""
    var errorThrowable : String = ""

    fun getConfiguration() : MutableLiveData<Int> {
        return activityConfig
    }

    fun setConfiguration(config : Int){
        activityConfig.value = config
    }

    fun getUserDetail(){
        getDetailData()
        getRepositoryData()
        getStarredData()
        getFollowersData()
        getFollowingData()
    }

    private fun getDetailData(){
        serverInterface.user(username)?.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    detailList.value = response.body()!!
                } else {
                    detailList.value = User()
                    errorThrowable = response.errorBody().toString()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                detailList.value = null
                errorThrowable = t.toString()
            }

        })
    }

    private fun getRepositoryData(){
        serverInterface.repository(username)?.enqueue(object : Callback<ArrayList<Repository>>{
            override fun onResponse(call: Call<ArrayList<Repository>>, response: Response<ArrayList<Repository>>) {
                if(response.isSuccessful){
                    repositoryList.value = response.body()!!
                } else {
                    repositoryList.value = arrayListOf()
                    errorThrowable = response.errorBody().toString()
                }
            }

            override fun onFailure(call: Call<ArrayList<Repository>>, t: Throwable) {
                repositoryList.value = null
                errorThrowable = t.toString()
            }
        })
    }

    private fun getStarredData(){
        serverInterface.starred(username)?.enqueue(object : Callback<ArrayList<Repository>>{
            override fun onResponse(call: Call<ArrayList<Repository>>, response: Response<ArrayList<Repository>>) {
                if(response.isSuccessful){
                    starredList.value = response.body()!!
                } else {
                    starredList.value = arrayListOf()
                    errorThrowable = response.errorBody().toString()
                }
            }

            override fun onFailure(call: Call<ArrayList<Repository>>, t: Throwable) {
                starredList.value = null
                errorThrowable = t.toString()
            }

        })
    }

    private fun getFollowersData() {
        serverInterface.followers(username)?.enqueue(object : Callback<ArrayList<User>>{
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                if(response.isSuccessful){
                    followersList.value = response.body()!!
                } else {
                    followersList.value = arrayListOf()
                    errorThrowable = response.errorBody().toString()
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                followersList.value = null
                errorThrowable = t.toString()
            }
        })
    }

    private fun getFollowingData() {
        serverInterface.following(username)?.enqueue(object : Callback<ArrayList<User>>{
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                if(response.isSuccessful){
                    followingList.value = response.body()!!
                } else {
                    followingList.value = arrayListOf()
                    errorThrowable = response.errorBody().toString()
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                followingList.value = null
                errorThrowable = t.toString()
            }

        })
    }
}