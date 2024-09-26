package com.fdhasna21.githubusers.activity

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fdhasna21.githubusers.R
import com.fdhasna21.githubusers.adapter.UserRowAdapter
import com.fdhasna21.githubusers.databinding.ActivityHistoryBinding
import com.fdhasna21.githubusers.utility.DialogUtils
import com.fdhasna21.githubusers.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Fernanda Hasna on 26/09/2024.
 */

class HistoryActivity : BaseActivity<ActivityHistoryBinding, HistoryViewModel>(
    ActivityHistoryBinding::inflate,
    HistoryViewModel::class.java
) {

    override val viewModel: HistoryViewModel by viewModel()

    private lateinit var rowAdapter: UserRowAdapter
    private lateinit var layoutManager : LinearLayoutManager

    override fun setupData() {
        viewModel.getAllHistoriesFromRepository()
    }

    override fun setupUIWhenConfigChange() {
        setupToolbar()
        setupRecyclerView()
        super.setupUIWhenConfigChange()
    }

    override fun setupUIWithoutConfigChange() {
        viewModel.allHistories.observe(this){
            if (it == null || it.isEmpty()) {
//                val error: ArrayList<Int> = viewModel.errorResponse.type?.setError(this)!!
//                binding.mainResponse.layoutError.visibility = View.VISIBLE
//                binding.mainResponse.errorImage.setImageDrawable(
//                    AppCompatResources.getDrawable(
//                        this,
//                        error[0]
//                    )
//                )
//                binding.mainResponse.errorMessage.text = listOf(
//                    getString(error[1]),
//                    viewModel.errorResponse.code
//                ).joinToString(". Code:")
                binding.recyclerView.visibility = View.INVISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                rowAdapter.updateData(it)
            }
        }
    }

    private fun setupToolbar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.title = getString(R.string.history)
    }

    private fun setupRecyclerView(){
        binding.apply {
            rowAdapter = UserRowAdapter(this@HistoryActivity, arrayListOf())
            layoutManager = LinearLayoutManager(this@HistoryActivity)

            recyclerView.apply {
                layoutManager = this@HistoryActivity.layoutManager
                adapter = rowAdapter
                addItemDecoration(object : DividerItemDecoration(this@HistoryActivity, VERTICAL) {})
                setHasFixedSize(true)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.history_delete -> {
                DialogUtils.showConfirmationDialog(this, getString(R.string.warning_delete_history)){
                    _, _ -> viewModel.deleteAllHistories()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        setupData()
    }
}