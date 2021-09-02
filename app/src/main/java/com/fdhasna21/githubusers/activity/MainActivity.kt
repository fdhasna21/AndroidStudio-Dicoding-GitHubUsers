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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private fun getDataDefault(isLoadMore:Boolean){
        isLoading = true
        if(!isLoadMore){
            binding.mainProgress.visibility = View.VISIBLE
            viewModel.defaultLastID = 0
        }

        viewModel.getDefaultData()
        viewModel.defaultData.observe(this, {
            if(it != null){
                rowAdapter.addData(it)
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

        viewModel.getSearchData(keyword)
        viewModel.searchData.observe(this, {
            if(it != null){
                rowAdapter.addData(it)
                isLoading = false
                binding.mainProgress.visibility = View.INVISIBLE
                binding.refreshRecyclerView.isRefreshing = false
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
        layoutManager = LinearLayoutManager(this)
        getDataDefault(false)

        binding.recyclerView.adapter = rowAdapter
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(object : DividerItemDecoration(this@MainActivity, VERTICAL) {})
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
//            var firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                val currentVisibleItem = layoutManager.findFirstVisibleItemPosition()
//                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
//                val visibleItemCount = layoutManager.childCount
//                val totalItem = rowAdapter.itemCount
//
//                if((!isLoading && !binding.refreshRecyclerView.isRefreshing)
//                    && (newState == RecyclerView.SCROLL_STATE_DRAGGING)){
//                    if(visibleItemCount + currentVisibleItem >= totalItem){
//                        //cek itu search/datadefault
//                        getDataDefault(true)
//                        Toast.makeText(this@MainActivity, "curr$currentVisibleItem, first${firstVisibleItem}, last$lastVisibleItem, total$totalItem, visibleitem$visibleItemCount", Toast.LENGTH_LONG).show()
//                    }
//                }
//                firstVisibleItem = currentVisibleItem
//            }
            //dragging bawah ke atas. lastvisible+1 lebih besar dari totalitem. isLoadingnya false
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        setupToolbar()
        setupRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null){
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!TextUtils.isEmpty(query)){
            getDataSearch(query!!, false)
        }else{
            getDataDefault(false)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(!TextUtils.isEmpty(newText)){
            getDataSearch(newText!!, false)
        }else{
            getDataDefault(false)
        }
        return false
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
}