package com.fdhasna21.githubusers.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fdhasna21.githubusers.activity.UserDetailActivity
import com.fdhasna21.githubusers.databinding.RowRecyclerUserBinding
import com.fdhasna21.githubusers.model.response.UserResponse
import com.fdhasna21.githubusers.utility.DataUtils
import com.fdhasna21.githubusers.utility.Key

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 * Updated by Fernanda Hasna on 27/09/2024.
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
                    if(onClickListener != null) {
                        val cachePath = DataUtils().saveImageToCache(context, holder.binding.rowImage, username)
                        item.imageCachePath = cachePath
                        onClickListener?.invoke(item)
                    }

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

        item.photoProfile?.let {
            Glide.with(context)
                .load(it)
                .circleCrop()
                .into(holder.binding.rowImage)
        }

        item.imageCachePath?.let{
            Glide.with(context)
                .load(DataUtils().loadImageFromCache(it))
                .circleCrop()
                .into(holder.binding.rowImage)
        }

        item.timestampAsString?.let{
            holder.binding.rowTimestamp.text = it
            holder.binding.rowTimestamp.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(newData: ArrayList<UserResponse>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(user: UserResponse, position: Int) {
        data.add(position, user)
        notifyItemInserted(position)
    }
}

abstract class UserItemSwipeCallback(
    private val adapter: UserRowAdapter
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    abstract fun onItemSwipeListener(user: UserResponse, position: Int)
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        adapter.removeItem(position)
        onItemSwipeListener(adapter.data[position], position)
    }
}