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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ManageTemplateFragment : BaseFragment<TemplateManageFragmentBinding>(), TemplateListAdapter.CreateTestCallback {
    private val templateAdapter: TemplateListAdapter by lazy { TemplateListAdapter(createTestCallback = this, isCreateTest = args.spreadId != null) }
    private val viewModel: ManageTemplateViewModel by viewModels { ManageTemplateViewModelFactory(testRepository, googleApiRepository) }
    private val args: ManageTemplateFragmentArgs by navArgs()

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

    override fun onClick(template: Template) {
        //Create test with this template.
        lifecycleScope.launch(Dispatchers.IO) {
            if (viewModel.updateTest(args.spreadId!!, template))
                withContext(Dispatchers.Main) {
                    navigate(ManageTemplateFragmentDirections.actionManageTemplateFragmentToManageTestFragment(args.spreadId!!))
                }
            else {
                Timber.e("Error when add template to test.")
                }
        }
    }

    override fun onDestroyView() {
        binding.recyclerView.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {}
            override fun onViewDetachedFromWindow(v: View?) { binding.recyclerView.adapter = null }
        })
        super.onDestroyView()
    }
}