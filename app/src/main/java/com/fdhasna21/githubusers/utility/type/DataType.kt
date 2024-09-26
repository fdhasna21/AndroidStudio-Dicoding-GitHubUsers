package com.fdhasna21.githubusers.utility.type

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.fdhasna21.githubusers.adapter.RepositoryRowAdapter
import com.fdhasna21.githubusers.adapter.UserRowAdapter
import com.fdhasna21.githubusers.model.response.RepoResponse
import com.fdhasna21.githubusers.model.response.UserResponse

enum class DataType {
    USER {
        override fun getAdapter(context: Context, data: ArrayList<*>): RecyclerView.Adapter<*> {
            return UserRowAdapter(context, data as ArrayList<UserResponse>)
        }

    },
    REPOSITORY {
        override fun getAdapter(context: Context, data: ArrayList<*>): RecyclerView.Adapter<*> {
            return RepositoryRowAdapter(context, data as ArrayList<RepoResponse>)
        }
    };

    abstract fun getAdapter(context: Context, data:ArrayList<*>) : RecyclerView.Adapter<*>
}