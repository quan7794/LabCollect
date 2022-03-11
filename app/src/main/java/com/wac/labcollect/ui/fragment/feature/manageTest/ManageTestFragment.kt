package com.wac.labcollect.ui.fragment.feature.manageTest

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.wac.labcollect.databinding.ManageTestFragmentBinding
import com.wac.labcollect.ui.base.BaseFragment

class ManageTestFragment : BaseFragment<ManageTestFragmentBinding>() {

    private val viewModel: ManageTestViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(backPressCallback)
    }

    private val backPressCallback = object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val action = ManageTestFragmentDirections.toFirstScreenFragment()
            binding.root.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        backPressCallback.remove()
        super.onDestroyView()
    }
}