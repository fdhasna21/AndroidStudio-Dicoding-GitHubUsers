package com.fdhasna21.githubusers.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fdhasna21.githubusers.activity.UserDetailActivity
import com.fdhasna21.githubusers.databinding.RowRecyclerUserBinding
import com.fdhasna21.githubusers.model.response.UserResponse
import com.fdhasna21.githubusers.utility.Key

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */

class UserRowAdapter(
    val context: Context,
    var data:ArrayList<UserResponse>,
    private var onClickListener : ((user: UserResponse) -> Unit)? = null,
    private var onLongClickListener : ((user: UserResponse) -> Unit)? = null)
    :RecyclerView.Adapter<UserRowAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RowRecyclerUserBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowRecyclerUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.binding.rowUsername.text = item.username
        item.id?.let {
            item.username?.let { username ->
                holder.binding.rowUser.setOnClickListener {
                    onClickListener?.invoke(item)

                    val intent = Intent(context, UserDetailActivity::class.java)
                    intent.putExtra(Key.INTENT.USERNAME, username)
                    context.startActivity(intent)
                }

                onLongClickListener?.let { listener ->
                    holder.binding.rowUser.setOnLongClickListener {
                        listener.invoke(item)
                        true
                    }
                }
            }
        }

        Glide.with(context)
            .load(item.photoProfile)
            .circleCrop()
            .into(holder.binding.rowImage)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(newData: ArrayList<UserResponse>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}