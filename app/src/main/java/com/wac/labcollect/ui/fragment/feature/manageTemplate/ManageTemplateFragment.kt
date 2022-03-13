package com.wac.labcollect.ui.fragment.feature.manageTemplate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.wac.labcollect.databinding.TemplateManageFragmentBinding
import com.wac.labcollect.domain.models.Template
import com.wac.labcollect.ui.base.BaseFragment
import kotlinx.coroutines.launch
import timber.log.Timber

class ManageTemplateFragment : BaseFragment<TemplateManageFragmentBinding>(), TemplateListAdapter.CreateTestCallback {
    private val templateAdapter: TemplateListAdapter by lazy { TemplateListAdapter(createTestCallback = this, isCreateTest = args.testUniqueName != null) }
    private val viewModel: ManageTemplateViewModel by viewModels { ManageTemplateViewModelFactory(testRepository) }
    private val args: ManageTemplateFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        templateAdapter.setIsCreateTest(args.testUniqueName != null)
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

    override fun onClick(template: Template) {
        //Create test with this template.
        lifecycleScope.launch {
            viewModel.updateTest(args.testUniqueName!!, template)
            findNavController().navigate(ManageTemplateFragmentDirections.actionManageTemplateFragmentToManageTestFragment(args.testUniqueName!!))
        }
    }

}