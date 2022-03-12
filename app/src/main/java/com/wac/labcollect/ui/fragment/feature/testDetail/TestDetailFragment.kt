package com.wac.labcollect.ui.fragment.feature.testDetail

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.wac.labcollect.databinding.ManageTestFragmentBinding
import com.wac.labcollect.domain.models.Test
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.utils.Utils.observeOnce

class TestDetailFragment : BaseFragment<ManageTestFragmentBinding>() {

    private val viewModel: ManageTestViewModel by viewModels { TestDetailViewModelFactory(testRepository) }
    private val args: TestDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(backPressCallback)
        viewModel.getTest(args.testUniqueName).observeOnce {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it.title
            setupUi(it)
        }
    }

    private fun setupUi(test: Test) {
        binding.apply {
            name.text = test.title
            type.text = test.type
            startTime.text = test.startTime
            endTime.text = test.endTime
        }
    }

    private val backPressCallback = object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val action = TestDetailFragmentDirections.toFirstScreenFragment()
            binding.root.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        backPressCallback.remove()
        super.onDestroyView()
    }
}