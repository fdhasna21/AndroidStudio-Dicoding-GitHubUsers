package com.fdhasna21.githubusers.activity

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fdhasna21.githubusers.R
import com.fdhasna21.githubusers.adapter.UserRowAdapter
import com.fdhasna21.githubusers.databinding.ActivityHistoryBinding

import com.fdhasna21.githubusers.repository.UserRepositoryImp
import com.fdhasna21.githubusers.viewmodel.HistoryViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Fernanda Hasna on 26/09/2024.
 */

class HistoryActivity : BaseActivity<ActivityHistoryBinding, HistoryViewModel, UserRepositoryImp>(
    ActivityHistoryBinding::inflate,
    HistoryViewModel::class.java
) {

    override val viewModel: HistoryViewModel by viewModel()
    override val repository: UserRepositoryImp by inject()

    private lateinit var rowAdapter: UserRowAdapter
    private lateinit var layoutManager : LinearLayoutManager

    override fun setupUIWhenConfigChange() {
        setupToolbar()
        setupRecyclerView()
        super.setupUIWhenConfigChange()
    }

    private fun setupToolbar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.title = getString(R.string.history)
    }

    private fun setupRecyclerView(){
        binding.apply {
            rowAdapter = UserRowAdapter(arrayListOf(), this@HistoryActivity)
            layoutManager = LinearLayoutManager(this@HistoryActivity)

            recyclerView.apply {
                layoutManager = this@HistoryActivity.layoutManager
                adapter = rowAdapter
                addItemDecoration(object : DividerItemDecoration(this@HistoryActivity, VERTICAL) {})
                setHasFixedSize(true)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}