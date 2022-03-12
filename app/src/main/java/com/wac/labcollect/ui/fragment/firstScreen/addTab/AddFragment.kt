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
        binding.createTest.setOnClickListener {
            context?.let {
                Timber.e("Create new template")
                val action = FirstScreenFragmentDirections.actionFirstScreenFragmentToCreateTestFragment()
                this.findNavController().navigate(action)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.add)
    }

}