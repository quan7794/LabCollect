package com.wac.labcollect.ui.fragment.feature.manageTemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wac.labcollect.MainApplication
import com.wac.labcollect.R
import com.wac.labcollect.databinding.TemplateManagerFragmentBinding
import com.wac.labcollect.ui.base.BaseFragment
import timber.log.Timber

class ManageTemplateFragment : BaseFragment(R.layout.template_manager_fragment) {
    private var _binding: TemplateManagerFragmentBinding?= null
    private val binding: TemplateManagerFragmentBinding
        get() = _binding!!
    private val templateAdapter: TemplateListAdapter by lazy { TemplateListAdapter() }
    private val viewModel: ManageTemplateViewModel by viewModels {
        ManageTemplateViewModelFactory((activity?.application as MainApplication).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.template_manager_fragment, container, false)
        _binding = TemplateManagerFragmentBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = templateAdapter
        }
        viewModel.templates.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Timber.e("$it")
                templateAdapter.dataSet = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}