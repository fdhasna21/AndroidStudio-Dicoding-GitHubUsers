package com.fdhasna21.githubusers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fdhasna21.githubusers.dataclass.Account
import com.fdhasna21.githubusers.dataResolver.getImageID
import com.fdhasna21.githubusers.databinding.RowRecyclerAccountBinding

class AccountRowAdapter(private val data:ArrayList<Account>, val context: Context)
    :RecyclerView.Adapter<AccountRowAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(val binding: RowRecyclerAccountBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowRecyclerAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.binding.rowUsername.text = item.username
        holder.binding.rowName.text = item.name
        holder.binding.rowLocation.text = item.location
        holder.binding.rowAccount.setOnClickListener {
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
        fun onItemClicked(data: Account)
    }
}