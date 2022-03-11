package com.wac.labcollect.ui.fragment.feature.manageTemplate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wac.labcollect.MainApplication
import com.wac.labcollect.databinding.TemplateManageFragmentBinding
import com.wac.labcollect.ui.base.BaseFragment
import timber.log.Timber

class ManageTemplateFragment : BaseFragment<TemplateManageFragmentBinding>() {
    private val templateAdapter: TemplateListAdapter by lazy { TemplateListAdapter() }
    private val viewModel: ManageTemplateViewModel by viewModels { ManageTemplateViewModelFactory(testRepository) }

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

}