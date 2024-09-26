package com.fdhasna21.githubusers.model.entity

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.fdhasna21.githubusers.service.RoomService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by Fernanda Hasna on 26/09/2024.
 */

open class BaseDb(
    @PrimaryKey(autoGenerate = true)
    open val id: Long = 0,
    @ColumnInfo(defaultValue = RoomService.COLUMN_TIMESTAMP)
    open val timestamp: Long = System.currentTimeMillis()
) {
    fun Long.convertAsStringTimestamp(): String {
        val currentTime = System.currentTimeMillis()
        val date = Date(timestamp)

        val timeDifferenceInMillis = currentTime - timestamp

        return when {
            timeDifferenceInMillis < 24 * 60 * 60 * 1000 -> {
                val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
                sdf.format(date).uppercase(Locale.getDefault())
            }
            timeDifferenceInMillis < 365 * 24 * 60 * 60 * 1000 -> {
                val sdf = SimpleDateFormat("d MMM", Locale.getDefault())
                sdf.format(date)
            }
            else -> {
                val sdf = SimpleDateFormat("MMM yy", Locale.getDefault())
                sdf.format(date)
            }
        }
    }
}