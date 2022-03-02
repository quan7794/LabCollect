package com.wac.labcollect.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crazylegend.viewbinding.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentFirstScreenBinding
import com.wac.labcollect.ui.fragment.viewPager.ViewPagerAdapter

class FirstScreenFragment : Fragment(R.layout.fragment_first_screen) {
    private val firstScreenTabItems = arrayListOf(R.string.home, R.string.search, R.string.add, R.string.profile )
    private val firstScreenBinding by viewBinding(FragmentFirstScreenBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_first_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstScreenBinding.apply {
            viewPager.adapter = ViewPagerAdapter(this@FirstScreenFragment)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = getString(firstScreenTabItems[position])
            }.attach()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FirstScreenFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
