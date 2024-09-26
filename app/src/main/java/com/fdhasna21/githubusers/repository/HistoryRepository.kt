package com.fdhasna21.githubusers.repository

import android.content.Context
import com.fdhasna21.githubusers.model.entity.HistoryDb
import com.fdhasna21.githubusers.service.BaseAPI
import com.fdhasna21.githubusers.service.dao.HistoryDao


/**
 * Created by Fernanda Hasna on 26/09/2024.
 */

class HistoryRepositoryImp(override var context: Context) : BaseRepository<BaseAPI, HistoryDao>(), HistoryRepository
{
    override val apiService = null
    override val daoService = roomDb.historyDao()

    override suspend fun getAllHistories(onSuccess: (List<HistoryDb>) -> Unit) {
        localDbHandler(
            databaseCall = { daoService.getAllHistories() },
            onSuccess = onSuccess
        )
    }

    override suspend fun insertHistory(history: HistoryDb, onSuccess: () -> Unit) {
        localDbHandler(
            databaseCall = { daoService.insertHistory(history) },
            onSuccess = { onSuccess() }
        )
    }

    override suspend fun deleteHistory(history: HistoryDb, onSuccess: () -> Unit) {
        localDbHandler(
            databaseCall = { daoService.deleteHistory(history) },
            onSuccess = { onSuccess() }
        )
    }

    override suspend fun deleteAllHistories(onSuccess: () -> Unit) {
        localDbHandler(
            databaseCall = { daoService.deleteAllHistories() },
            onSuccess = { onSuccess() }
        )
    }
}

interface HistoryRepository {
    suspend fun getAllHistories(onSuccess: (List<HistoryDb>) -> Unit)
    suspend fun insertHistory(history: HistoryDb, onSuccess: () -> Unit)
    suspend fun deleteHistory(history: HistoryDb, onSuccess: () -> Unit)
    suspend fun deleteAllHistories(onSuccess: () -> Unit)
}