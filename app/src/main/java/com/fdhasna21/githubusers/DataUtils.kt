package com.fdhasna21.githubusers

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import com.fdhasna21.githubusers.dataclass.ErrorType
import kotlin.math.ln
import kotlin.math.pow

class DataUtils {
    fun withSuffix(count: Long): String {
        if (count < 1000) return "" + count
        val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
        return String.format(
            "%.1f %c",
            count / 1000.0.pow(exp.toDouble()),
            "kMGTPE"[exp - 1]
        )
    }

    @SuppressLint("ResourceAsColor")
    fun getBitmapFromView(view: View): Bitmap {
        val output = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val bg = view.background

        if (bg != null) {
            bg.draw(canvas)
        } else {
            canvas.drawColor(android.R.color.white)
        }
        view.draw(canvas)
        return output
    }

    fun checkErrorType(code:Int?) : ErrorType {
        return when(code){
            null -> ErrorType.LOST_CONNECTION
            200 -> ErrorType.DATA_EMPTY
            304 -> ErrorType.NOT_MODIFIED
            401, 422 -> ErrorType.UNPROCESSABLE_ENTITY
            403 -> ErrorType.FORBIDDEN
            in 500..599 -> ErrorType.SERVER_ERROR
            else -> ErrorType.OTHERS
        }
    }
}