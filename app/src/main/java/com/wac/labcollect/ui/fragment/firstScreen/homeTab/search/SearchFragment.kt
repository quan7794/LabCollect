package com.wac.labcollect.ui.fragment.firstScreen.homeTab.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentSearchBinding
import com.wac.labcollect.ui.base.BaseFragment

class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editSearch.requestFocus()

    }
}