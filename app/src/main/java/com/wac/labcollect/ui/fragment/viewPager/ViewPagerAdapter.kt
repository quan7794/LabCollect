package com.wac.labcollect.ui.fragment.viewPager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int  = 4

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeFragment()
            1 -> SearchFragment()
            2 -> AddFragment()
            3 -> ProfileFragment()
            else -> Fragment()
        }
    }
}