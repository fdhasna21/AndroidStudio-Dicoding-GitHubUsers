package com.fdhasna21.githubusers.utility

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import com.fdhasna21.githubusers.R
import com.fdhasna21.githubusers.utility.type.ErrorType
import java.io.File
import java.io.FileOutputStream
import kotlin.math.ln
import kotlin.math.pow

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */

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

    fun saveImageToCache(context: Context, bitmap: Bitmap, imageName: String): String {
        val cacheDir = context.cacheDir
        val imageFile = File(cacheDir, context.getString(R.string.app_name).replace(".","")+"-$imageName.jpg")

        FileOutputStream(imageFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out) // Adjust format and quality as needed
        }

        return imageFile.absolutePath // Return the file path
    }

    fun saveImageToCache(context: Context, imageView: ImageView, imageName: String): String {
        val cacheDir = context.cacheDir
        val imageFile = File(cacheDir, context.getString(R.string.app_name).replace(".","")+"-$imageName.jpg")

        FileOutputStream(imageFile).use { out ->
            getBitmapFromImageView(imageView)?.compress(Bitmap.CompressFormat.JPEG, 100, out) // Adjust format and quality as needed
        }

        return imageFile.absolutePath // Return the file path
    }

    fun loadImageFromCache(path: String): Bitmap? {
        return BitmapFactory.decodeFile(path)
    }

    fun getBitmapFromImageView(imageView: ImageView): Bitmap? {
        val drawable = imageView.drawable
        if (drawable is BitmapDrawable) {
            return drawable.bitmap // Return the existing bitmap
        } else {
            // If drawable is not a BitmapDrawable, create a bitmap from the view
            val bitmap = Bitmap.createBitmap(imageView.width, imageView.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            imageView.layout(0, 0, imageView.width, imageView.height)
            imageView.draw(canvas)
            return bitmap
        }
    }
}