package com.fdhasna21.githubusers.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fdhasna21.githubusers.fragment.TabLayoutFragment

class ViewPagerAdapter(activity : AppCompatActivity) : FragmentStateAdapter(activity) {
    private var totalFragments: Int = 0
    constructor(activity: AppCompatActivity, totalFragments:Int) : this(activity){
        this.totalFragments = totalFragments
    }

    constructor(activity: AppCompatActivity, dataFragment:ArrayList<*>):this(activity){
        this.totalFragments = dataFragment.size
    }

    override fun getItemCount(): Int = totalFragments

    override fun createFragment(position: Int): Fragment = TabLayoutFragment()
}