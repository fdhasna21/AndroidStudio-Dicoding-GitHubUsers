package com.fdhasna21.githubusers.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fdhasna21.githubusers.activity.UserDetailActivity
import com.fdhasna21.githubusers.databinding.RowRecyclerUserBinding
import com.fdhasna21.githubusers.dataclass.User


class UserRowAdapter(val data:ArrayList<User>, val context: Context)
    :RecyclerView.Adapter<UserRowAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RowRecyclerUserBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowRecyclerUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.binding.rowUsername.text = item.username
        holder.binding.rowUser.setOnClickListener {
            val intent = Intent(context, UserDetailActivity::class.java)
            intent.putExtra(UserDetailActivity.EXTRA_USER, item.username)
            context.startActivity(intent)
        }

        Glide.with(context)
            .load(item.photo_profile)
            .circleCrop()
            .into(holder.binding.rowImage)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addData(newData : ArrayList<User>){
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}