package com.wac.labcollect.ui.fragment.feature.manageTest

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wac.labcollect.R

class ManageTestFragment : Fragment() {

    companion object {
        fun newInstance() = ManageTestFragment()
    }

    private lateinit var viewModel: ManageTestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.manage_test_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ManageTestViewModel::class.java)
        // TODO: Use the ViewModel
    }

}