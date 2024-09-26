package com.fdhasna21.githubusers.repository

import android.content.Context
import com.fdhasna21.githubusers.service.BaseAPI
import com.fdhasna21.githubusers.service.BaseDao

/**
 * Created by Fernanda Hasna on 26/09/2024.
 */

class GeneralRepositoryImp(override var context: Context) : BaseRepository<BaseAPI, BaseDao<*>>() {
    override val apiService = null
    override val daoService = null
}