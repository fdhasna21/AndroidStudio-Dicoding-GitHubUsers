package com.fdhasna21.githubusers.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fdhasna21.githubusers.R
import com.fdhasna21.githubusers.adapter.UserRowAdapter
import com.fdhasna21.githubusers.databinding.ActivityMainBinding
import com.fdhasna21.githubusers.utility.type.ErrorType
import com.fdhasna21.githubusers.viewmodel.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Updated by Fernanda Hasna on 27/09/2024.
 */

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>(
    ActivityMainBinding::inflate,
    MainActivityViewModel::class.java
), SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

    override val viewModel: MainActivityViewModel by viewModel()

    private lateinit var rowAdapter: UserRowAdapter
    private lateinit var layoutManager : LinearLayoutManager
    override fun setupData() {
        getAllUsers(true)
    }

    override fun setupUIWhenConfigChange() {
        setupToolbar()
        setupRecyclerView()
        super.setupUIWhenConfigChange()
    }
    override fun setupUIWithoutConfigChange() {
        viewModel.isLoading.observe(this){
            when(it){
                true -> {
                    binding.mainResponse.progressCircular.visibility = View.VISIBLE
                }
                false -> {
                    binding.mainResponse.progressCircular.visibility = View.INVISIBLE
                    binding.refreshRecyclerView.isRefreshing = false
                }
            }
        }
        viewModel.allUsers.observe(this){
            if (it == null || it.isEmpty()) {
//                if(viewModel.isLoading.value == false){
                    val error: ArrayList<Int> = ErrorType.DATA_EMPTY.setError(this)
                    binding.mainResponse.layoutError.visibility = View.VISIBLE
                    binding.mainResponse.errorImage.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            error[0]
                        )
                    )
                    binding.mainResponse.errorMessage.text = getString(error[1])
                    binding.recyclerView.visibility = View.INVISIBLE
//                }
            } else {
                binding.mainResponse.layoutError.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                rowAdapter.updateData(it)
            }
        }
    }

    private fun setupToolbar(){
        supportActionBar?.title = getString(R.string.app_name)
        val searchManager : SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        binding.searchView.setOnQueryTextListener(this@MainActivity)
    }

    private fun setupRecyclerView(){
        binding.apply {
            refreshRecyclerView.setOnRefreshListener(this@MainActivity)
            rowAdapter = UserRowAdapter( this@MainActivity, arrayListOf(),
                onClickListener = { user ->
                    viewModel.insertOrUpdateHistoryToRepository(user)
                }
            )
            layoutManager = LinearLayoutManager(this@MainActivity)

            recyclerView.apply {
                layoutManager = this@MainActivity.layoutManager
                adapter = rowAdapter
                addItemDecoration(object : DividerItemDecoration(this@MainActivity, VERTICAL) {})
                setHasFixedSize(true)
            }
        }
    }

    private fun getAllUsers(isReset:Boolean = false){
        if(isReset){
            viewModel.resetUsers()
        }
        viewModel.getUsersFromRepository()
    }

    private fun getAllUsersByKeywords(keyword : String?){
        if(keyword.isNullOrEmpty()){
            viewModel.getUsersFromRepository()
        } else {
            viewModel.setKeyword(keyword)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_history -> {
                startActivity(
                    Intent(
                        this,
                        HistoryActivity::class.java
                    )
                )
            }
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
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (currentFocus != null && imm.isAcceptingText){
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        binding.searchView.clearFocus()
        getAllUsersByKeywords(query ?: "")
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        getAllUsersByKeywords(newText ?: "")
        return true
    }

    override fun onRefresh() {
        if(binding.searchView.query.toString().isEmpty()){
            getAllUsers(true)
        } else{
            getAllUsersByKeywords(binding.searchView.query.toString())
        }
    }
}