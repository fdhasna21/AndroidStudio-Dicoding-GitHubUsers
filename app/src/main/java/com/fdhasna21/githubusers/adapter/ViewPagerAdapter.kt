package com.fdhasna21.githubusers.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fdhasna21.githubusers.activity.fragment.TabLayoutFragment
import com.fdhasna21.githubusers.utility.type.DataType

/**
 * Updated by Fernanda Hasna on 26/09/2024.
 */

class ViewPagerAdapter(private var activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private var totalFragments: Int = 0
    private lateinit var dataFragment : ArrayList<ArrayList<*>>
    private var fragments : ArrayList<TabLayoutFragment> = arrayListOf()
    private lateinit var dataType : DataType

    constructor(activity: AppCompatActivity, dataFragment: ArrayList<ArrayList<*>>, dataType: DataType):this(activity){
        this.totalFragments = dataFragment.size
        this.activity = activity
        this.dataFragment = dataFragment
        this.dataType = dataType
    }

    override fun getItemCount(): Int = totalFragments

    override fun createFragment(position: Int): Fragment {
        val fragment = TabLayoutFragment(dataType.getAdapter(dataFragment[position], activity))
        fragments.add(fragment)
        return fragment
    }

    private fun getFragment(tab: Int) : TabLayoutFragment?{
        return when {
            tab < fragments.size -> fragments[tab]
            tab == fragments.size -> fragments[tab-1]
            else -> null
        }
    }

    private fun updateAdapter(tab:Int){
        getFragment(tab)?.setUpdateData()
    }

    private fun setFragmentError(tab:Int, visible:Boolean, drawableID:Int=0, messageID:Int=0, code:Int?=null){
        getFragment(tab)?.let {
            if(visible){
                it.setError(drawableID, messageID, code)
            } else {
                it.setNotError()
            }
        }
    }

    fun <T> updateData(tab: Int, newData: List<T>) {
        @Suppress("UNCHECKED_CAST")
        val dataList = dataFragment[tab] as ArrayList<T>
        dataList.clear()
        dataList.addAll(newData)
        updateAdapter(tab)
        setFragmentError(tab, false)
    }
}

fun <T> ViewPagerAdapter.observeData(
    lifecycleOwner: LifecycleOwner,
    liveData: LiveData<ArrayList<T>>,
    tabIndex: Int,
    errorHandler: (() -> Unit)? = null
) {
    liveData.observe(lifecycleOwner) { data ->
        if (data == null || data.isEmpty()) {
            errorHandler?.invoke()
        } else {
            updateData(tabIndex, data)
        }
    }
}