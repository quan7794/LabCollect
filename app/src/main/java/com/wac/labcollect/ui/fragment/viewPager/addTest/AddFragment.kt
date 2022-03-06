package com.wac.labcollect.ui.fragment.viewPager.addTest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentAddBinding
import com.wac.labcollect.ui.BaseFragment
import com.wac.labcollect.ui.fragment.FirstScreenFragmentDirections
import timber.log.Timber

class AddFragment : BaseFragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding: FragmentAddBinding
    get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddBinding.inflate(inflater)
        binding.createTest.setOnClickListener {
            context?.let { context ->
                MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.create_test)
                    .setMessage(R.string.create_test_message)
                    .setPositiveButton(R.string.create_new) { dialog, which ->
                        Timber.e("Create new template")
                        val action = FirstScreenFragmentDirections.actionFirstScreenFragmentToCreateTemplateFragment()
                        this.findNavController().navigate(action)
                    }
                    .setNegativeButton(R.string.select_existed_template) { dialog, which ->
                        Timber.e("Use existed template")
                    }
                    .setCancelable(false)
                    .show()
            }
        }
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}