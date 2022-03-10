package com.wac.labcollect.ui.fragment.firstScreen.homeTab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentSearchBinding
import com.wac.labcollect.ui.base.BaseFragment

class SearchFragment : BaseFragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater)
        binding.editSearch.requestFocus()
        return binding.root
    }

}