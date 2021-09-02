package com.fdhasna21.githubusers.activity.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fdhasna21.githubusers.dataclass.SearchUsers
import com.fdhasna21.githubusers.dataclass.User
import com.fdhasna21.githubusers.server.ServerAPI
import com.fdhasna21.githubusers.server.ServerInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel : ViewModel() {
    var serverInterface : ServerInterface = ServerAPI().getAPI()!!.create(ServerInterface::class.java)
    var errorThrowable : String? = null
    var defaultData : MutableLiveData<ArrayList<User>> = MutableLiveData()
    var defaultLastID : Int = 0
    var searchData : MutableLiveData<ArrayList<User>> = MutableLiveData()
    var searchPage : Int = 1

    fun getDefaultData(){
        serverInterface.users(lastID = defaultLastID).enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                if(response.isSuccessful){
                    val data : ArrayList<User> = response.body()!!
                    defaultData.value = data
                    defaultLastID = data[data.size-1].id!!
                } else {
                    defaultData.value = null
                    defaultLastID = 0
                    errorThrowable = response.errorBody().toString()
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                defaultData.postValue(null)
                errorThrowable = t.toString()
            }
        })
    }

    fun getSearchData(keyword:String){
        serverInterface.search(keyword).enqueue(object : Callback<SearchUsers>{
            override fun onResponse(call: Call<SearchUsers>, response: Response<SearchUsers>) {
                if(response.isSuccessful){
                    val data : ArrayList<User> = response.body()!!.users!!
                    searchData.value = data
                    searchPage++
                } else {
                    searchData.value = null
                    searchPage = 1
                    errorThrowable = response.errorBody().toString()
                }
            }

            override fun onFailure(call: Call<SearchUsers>, t: Throwable) {
                defaultData.postValue(null)
                errorThrowable = t.toString()
            }
        })
    }
}