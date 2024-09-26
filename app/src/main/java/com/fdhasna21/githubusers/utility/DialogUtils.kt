package com.fdhasna21.githubusers.utility

import android.content.Context
import android.content.DialogInterface
import com.fdhasna21.githubusers.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Created by Fernanda Hasna on 24/09/2024.
 */

object DialogUtils {
    private fun builder(context: Context): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(context, R.style.Theme_Material3_Dark_Dialog_Alert)
    }

    @JvmStatic
    fun showPositiveDialog(
        context: Context,
        title: String,
        content: String,
        onClickListener: DialogInterface.OnClickListener?
    ) {
        builder(context).apply {
            setTitle(title)
            setMessage(content)
            setPositiveButton(context.getString(R.string.ok), onClickListener)
            show()
        }
    }

    fun showWarningDialog(
        context: Context,
        content: String,
        onClickListener: DialogInterface.OnClickListener?
    ) {
        builder(context).apply {
            setTitle(context.getString(R.string.warning))
            setMessage(content)
            setPositiveButton(context.getString(R.string.ok), onClickListener)
            setCancelable(false)
            show()
        }
    }

    fun showConfirmationDialog(
        context: Context,
        content: String,
        positiveClick: DialogInterface.OnClickListener?
    ) {
        builder(context).apply {
            setTitle(context.getString(R.string.confirmation))
            setMessage(content)
            setPositiveButton(context.getString(R.string.ok), positiveClick)
            setNegativeButton(context.getString(R.string.cancel), null)
            show()
        }
    }
}