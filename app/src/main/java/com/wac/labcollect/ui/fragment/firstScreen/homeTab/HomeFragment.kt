package com.wac.labcollect.ui.fragment.firstScreen.homeTab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentHomeBinding
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.ui.activity.mainActivity.MainViewModel
import com.wac.labcollect.ui.fragment.firstScreen.FirstScreenFragmentDirections

class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.apply {
            shortcutSearch.setOnClickListener {
                val action = FirstScreenFragmentDirections.actionFirstScreenFragmentToSearchFragment()
                view?.let { view -> Navigation.findNavController(view).navigate(action) }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}