package com.wac.labcollect.ui.fragment.firstScreen.addTab

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentAddBinding
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.ui.fragment.firstScreen.FirstScreenFragmentDirections
import timber.log.Timber

class AddFragment : BaseFragment<FragmentAddBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtonClick()
    }

    private fun setupButtonClick() {
        binding.apply {
            createTest.setOnClickListener {
                context?.let {
                    Timber.e("Create new test")
                    this@AddFragment.findNavController().navigate(FirstScreenFragmentDirections.actionFirstScreenFragmentToCreateTestFragment())
                }
            }
            createTestTemplate.setOnClickListener {
                Timber.e("Create new template")
                this@AddFragment.findNavController().navigate(FirstScreenFragmentDirections.actionFirstScreenFragmentToCreateTemplateFragment(null))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.add)
    }

}