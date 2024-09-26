package com.fdhasna21.githubusers.service.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fdhasna21.githubusers.model.entity.HistoryDb
import com.fdhasna21.githubusers.service.BaseDao
import com.fdhasna21.githubusers.service.RoomService

/**
 * Created by Fernanda Hasna on 26/09/2024.
 */

@Dao
interface HistoryDao : BaseDao<HistoryDb> {
    @Query("SELECT * FROM ${RoomService.TB_HISTORY} ORDER BY ${RoomService.TIMESTAMP} DESC")
    suspend fun getAllHistories(): List<HistoryDb>

    @Query("SELECT * FROM ${RoomService.TB_HISTORY} WHERE userId = :userId LIMIT 1")
    suspend fun getHistoryByUserId(userId: Int): HistoryDb?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistory(history: HistoryDb)

    @Delete
    suspend fun deleteHistory(history: HistoryDb)

    @Query("DELETE FROM ${RoomService.TB_HISTORY}")
    suspend fun deleteAllHistories()
}