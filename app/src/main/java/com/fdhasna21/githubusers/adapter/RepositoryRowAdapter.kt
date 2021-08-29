package com.fdhasna21.githubusers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fdhasna21.githubusers.databinding.RowRecyclerRepositoryBinding
import com.fdhasna21.githubusers.dataclass.Account
import com.fdhasna21.githubusers.dataclass.Repository

class RepositoryRowAdapter(private val data:ArrayList<Repository>, val context: Context)
    :RecyclerView.Adapter<RepositoryRowAdapter.ViewHolder>(){
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(val binding: RowRecyclerRepositoryBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowRecyclerRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        //todo : implement bindviewholder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Repository)
    }
}