package com.fdhasna21.githubusers.utility

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import com.fdhasna21.githubusers.R
import com.fdhasna21.githubusers.activity.BaseActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Created by Fernanda Hasna on 24/09/2024.
 */

object DialogUtils {
    private fun builder(context: AppCompatActivity): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(context, R.style.Theme_Material3_Dark_Dialog_Alert)
    }

    private fun MaterialAlertDialogBuilder.showDialog(context: AppCompatActivity){
        val activity = context as BaseActivity<*,*>
        if(!activity.isFinishing){
            this.show()
        }
    }

    @JvmStatic
    fun showPositiveDialog(
        context: AppCompatActivity,
        title: String?,
        content: String?,
        onClickListener: DialogInterface.OnClickListener?
    ) {
        builder(context).apply {
            setTitle(title)
            setMessage(content)
            setPositiveButton(context.getString(R.string.ok), onClickListener)
            showDialog(context)
        }
    }

    fun showWarningDialog(
        context: AppCompatActivity,
        content: String?,
        onClickListener: DialogInterface.OnClickListener?
    ) {
        builder(context).apply {
            setTitle(context.getString(R.string.warning))
            setMessage(content)
            setPositiveButton(context.getString(R.string.ok), onClickListener)
            setCancelable(false)
            showDialog(context)
        }
    }

    fun showConfirmationDialog(
        context: AppCompatActivity,
        content: String?,
        positiveClick: DialogInterface.OnClickListener?
    ) {
        builder(context).apply {
            setTitle(context.getString(R.string.confirmation))
            setMessage(content)
            setPositiveButton(context.getString(R.string.ok), positiveClick)
            setNegativeButton(context.getString(R.string.cancel), null)
            showDialog(context)
        }
    }
}