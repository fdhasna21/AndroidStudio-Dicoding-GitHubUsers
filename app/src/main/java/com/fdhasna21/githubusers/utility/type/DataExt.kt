package com.fdhasna21.githubusers.utility.type

import android.content.res.Resources

/**
 * Created by Fernanda Hasna on 26/09/2024.
 */

const val TAG = "fdhasna21-Git.Debug"
fun Int.dpToPx(): Int {
    val density = Resources.getSystem().displayMetrics.density
    return (this * density).toInt()
}

fun <T, R> List<T>.convertToArrayList(transform: (T) -> R): ArrayList<R> {
    return ArrayList(this.map(transform))
}