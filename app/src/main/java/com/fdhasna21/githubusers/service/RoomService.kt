package com.fdhasna21.githubusers.service

import android.content.Context
import android.util.Log
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fdhasna21.githubusers.BuildConfig
import com.fdhasna21.githubusers.model.entity.HistoryDb
import com.fdhasna21.githubusers.service.dao.HistoryDao
import com.fdhasna21.githubusers.utility.ext.TAG
import java.util.concurrent.Executors

/**
 * Created by Fernanda Hasna on 26/09/2024.
 *  * Updated by Fernanda Hasna on 28/09/2024.
 */

@Database(entities = [HistoryDb::class], version = 1, exportSchema = false)
abstract class RoomService : RoomDatabase(){
    abstract fun historyDao(): HistoryDao
    companion object {
        const val DB_GIT = "db_git"
        const val TB_HISTORY = "tb_history"

        const val COLUMN_TIMESTAMP = "CURRENT_TIMESTAMP"
        const val USER_ID = "userId"
        const val TIMESTAMP = "timestamp"

        @Volatile
        private var INSTANCE: RoomService? = null

        fun getDatabase(context: Context): RoomService {
            return INSTANCE ?: synchronized(this) {
                val builder = Room.databaseBuilder(
                    context.applicationContext,
                    RoomService::class.java,
                    DB_GIT
                )
                    .setJournalMode(JournalMode.TRUNCATE)
                    .setTransactionExecutor(Executors.newSingleThreadExecutor())
                    .setQueryExecutor(Executors.newSingleThreadExecutor())
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.d(TAG, "Database $DB_GIT created.")
                        }
                    })

                if (BuildConfig.DEBUG) {
                    builder.allowMainThreadQueries()
                }

                val instance = builder.build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Dao
interface BaseDao<T> {
}