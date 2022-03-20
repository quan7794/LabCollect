package com.wac.labcollect.ui.fragment.firstScreen.homeTab

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wac.labcollect.R
import com.wac.labcollect.data.repository.sheet.GoogleApiConstant.ROOT_DIR_ID
import com.wac.labcollect.databinding.FragmentHomeBinding
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.ui.fragment.firstScreen.FirstScreenFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


class HomeFragment : BaseFragment<FragmentHomeBinding>(), TestListAdapter.OnTestClick {
    private val viewModel: HomeTabViewModel by viewModels { HomeTabViewModelFactory(testRepository) }
    private val testListAdapter: TestListAdapter by lazy { TestListAdapter(testClickCallback = this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchAction()
        initTestList()
        initScanQRCode()
        getDriverFiles()
//        moveFileToDir()
    }

    private fun moveFileToDir() { //TODO: Move file to dir example
        lifecycleScope.launch(Dispatchers.IO) {
            val id = googleApiRepository.createSpreadsheet("Excel1")
            Timber.e("Spreadsheet ID: $id")
            val newDir = id?.let { googleApiRepository.moveFileToDir(it, ROOT_DIR_ID) }
            Timber.e(newDir.toString())
        }
    }

    private fun getDriverFiles() { //TODO: Get drive contents example
        lifecycleScope.launch(Dispatchers.IO) {
            val files = googleApiRepository.getAllFiles()
            files.forEach { Timber.e("$it \n") }
        }
    }

    private fun createSheet() { //TODO: Create spread example
        lifecycleScope.launch(Dispatchers.IO) {
           val id = googleApiRepository.createSpreadsheet("Excel1")
            Timber.e("Spreadsheet ID: $id")
        }
    }

    private fun initTestList() {
        binding.testList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = testListAdapter
        }
        viewModel.tests.observe(viewLifecycleOwner) { testList ->
            testListAdapter.dataSet = testList
        }
    }

    private fun initSearchAction() {
        binding.apply {
            shortcutSearch.setOnClickListener {
                navigate(FirstScreenFragmentDirections.actionFirstScreenFragmentToSearchFragment())
            }
        }
    }

    private fun initScanQRCode(){
        binding.apply {
            toQRCodeFragmentButton.setOnClickListener {
                navigate(FirstScreenFragmentDirections.actionFirstScreenFragmentToQRCodeFragment())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.home)
    }

    override fun onClick(spreadId: String) {
        findNavController().navigate(FirstScreenFragmentDirections.actionFirstScreenFragmentToTestDetailFragment(spreadId))
    }

    override fun onDestroyView() {
        binding.testList.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {}
            override fun onViewDetachedFromWindow(v: View?) {binding.testList.adapter = null}
        })
        super.onDestroyView()
    }

}