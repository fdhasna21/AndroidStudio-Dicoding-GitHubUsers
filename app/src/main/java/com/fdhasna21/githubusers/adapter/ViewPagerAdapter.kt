package com.fdhasna21.githubusers.adapter

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fdhasna21.githubusers.resolver.enumclass.DataType
import com.fdhasna21.githubusers.fragment.TabLayoutFragment

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
        Log.i("userDetailActivity", "createFragment: $fragments")
        fragments.add(fragment)
        return fragment
    }

    private fun getFragment(tab: Int) : TabLayoutFragment?{
//        Log.i("userDetailActivity", "getFragment: tab$tab size${fragments.size}")
        return when {
            tab < fragments.size -> fragments[tab]
            tab == fragments.size -> fragments[tab-1]
            else -> null
        }
    }

    private fun getAdapter(tab:Int) : RecyclerView.Adapter<*>?{
        return getFragment(tab)?.getAdapter()
    }

    fun updateAdapter(tab:Int){
        getFragment(tab)?.setUpdateData()
    }

    fun setFragmentError(tab:Int, visible:Boolean, drawableID:Int=0, messageID:Int=0, code:Int?=null){
        Log.i("userDetailActivity", "setFragmentError: fr ${getFragment(tab)}")
        getFragment(tab)?.let {
            if(visible){
                it.setError(drawableID, messageID, code)
            } else {
                it.setNotError()
            }
        }
    }
}