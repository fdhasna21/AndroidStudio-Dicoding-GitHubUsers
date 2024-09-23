package com.fdhasna21.githubusers.activity.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fdhasna21.githubusers.resolver.DataUtils
import com.fdhasna21.githubusers.adapter.UserRowAdapter
import com.fdhasna21.githubusers.resolver.dataclass.ErrorResponse
import com.fdhasna21.githubusers.resolver.enumclass.ErrorType
import com.fdhasna21.githubusers.resolver.dataclass.SearchUsers
import com.fdhasna21.githubusers.resolver.dataclass.User
import com.fdhasna21.githubusers.server.ServerAPI
import com.fdhasna21.githubusers.server.ServerInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Updated by Fernanda Hasna on 24/09/2024.
 */
class MainActivityViewModel : ViewModel() {
    private lateinit var serverInterface : ServerInterface
    private var activityConfig : MutableLiveData<Int> = MutableLiveData()
    private var dataList : MutableLiveData<ArrayList<User>> = MutableLiveData()
    private lateinit var rowAdapter : UserRowAdapter
    var errorResponse : ErrorResponse = ErrorResponse()
    var defaultLastID : Int = 0
    var searchPage : Int = 1

    fun setServerInterface(serverInterface: ServerInterface){
        this.serverInterface = serverInterface
    }

    fun setAdapter(adapter : UserRowAdapter){
        rowAdapter = adapter
    }

    fun setConfiguration(config : Int){
        activityConfig.value = config
    }

    fun getConfiguration() : MutableLiveData<Int> = activityConfig

    fun getDataList() : MutableLiveData<ArrayList<User>> = dataList

    fun searchData(keyword:String){
        serverInterface.search(keyword)?.enqueue(object : Callback<SearchUsers>{
            override fun onResponse(call: Call<SearchUsers>, response: Response<SearchUsers>) {
                if(response.isSuccessful){
                    val rawData = response.body()!!
                    lateinit var finalData : ArrayList<User>
                    if(rawData.total == 0)
                    {
                        errorResponse.type = ErrorType.DATA_EMPTY
                        errorResponse.code = response.code()
                        finalData = arrayListOf()
                    } else {
                        finalData = rawData.users!!
                    }
                    dataList.value = finalData
                    rowAdapter.addData(finalData)
                    searchPage++
                } else {
                    notWantedResponse(response.code(), "Search")
                }
            }

            override fun onFailure(call: Call<SearchUsers>, t: Throwable) {
                notWantedResponse(null, "Search")
            }
        })
    }

    fun defaultData(){
        serverInterface.users(lastID = defaultLastID)?.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                if(response.isSuccessful){
                    val data : ArrayList<User> = response.body()!!
                    dataList.value = data
                    rowAdapter.addData(data)
                    defaultLastID = data[data.size-1].id!!
                } else {
                    notWantedResponse(response.code(), "Default")
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                notWantedResponse(null, "Default")
            }
        })
    }

    private fun notWantedResponse(code:Int?, flag:String){
        errorResponse = ErrorResponse(
            type = DataUtils().checkErrorType(code),
            flag = flag,
            code = code
        )
        Log.i("mainActivity_${errorResponse.flag}", "${errorResponse.type}")
        dataList.value = null
    }
}