package com.fdhasna21.githubusers.DataResolver

import android.content.Context

fun getImageID(imageName: String, context: Context): Int {
    return context.resources.getIdentifier(imageName, "drawable", context.packageName)
}