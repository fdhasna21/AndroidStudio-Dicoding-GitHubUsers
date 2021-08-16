package com.fdhasna21.githubusers.dataResolver

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

fun getImageID(imageName: String, context: Context): Int {
    return context.resources.getIdentifier(imageName, "drawable", context.packageName)
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