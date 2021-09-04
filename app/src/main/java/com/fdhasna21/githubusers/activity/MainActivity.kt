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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fdhasna21.githubusers.R
import com.fdhasna21.githubusers.activity.viewmodel.MainActivityViewModel
import com.fdhasna21.githubusers.adapter.UserRowAdapter
import com.fdhasna21.githubusers.databinding.ActivityMainBinding
import com.fdhasna21.githubusers.dataclass.User

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var rowAdapter: UserRowAdapter
    private lateinit var layoutManager : LinearLayoutManager
    private var isLoading = false
    private var isConfigChange = false

    private fun setResponse(it : ArrayList<User>?){
        if(it == null){
            val (errorType, errorCode) = viewModel.getErrorType()
            val error : ArrayList<Int> = errorType.setError(this)
            binding.mainError.layoutError.visibility = View.VISIBLE
            binding.mainError.errorImage.setImageDrawable(AppCompatResources.getDrawable(this, error[0]))
            binding.mainError.errorMessage.text = listOf(getString(error[1]), errorCode).joinToString(" \n Code:")
        } else {
            binding.mainError.layoutError.visibility = View.GONE
        }
        isLoading = false
        binding.mainProgress.visibility = View.INVISIBLE
        binding.refreshRecyclerView.isRefreshing = false
    }

    private fun getDataDefault(isLoadMore:Boolean){
        isLoading = true
        binding.mainProgress.visibility = View.VISIBLE
        if(!isLoadMore){
            viewModel.defaultLastID = 0
        }
        viewModel.defaultData()
        viewModel.getDataList().observe(this, { setResponse(it) })
    }

    private fun getDataSearch(keyword:String, isLoadMore: Boolean){
        isLoading = true
        binding.mainProgress.visibility = View.VISIBLE
        if(!isLoadMore){
            viewModel.searchPage = 1
        }
        viewModel.searchData(keyword)
        viewModel.getDataList().observe(this, { setResponse(it) })
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
        binding.searchView.clearFocus()
        if(!TextUtils.isEmpty(query)){
            getDataSearch(query!!, false)
        }else{
            getDataDefault(false)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(!TextUtils.isEmpty(newText)){
            getDataSearch(newText!!, false)
        }else{
            getDataDefault(false)
        }
        return true
    }

    override fun onRefresh() {
        viewModel.defaultLastID = 0
        if(binding.searchView.query.toString().isEmpty()){
            getDataDefault(false)
        } else{
            getDataSearch(binding.searchView.query.toString(), false)
        }
    }
}