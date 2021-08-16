package com.fdhasna21.githubusers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fdhasna21.githubusers.dataResolver.User
import com.fdhasna21.githubusers.dataResolver.getImageID
import com.fdhasna21.githubusers.databinding.RowRecyclerBinding

class RowAdapter(private val data:ArrayList<User>, val context: Context)
    :RecyclerView.Adapter<RowAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(val binding: RowRecyclerBinding):RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.binding.rowUsername.text = item.username
        holder.binding.rowName.text = item.name
        holder.binding.rowCompany.text = item.company
        holder.binding.rowUser.setOnClickListener {
            onItemClickCallback.onItemClicked(data[holder.adapterPosition])
        }
        Glide.with(context)
            .load(getImageID((item.avatar.toString()).substringAfterLast("/"), context))
            .into(holder.binding.rowImage)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}