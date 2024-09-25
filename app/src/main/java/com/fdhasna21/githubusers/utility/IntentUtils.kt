package com.fdhasna21.githubusers.utility

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */
class IntentUtils(val context: Context){
    fun openBrowser(url: String){
        val intent = Intent()
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(String.format(url))
        context.startActivity(intent)
    }

    fun openEmail(email:String){
        val intent = Intent()
        val emailSubject = "Hi from user of Git."
        val emailBody = "Hi there, the creator of Git. I want to know about you, can we catch up later?"
        intent.apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:$email?subject=${Uri.encode(emailSubject)}&body=${Uri.encode(emailBody)}")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        }
        context.startActivity(Intent.createChooser(intent, "Send email with"))
    }
}