package com.fdhasna21.githubusers.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fdhasna21.githubusers.R
import com.fdhasna21.githubusers.adapter.UserRowAdapter
import com.fdhasna21.githubusers.databinding.ActivityMainBinding
import com.fdhasna21.githubusers.dataclass.SearchUsers
import com.fdhasna21.githubusers.dataclass.User
import com.fdhasna21.githubusers.server.ServerAPI
import com.fdhasna21.githubusers.server.ServerInterface
import com.fdhasna21.githubusers.server.ServerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var serverInterface: ServerInterface
    private var lastID : Int = 0
    private var data : ArrayList<User> = arrayListOf()

    private fun getDataDefault(){
        binding.mainProgress.visibility = View.VISIBLE
        serverInterface.users(lastID = lastID).enqueue(object : Callback<ArrayList<User>>{
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                if(response.isSuccessful){
                    refreshData(ServerResponse.SUCCESS, response.body())
                    lastID = data[data.size-1].id!!
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                refreshData(ServerResponse.FAILURE, error=t.toString())
            }
        })
    }

    private fun getDataSearch(keyword:String){
        data.removeAll(data)
        binding.mainProgress.visibility = View.VISIBLE
        serverInterface.search(keyword).enqueue(object : Callback<SearchUsers>{
            override fun onResponse(call: Call<SearchUsers>, response: Response<SearchUsers>) {
                if(response.isSuccessful) {
                    refreshData(ServerResponse.SUCCESS, response.body()?.users!!)
                }
            }

            override fun onFailure(call: Call<SearchUsers>, t: Throwable) {
                refreshData(ServerResponse.FAILURE, error=t.toString())
            }

        })
    }

    private fun refreshData(state: ServerResponse, output:ArrayList<User>?=null, error:String?=null){
        binding.mainProgress.visibility = View.INVISIBLE
        when(state){
            ServerResponse.SUCCESS ->{
                data.addAll(output!!)
            }
            ServerResponse.FAILURE ->{
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_LONG).show()
                data = arrayListOf()
            }
        }
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun setupToolbar(){
        supportActionBar?.title = getString(R.string.app_name)

        val searchManager : SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        binding.searchView.setOnQueryTextListener(this)
    }

    private fun setupRecyclerView(){
        binding.mainProgress.visibility = View.VISIBLE
        val rowAdapter = UserRowAdapter(data, this@MainActivity)
        binding.recyclerView.adapter = rowAdapter
        binding.recyclerView.addItemDecoration(object : DividerItemDecoration(this@MainActivity, VERTICAL) {})
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        getDataDefault()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        val serverAPI = ServerAPI()
        serverInterface = serverAPI.getServerAPI(binding.mainProgress, this)!!.create(ServerInterface::class.java)
        setupRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_about -> {
                startActivity(
                    Intent(
                        this,
                        AboutMeActivity::class.java
                    )
                )
            }
            R.id.menu_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!TextUtils.isEmpty(query)){
            getDataSearch(query!!)
        }else{
            lastID=0
            getDataDefault()
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(!TextUtils.isEmpty(newText)){
            getDataSearch(newText!!)
        }else{
            lastID=0
            getDataDefault()
        }
        return false
    }

    //todo : onSuccess datanya cuma nama, onFailure bikin mekanisme kalau error gimana
    //todo : swipe atas buat refresh, swipe bawah buat nambah data yang di load
}