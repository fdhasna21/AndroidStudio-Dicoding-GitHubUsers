package com.fdhasna21.githubusers.utility.type

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.fdhasna21.githubusers.adapter.RepositoryRowAdapter
import com.fdhasna21.githubusers.adapter.UserRowAdapter
import com.fdhasna21.githubusers.model.response.RepoResponse
import com.fdhasna21.githubusers.model.response.UserResponse

enum class DataType {
    USER {
        override fun getAdapter(data: ArrayList<*>, context: Context): RecyclerView.Adapter<*> {
            return UserRowAdapter(data as ArrayList<UserResponse>, context)
        }

    },
    REPOSITORY {
        override fun getAdapter(data: ArrayList<*>, context: Context): RecyclerView.Adapter<*> {
            return RepositoryRowAdapter(data as ArrayList<RepoResponse>, context)
        }
    };

    abstract fun getAdapter(data:ArrayList<*>, context: Context) : RecyclerView.Adapter<*>
}