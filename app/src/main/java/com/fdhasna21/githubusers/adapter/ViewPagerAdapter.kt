package com.fdhasna21.githubusers.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fdhasna21.githubusers.dataclass.DataType
import com.fdhasna21.githubusers.fragment.TabLayoutFragment

class ViewPagerAdapter(private var activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private var totalFragments: Int = 0
    private lateinit var dataFragment : ArrayList<ArrayList<*>>
    private var fragments : ArrayList<TabLayoutFragment> = arrayListOf()
    private lateinit var dataType : DataType

    constructor(activity: AppCompatActivity, dataFragment: ArrayList<ArrayList<*>>, dataType:DataType):this(activity){
        this.totalFragments = dataFragment.size
        this.activity = activity
        this.dataFragment = dataFragment
        this.dataType = dataType
    }

    fun updateAdapter(){
        fragments.forEach {
            val adapt = it.binding.tablayoutRecyclerView.adapter
            adapt?.notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = totalFragments

    override fun createFragment(position: Int): Fragment {
        fragments.add(TabLayoutFragment(dataType.getAdapter(dataFragment[position], activity)))
        return fragments[position]
    }
}