package com.wac.labcollect.ui.fragment.firstScreen.addTab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentAddBinding
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.ui.fragment.firstScreen.FirstScreenFragmentDirections
import timber.log.Timber

class AddFragment : BaseFragment(R.layout.fragment_add) {

    private var _binding: FragmentAddBinding? = null
    private val binding: FragmentAddBinding
    get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddBinding.inflate(inflater)
        binding.createTest.setOnClickListener {
            context?.let {
                Timber.e("Create new template")
                val action = FirstScreenFragmentDirections.actionFirstScreenFragmentToCreateTestFragment()
                this.findNavController().navigate(action)
            }
        }
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}