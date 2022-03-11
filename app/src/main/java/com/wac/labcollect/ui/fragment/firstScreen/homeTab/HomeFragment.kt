package com.wac.labcollect.ui.fragment.firstScreen.homeTab

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.wac.labcollect.databinding.FragmentHomeBinding
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.ui.fragment.firstScreen.FirstScreenFragmentDirections

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            shortcutSearch.setOnClickListener {
                val action = FirstScreenFragmentDirections.actionFirstScreenFragmentToSearchFragment()
                view.let { view -> Navigation.findNavController(view).navigate(action) }
            }
        }

    }
}