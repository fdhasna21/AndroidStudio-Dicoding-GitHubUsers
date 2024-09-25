package com.fdhasna21.githubusers.repository

import android.content.Context
import com.fdhasna21.githubusers.service.api.BaseAPI

/**
 * Created by Fernanda Hasna on 26/09/2024.
 */

class GeneralRepositoryImp(override var context: Context) : BaseRepository() {
    override val apiService: BaseAPI = serverAPI<BaseAPI>()
}