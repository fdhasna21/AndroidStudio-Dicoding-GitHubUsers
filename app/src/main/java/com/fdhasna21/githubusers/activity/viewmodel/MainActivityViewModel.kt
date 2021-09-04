package com.fdhasna21.githubusers.activity.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fdhasna21.githubusers.DataUtils
import com.fdhasna21.githubusers.adapter.UserRowAdapter
import com.fdhasna21.githubusers.dataclass.ErrorType
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
    private var dataList : MutableLiveData<ArrayList<User>> = MutableLiveData()
    private lateinit var rowAdapter : UserRowAdapter
    private var errorType : ErrorType = ErrorType.OTHERS
    private var errorCode : Int? = 200
    var defaultLastID : Int = 0
    var searchPage : Int = 1

    fun setAdapter(adapter : UserRowAdapter){
        rowAdapter = adapter
    }

    fun setConfiguration(config : Int){
        activityConfig.value = config
    }

    fun getConfiguration() : MutableLiveData<Int> = activityConfig

    fun getDataList() : MutableLiveData<ArrayList<User>> = dataList

    fun getErrorType() = Pair(errorType, errorCode.toString())

    fun searchData(keyword:String){
        serverInterface.search(keyword)?.enqueue(object : Callback<SearchUsers>{
            override fun onResponse(call: Call<SearchUsers>, response: Response<SearchUsers>) {
                if(response.isSuccessful){
                    val rawData = response.body()!!
                    lateinit var finalData : ArrayList<User>
                    if(rawData.total == 0)
                    {
                        errorType = ErrorType.DATA_EMPTY
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
                dataList.value = null
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
                    notWantedResponse(response.code(), "Search")
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                dataList.value = null
                notWantedResponse(null, "Search")
            }
        })
    }

    private fun notWantedResponse(code:Int?, flag:String){
        dataList.value = null
        errorType = DataUtils().checkErrorType(code)
        errorCode = code
        Log.i("mainActivity_$flag", "$code : $errorType")
    }
}