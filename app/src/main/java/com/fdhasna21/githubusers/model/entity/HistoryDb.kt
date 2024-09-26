package com.fdhasna21.githubusers.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fdhasna21.githubusers.model.response.UserResponse
import com.fdhasna21.githubusers.service.RoomService

/**
 * Created by Fernanda Hasna on 26/09/2024.
 */

@Entity(tableName = RoomService.TB_HISTORY)
data class HistoryDb (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val photoProfile: String,
    @ColumnInfo(defaultValue = RoomService.TIMESTAMP)
    val timestamp: Long = System.currentTimeMillis()
)

fun HistoryDb.toUserResponse() : UserResponse{
    return UserResponse(
        username = this.username
    )
}