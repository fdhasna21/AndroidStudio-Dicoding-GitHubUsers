package com.fdhasna21.githubusers.activity.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fdhasna21.githubusers.adapter.UserRowAdapter
import com.fdhasna21.githubusers.dataclass.SearchUsers
import com.fdhasna21.githubusers.dataclass.User
import com.fdhasna21.githubusers.server.ServerAPI
import com.fdhasna21.githubusers.server.ServerInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel : ViewModel() {
    private var serverInterface : ServerInterface = ServerAPI().getServerAPI()!!.create(ServerInterface::class.java)
    private var activityConfig : MutableLiveData<Int> = MutableLiveData()
    private var defaultList : MutableLiveData<ArrayList<User>> = MutableLiveData()
    private var searchList : MutableLiveData<ArrayList<User>> = MutableLiveData()
    private var searchKey : MutableLiveData<String> = MutableLiveData("")
    private lateinit var rowAdapter : UserRowAdapter
    var errorThrowable : String = ""
    var defaultLastID : Int = 0
    var searchPage : Int = 1

    fun getDefaultData() : MutableLiveData<ArrayList<User>>{
        return defaultList
    }

    fun getSearchData() : MutableLiveData<ArrayList<User>>{
        return searchList
    }

    fun setAdapter(adapter : UserRowAdapter){
        rowAdapter = adapter
    }

    fun getConfiguration() : MutableLiveData<Int> {
        return activityConfig
    }

    fun setConfiguration(config : Int){
        activityConfig.value = config
    }

    fun setSearchKey(query : String){
        searchKey.value = query
    }

    fun getSearchKey() : String{
        return searchKey.value.toString()
    }

    fun setDefaultData(){
        serverInterface.users(lastID = defaultLastID)?.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                if(response.isSuccessful){
                    val data : ArrayList<User> = response.body()!!
                    rowAdapter.addData(data)
                    defaultList.value = data
                    defaultLastID = data[data.size-1].id!!
                } else {
                    val data : ArrayList<User> = arrayListOf()
                    rowAdapter.addData(data)
                    defaultList.value = arrayListOf()
                    defaultLastID = 0
                    errorThrowable = response.errorBody().toString()
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                defaultList.value = null
                errorThrowable = t.toString()
            }
        })
    }

    fun setSearchData(keyword:String){
        serverInterface.search(keyword)?.enqueue(object : Callback<SearchUsers>{
            override fun onResponse(call: Call<SearchUsers>, response: Response<SearchUsers>) {
                if(response.isSuccessful){
                    val data : ArrayList<User> = response.body()!!.users!!
                    rowAdapter.addData(data)
                    searchList.value = data
                    searchPage++
                } else {
                    val data : ArrayList<User> = arrayListOf()
                    rowAdapter.addData(data)
                    searchList.value = arrayListOf()
                    searchPage = 1
                    errorThrowable = response.errorBody().toString()
                }
            }

            override fun onFailure(call: Call<SearchUsers>, t: Throwable) {
                defaultList.value = null
                errorThrowable = t.toString()
            }
        })
    }
}