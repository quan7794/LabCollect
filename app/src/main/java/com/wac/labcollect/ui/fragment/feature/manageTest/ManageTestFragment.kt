package com.wac.labcollect.ui.fragment.feature.manageTest

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.wac.labcollect.databinding.ManageTestFragmentBinding
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.utils.Utils.observeOnce

class ManageTestFragment : BaseFragment<ManageTestFragmentBinding>() {

    private val viewModel: ManageTestViewModel by viewModels { ManageTestViewModelFactory(testRepository) }
    private val args: ManageTestFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(backPressCallback)
        viewModel.getTest(args.testUniqueName).observeOnce {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it.title
        }
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