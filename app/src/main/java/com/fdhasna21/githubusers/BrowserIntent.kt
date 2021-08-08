package com.fdhasna21.githubusers

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.lang.String

fun browserIntent(url: kotlin.String, context: Context){
    val intent = Intent()
    intent.addCategory(Intent.CATEGORY_BROWSABLE)
    intent.action = Intent.ACTION_VIEW
    intent.data = Uri.parse(String.format(url))
    context.startActivity(intent)
}