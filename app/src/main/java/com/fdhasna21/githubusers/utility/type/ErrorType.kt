package com.fdhasna21.githubusers.utility.type

import android.content.Context
import com.fdhasna21.githubusers.R

enum class ErrorType {
    LOST_CONNECTION {
        override fun setError(context: Context): ArrayList<Int> {
            return arrayListOf(
                R.drawable.error_lost_connection,
                R.string.error_lost_connection
            )
        }
    },
    DATA_EMPTY {
        override fun setError(context: Context): ArrayList<Int> {
            return arrayListOf(
                R.drawable.error_not_found,
                R.string.error_not_found)
        }
    },
    NOT_MODIFIED{
        override fun setError(context: Context): ArrayList<Int> {
            return arrayListOf(
                R.drawable.error_forbidden,
                R.string.error_others)
        }

    },
    FORBIDDEN{
        override fun setError(context: Context): ArrayList<Int> {
            return arrayListOf(
                R.drawable.error_forbidden,
                R.string.error_forbidden)
        }

    },
    UNPROCESSABLE_ENTITY {
        override fun setError(context: Context): ArrayList<Int> {
            return arrayListOf(
                R.drawable.error_unprocessable,
                R.string.error_unprocessable
            )
        }

    },
    SERVER_ERROR{
        override fun setError(context: Context): ArrayList<Int> {
            return arrayListOf(
                R.drawable.error_server,
                R.string.error_server
            )
        }

    },
    OTHERS{
        override fun setError(context: Context): ArrayList<Int> {
            return arrayListOf(
                R.drawable.error_others,
                R.string.error_others
            )
        }
    };

    abstract fun setError(context: Context) : ArrayList<Int>
}