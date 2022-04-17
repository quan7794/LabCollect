package com.wac.labcollect.ui.fragment.feature.manageTemplate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.wac.labcollect.databinding.TemplateManageFragmentBinding
import com.wac.labcollect.domain.models.Template
import com.wac.labcollect.domain.models.Test
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.utils.Status
import com.wac.labcollect.utils.StatusControl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ManageTemplateFragment : BaseFragment<TemplateManageFragmentBinding>(), TemplateListAdapter.CreateTestCallback {
    private val templateAdapter: TemplateListAdapter by lazy { TemplateListAdapter(createTestCallback = this, isCreateTest = args.tempTest != null) }
    private val viewModel: ManageTemplateViewModel by viewModels { ManageTemplateViewModelFactory(testRepository, googleApiRepository) }
    private val args: ManageTemplateFragmentArgs by navArgs()
    private var tempTest: Test? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tempTest = args.tempTest
        initUi()
    }

    private fun initUi() {
        initProgress(viewModel, binding.loadingAnimation.id)
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
        viewModel.updateProgress(StatusControl(Status.LOADING))
        tempTest?.let { test ->
            lifecycleScope.launch(Dispatchers.IO) {
                test.spreadId = viewModel.createSpread(test)
                viewModel.createLocalTest(test)
                if (viewModel.updateTest(test.spreadId, template))
                    withContext(Dispatchers.Main) {
                        viewModel.updateProgress(StatusControl(Status.SUCCESS))
                        navigate(ManageTemplateFragmentDirections.actionManageTemplateFragmentToManageTestFragment(test.spreadId))
                    }
                else {
                    viewModel.updateProgress(StatusControl(Status.ERROR, message = "Error when add template to test."))
                }
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