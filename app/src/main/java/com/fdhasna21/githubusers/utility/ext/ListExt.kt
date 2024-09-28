package com.fdhasna21.githubusers.utility.ext

/**
 * Created by Fernanda Hasna on 28/09/2024.
 */

fun <T, R> List<T>.convertToArrayList(transform: (T) -> R): ArrayList<R> {
    return ArrayList(this.map(transform))
}