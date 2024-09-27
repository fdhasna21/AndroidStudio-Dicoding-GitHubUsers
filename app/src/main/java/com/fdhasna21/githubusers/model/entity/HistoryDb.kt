package com.fdhasna21.githubusers.model.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fdhasna21.githubusers.model.response.UserResponse
import com.fdhasna21.githubusers.service.RoomService

/**
 * Created by Fernanda Hasna on 26/09/2024.
 * Updated by Fernanda Hasna on 27/09/2024.
 */

@Entity(
    tableName = RoomService.TB_HISTORY,
    indices = [Index(value = [RoomService.USER_ID], unique = true)]
)
data class HistoryDb (
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    val userId: Int,
    val username: String,
    val photoProfile: String,
    override val timestamp: Long = System.currentTimeMillis()
) : BaseDb(id, timestamp)

fun HistoryDb.toUserResponse() : UserResponse{
    return UserResponse(
        username = this.username,
        id = this.userId,
        imageCachePath = this.photoProfile,
        timestampAsString = this.timestamp.convertAsStringTimestamp(),
        timestampAsLong = this.timestamp
    )
}