package com.wac.labcollect.ui.fragment.firstScreen

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wac.labcollect.ui.fragment.firstScreen.addTab.AddFragment
import com.wac.labcollect.ui.fragment.firstScreen.homeTab.HomeFragment
import com.wac.labcollect.ui.fragment.firstScreen.profileTab.ProfileFragment

class ViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int  = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeFragment()
            1 -> AddFragment()
            2 -> ProfileFragment()
            else -> Fragment()
        }
    }
}