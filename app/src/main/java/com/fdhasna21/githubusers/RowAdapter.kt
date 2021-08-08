package com.fdhasna21.githubusers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fdhasna21.githubusers.DataResolver.User
import com.fdhasna21.githubusers.DataResolver.getImageID
import kotlinx.android.synthetic.main.row_recycler.view.*

class RowAdapter(val data:ArrayList<User>, val context: Context)
    :RecyclerView.Adapter<RowAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val userGH = itemView.row_user
        val usernameGH = itemView.row_username
        val nameGH = itemView.row_name
        val imageGH = itemView.row_image
        val companyGH = itemView.row_company
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_recycler, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data.get(position)
        holder.usernameGH.text = item.username
        holder.nameGH.text = item.name
        holder.companyGH.text = item.company
        holder.userGH.setOnClickListener {
            onItemClickCallback.onItemClicked(data[holder.adapterPosition])
        }
        Glide.with(context)
            .load(getImageID((item.avatar.toString()).substringAfterLast("/"), context))
            .into(holder.imageGH)
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