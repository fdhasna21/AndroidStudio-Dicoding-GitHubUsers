package com.fdhasna21.githubusers.activity.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fdhasna21.githubusers.resolver.DataUtils
import com.fdhasna21.githubusers.resolver.dataclass.ErrorResponse
import com.fdhasna21.githubusers.resolver.enumclass.ErrorType
import com.fdhasna21.githubusers.resolver.dataclass.Repository
import com.fdhasna21.githubusers.resolver.dataclass.User
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
    var errorDetail : ErrorResponse = ErrorResponse()
    var errorRepo : ErrorResponse = ErrorResponse()
    var errorStarred : ErrorResponse = ErrorResponse()
    var errorFollowers : ErrorResponse = ErrorResponse()
    var errorFollowing : ErrorResponse = ErrorResponse()
    var username : String = ""

    fun setConfiguration(config : Int){
        activityConfig.value = config
    }

    fun getConfiguration() : MutableLiveData<Int> = activityConfig

    fun getDetailData(){
        serverInterface.user(username)?.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    val rawData = response.body()!!
                    lateinit var finalData : User
                    if(rawData == User())
                    {
                        errorDetail.type = ErrorType.DATA_EMPTY
                        errorDetail.code = response.code()
                        finalData = User()
                    } else {
                        finalData = rawData
                    }
                    detailList.value = finalData
                } else {
                    detailList.value = null
                    notWantedResponse(null, "Detail")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                detailList.value = null
                notWantedResponse(null, "Detail")
            }
        })
    }

    fun getRepositoryData(){
        serverInterface.repository(username)?.enqueue(object : Callback<ArrayList<Repository>>{
            override fun onResponse(call: Call<ArrayList<Repository>>, response: Response<ArrayList<Repository>>) {
                if(response.isSuccessful){
                    val rawData = response.body()!!
                    lateinit var finalData : ArrayList<Repository>
                    if(rawData.size == 0)
                    {
                        errorRepo.type = ErrorType.DATA_EMPTY
                        errorRepo.code = response.code()
                        finalData = arrayListOf()
                    } else {
                        finalData = rawData
                    }
                    repositoryList.value = finalData
                } else {
                    repositoryList.value = null
                    notWantedResponse(null, "Repositories")
                }
            }

            override fun onFailure(call: Call<ArrayList<Repository>>, t: Throwable) {
                repositoryList.value = null
                notWantedResponse(null, "Repositories")
            }
        })
    }

   fun getStarredData(){
        serverInterface.starred(username)?.enqueue(object : Callback<ArrayList<Repository>>{
            override fun onResponse(call: Call<ArrayList<Repository>>, response: Response<ArrayList<Repository>>) {
                Log.i("userDetailActivity", "onResponse: ${response.code()}")
                if(response.isSuccessful){
                    val rawData = response.body()!!
                    lateinit var finalData : ArrayList<Repository>
                    if(rawData.size == 0)
                    {
                        errorStarred.type = ErrorType.DATA_EMPTY
                        errorStarred.code = response.code()
                        finalData = arrayListOf()
                    } else {
                        finalData = rawData
                    }
                    starredList.value = finalData
                } else {
                    starredList.value = null
                    notWantedResponse(null, "Starred")
                }
            }

            override fun onFailure(call: Call<ArrayList<Repository>>, t: Throwable) {
                starredList.value = null
                notWantedResponse(null, "Starred")
            }
        })
    }

   fun getFollowersData() {
        serverInterface.followers(username)?.enqueue(object : Callback<ArrayList<User>>{
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                if(response.isSuccessful){
                    val rawData = response.body()!!
                    lateinit var finalData : ArrayList<User>
                    if(rawData.size == 0)
                    {
                        errorFollowers.type = ErrorType.DATA_EMPTY
                        errorFollowers.code = response.code()
                        finalData = arrayListOf()
                    } else {
                        finalData = rawData
                    }
                    followersList.value = finalData
                } else {
                    followersList.value = null
                    notWantedResponse(null, "Followers")
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                followersList.value = null
                notWantedResponse(null, "Followers")
            }
        })
    }

   fun getFollowingData() {
        serverInterface.following(username)?.enqueue(object : Callback<ArrayList<User>>{
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                if(response.isSuccessful){
                    val rawData = response.body()!!
                    lateinit var finalData : ArrayList<User>
                    if(rawData.size == 0)
                    {
                        errorFollowing.type = ErrorType.DATA_EMPTY
                        errorFollowing.code = response.code()
                        finalData = arrayListOf()
                    } else {
                        finalData = rawData
                    }
                    followingList.value = finalData
                } else {
                    followingList.value = null
                    notWantedResponse(null, "Following")
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                followingList.value = null
                notWantedResponse(null, "Following")
            }

        })
    }

   private fun notWantedResponse(code:Int?, flag:String){
        val error = ErrorResponse(
            type = DataUtils().checkErrorType(code),
            flag = flag,
            code = code
        )
        when(flag){
            "Detail"        -> errorDetail = error
            "Repositories"  -> errorRepo = error
            "Starred"       -> errorStarred = error
            "Followers"     -> errorFollowers = error
            "Following"     -> errorFollowing = error
        }
       Log.i("detailUserActivity_${error.flag}", "${error.type}")
   }
}