package com.fdhasna21.githubusers.utility.type

import android.content.res.Resources

/**
 * Created by Fernanda Hasna on 26/09/2024.
 */

fun Int.dpToPx(): Int {
    val density = Resources.getSystem().displayMetrics.density
    return (this * density).toInt()
}