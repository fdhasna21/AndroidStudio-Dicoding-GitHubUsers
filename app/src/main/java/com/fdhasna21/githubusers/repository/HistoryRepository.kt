package com.fdhasna21.githubusers.repository

import android.content.Context
import com.fdhasna21.githubusers.model.entity.HistoryDb
import com.fdhasna21.githubusers.service.BaseAPI
import com.fdhasna21.githubusers.service.dao.HistoryDao


/**
 * Created by Fernanda Hasna on 26/09/2024.
 * Updated by Fernanda Hasna on 27/09/2024.
 */

class HistoryRepositoryImp(override var context: Context) : BaseRepository<BaseAPI, HistoryDao>(), HistoryRepository
{
    override val apiService = null
    override val daoService = roomDb.historyDao()

    override suspend fun getAllHistories(onSuccess: (List<HistoryDb>) -> Unit, onFailed: (String?) -> Unit) {
        localDbHandler(
            process = "getAllHistories",
            databaseCall = { daoService.getAllHistories() },
            onSuccess = onSuccess,
            onFailed = {
                onFailed(it.exception.message)
                onFailedHandler(it)
            }
        )
    }

    override suspend fun getHistoryByUserId(userId: Int, onSuccess: (HistoryDb?) -> Unit, onFailed: (String?) -> Unit) {
        localDbHandler(
            process = "getHistoryByUserId",
            databaseCall = { daoService.getHistoryByUserId(userId) },
            onSuccess = onSuccess,
            onFailed = {
                onFailed(it.exception.message)
                onFailedHandler(it)
            }
        )
    }

    override suspend fun insertOrUpdateHistory(history: HistoryDb, onSuccess: () -> Unit, onFailed: (String?) -> Unit) {
        localDbHandler(
            process = "insertOrUpdateHistory",
            databaseCall = { daoService.insertOrUpdateHistory(history) },
            onSuccess = { onSuccess() },
            onFailed = {
                onFailed(it.exception.message)
                onFailedHandler(it)
            }
        )
    }

    override suspend fun updateHistory(history: HistoryDb, onSuccess: () -> Unit, onFailed: (String?) -> Unit) {
        localDbHandler(
            process = "updateHistory",
            databaseCall = {
                daoService.updateHistory(
                    userId = history.userId,
                    username = history.username,
                    timestamp = history.timestamp,
                    photoProfile = history.photoProfile
                ) },
            onSuccess = { onSuccess() },
            onFailed = {
                onFailed(it.exception.message)
                onFailedHandler(it)
            }
        )
    }

    override suspend fun deleteHistory(userId: Int, onSuccess: () -> Unit, onFailed: (String?) -> Unit) {
        localDbHandler(
            process = "deleteHistory",
            databaseCall = { daoService.deleteHistory(userId) },
            onSuccess = { onSuccess() },
            onFailed = {
                onFailed(it.exception.message)
                onFailedHandler(it)
            }
        )
    }

    override suspend fun deleteAllHistories(onSuccess: () -> Unit, onFailed: (String?) -> Unit) {
        localDbHandler(
            process = "deleteAllHistories",
            databaseCall = { daoService.deleteAllHistories() },
            onSuccess = { onSuccess() },
            onFailed = {
                onFailed(it.exception.message)
                onFailedHandler(it)
            }
        )
    }
}

interface HistoryRepository {
    suspend fun getAllHistories(onSuccess: (List<HistoryDb>) -> Unit, onFailed: (String?) -> Unit)
    suspend fun getHistoryByUserId(userId: Int, onSuccess: (HistoryDb?) -> Unit, onFailed: (String?) -> Unit)
    suspend fun insertOrUpdateHistory(history: HistoryDb, onSuccess: () -> Unit, onFailed: (String?) -> Unit)
    suspend fun updateHistory(history: HistoryDb, onSuccess: () -> Unit, onFailed: (String?) -> Unit)
    suspend fun deleteHistory(userId: Int, onSuccess: () -> Unit, onFailed: (String?) -> Unit)
    suspend fun deleteAllHistories(onSuccess: () -> Unit, onFailed: (String?) -> Unit)
}