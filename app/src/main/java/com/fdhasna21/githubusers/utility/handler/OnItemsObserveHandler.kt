package com.fdhasna21.githubusers.utility.handler

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fdhasna21.githubusers.databinding.LayoutDataHandlerResponseBinding

/**
 * Created by Fernanda Hasna on 27/09/2024.
 */

fun <T> LiveData<ArrayList<T>>.onItemsObserveHandler(
    context: Context,
    lifecycleOwner : LifecycleOwner,
    recyclerView: RecyclerView,
    onSuccessed : (ArrayList<T>) -> Unit,
    dataHandlerResponseBinding : LayoutDataHandlerResponseBinding? = null,
    refreshRecyclerViewBinding : SwipeRefreshLayout? = null,
    onFailed : (() -> Unit)? = null,
    onDone : (() -> Unit)? = null
) {
    this.observe(lifecycleOwner) {
        if (it == null || it.isEmpty()) {
//            recyclerView.visibility = View.INVISIBLE
//            val error = ErrorType.DATA_EMPTY.setError(context)
//            dataHandlerResponseBinding?.layoutError?.visibility = View.VISIBLE
//            dataHandlerResponseBinding?.errorImage?.setImageDrawable(
//                AppCompatResources.getDrawable(
//                    context,
//                    error[0]
//                )
//            )
//            dataHandlerResponseBinding?.errorMessage?.text = context.getString(error[1])
//            onFailed?.invoke()
        } else {
            recyclerView.visibility = View.VISIBLE
            dataHandlerResponseBinding?.layoutError?.visibility = View.GONE
            onSuccessed(it)
        }
        dataHandlerResponseBinding?.progressCircular?.visibility = View.INVISIBLE
        refreshRecyclerViewBinding?.isRefreshing = false
        onDone?.invoke()
    }
}