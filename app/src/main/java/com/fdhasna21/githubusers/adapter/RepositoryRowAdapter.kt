package com.fdhasna21.githubusers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fdhasna21.githubusers.databinding.RowRecyclerRepositoryBinding
import com.fdhasna21.githubusers.model.RepoResponse
import com.fdhasna21.githubusers.utility.DataUtils
import com.fdhasna21.githubusers.utility.IntentUtils

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */

class RepositoryRowAdapter(val data:ArrayList<RepoResponse>, val context: Context)
    :RecyclerView.Adapter<RepositoryRowAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: RowRecyclerRepositoryBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowRecyclerRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val view = arrayListOf(
            holder.binding.rowRepoDesc,
            holder.binding.rowRepoLanguage,
        )

        holder.binding.rowUsername.text = item.owner?.username
        holder.binding.rowRepoName.text = item.repo_name
        holder.binding.rowRepoDesc.text = item.description
        holder.binding.rowRepoLanguage.text = item.language
        holder.binding.rowRepoStar.text = DataUtils().withSuffix(item.star_count!!)
        holder.binding.rowRepoWatcher.text = DataUtils().withSuffix(item.watcher_count!!)

        Glide.with(context)
            .load(item.owner?.photo_profile)
            .circleCrop()
            .into(holder.binding.rowImage)

        holder.binding.rowRepository.setOnClickListener {
            IntentUtils(context).openBrowser("https://github.com/${item.owner!!.username}/${item.repo_name}")
        }

        view.forEach {
            if(it.text.isNullOrBlank() || it.text.isNullOrEmpty()){
                it.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}