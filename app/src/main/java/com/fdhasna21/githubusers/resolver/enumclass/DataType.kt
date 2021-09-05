package com.fdhasna21.githubusers.resolver.enumclass

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.fdhasna21.githubusers.adapter.RepositoryRowAdapter
import com.fdhasna21.githubusers.adapter.UserRowAdapter
import com.fdhasna21.githubusers.resolver.dataclass.Repository
import com.fdhasna21.githubusers.resolver.dataclass.User

enum class DataType {
    USER {
        override fun getAdapter(data: ArrayList<*>, context: Context): RecyclerView.Adapter<*> {
            return UserRowAdapter(data as ArrayList<User>, context)
        }

    },
    REPOSITORY {
        override fun getAdapter(data: ArrayList<*>, context: Context): RecyclerView.Adapter<*> {
            return RepositoryRowAdapter(data as ArrayList<Repository>, context)
        }
    };

    abstract fun getAdapter(data:ArrayList<*>, context: Context) : RecyclerView.Adapter<*>
}