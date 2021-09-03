package com.fdhasna21.githubusers.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fdhasna21.githubusers.R
import com.fdhasna21.githubusers.activity.viewmodel.MainActivityViewModel
import com.fdhasna21.githubusers.adapter.UserRowAdapter
import com.fdhasna21.githubusers.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var rowAdapter: UserRowAdapter
    private lateinit var layoutManager : LinearLayoutManager
    private var isLoading = false
    private var isConfigChange = false

    private fun getDataDefault(isLoadMore:Boolean){
        isLoading = true
        if(!isLoadMore){
            binding.mainProgress.visibility = View.VISIBLE
            viewModel.defaultLastID = 0
        }

        viewModel.setDefaultData()
        viewModel.getDefaultData().observe(this, {
            if(it != null){
                if(it.isEmpty()){
                    Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
                binding.mainProgress.visibility = View.INVISIBLE
                binding.refreshRecyclerView.isRefreshing = false
            } else {
                Toast.makeText(this, viewModel.errorThrowable, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getDataSearch(keyword:String, isLoadMore: Boolean){
        isLoading = true
        if(!isLoadMore){
            binding.mainProgress.visibility = View.VISIBLE
            rowAdapter.clearData()
            viewModel.searchPage = 1
        }

        viewModel.setSearchData(keyword)
        viewModel.getSearchData().observe(this, {
            if(it != null){
                if(it.isEmpty()){
                    Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
                binding.mainProgress.visibility = View.INVISIBLE
                binding.refreshRecyclerView.isRefreshing = false
            } else {
                Toast.makeText(this, viewModel.errorThrowable, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupToolbar(){
        supportActionBar?.title = getString(R.string.app_name)

        val searchManager : SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        binding.searchView.setOnQueryTextListener(this)
    }

    private fun setupRecyclerView(){
        binding.refreshRecyclerView.setOnRefreshListener(this)
        rowAdapter = UserRowAdapter(arrayListOf(), this@MainActivity)
        viewModel.setAdapter(rowAdapter)
        layoutManager = LinearLayoutManager(this)
        getDataDefault(false)

        binding.recyclerView.adapter = rowAdapter
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(object : DividerItemDecoration(this@MainActivity, VERTICAL) {})
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        if(!isConfigChange){
            setupToolbar()
            setupRecyclerView()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (currentFocus != null && imm.isAcceptingText){
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        viewModel.setConfiguration(newConfig.orientation)
        viewModel.getConfiguration().observe(this, {
            isConfigChange = (it != newConfig.orientation)
        })
        super.onConfigurationChanged(newConfig)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.setSearchKey(query!!)
        binding.searchView.clearFocus()
        if(!TextUtils.isEmpty(query)){
            getDataSearch(query, false)
        }else{
            getDataDefault(false)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.setSearchKey(newText!!)
        if(!TextUtils.isEmpty(newText)){
            getDataSearch(newText, false)
        }else{
            getDataDefault(false)
        }
        return true
    }

    override fun onRefresh() {
        rowAdapter.clearData()
        viewModel.defaultLastID = 0
        if(binding.searchView.query.toString().isEmpty()){
            getDataDefault(false)
        } else{
            getDataSearch(binding.searchView.query.toString(), false)
        }
    }

    //todo : onFailure bikin mekanisme kalau error gimana, swipe bawah buat nambah data yang di load
    //todo : abis null, langsung error
}