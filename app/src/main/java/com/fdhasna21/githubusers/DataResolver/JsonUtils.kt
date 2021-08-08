package com.fdhasna21.githubusers.DataResolver

import android.content.Context
import com.fdhasna21.githubusers.R
import java.io.IOException

fun getJsonDataFromFile(context: Context, fileName :String): String?{
    val jsonString : String
    try {
//        jsonString = context.assets.open(fileName).bufferedReader().use {it.readText()}
        jsonString = context.resources.openRawResource(R.raw.githubuser).bufferedReader().use {it.readText()}
    } catch (ioException : IOException){
        ioException.printStackTrace()
        return null
    }
    return jsonString
}