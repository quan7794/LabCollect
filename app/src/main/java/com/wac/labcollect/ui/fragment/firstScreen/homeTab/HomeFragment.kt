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
    private val viewModel: HomeTabViewModel by viewModels { HomeTabViewModelFactory(testRepository, googleApiRepository) }
    private val testListAdapter: TestListAdapter by lazy { TestListAdapter(testClickCallback = this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchAction()
        initTestList()
        initScanQRCode()
    }

    private fun initTestList() {
        if (lastSignedAccount() == null) return
        binding.testList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = testListAdapter
        }
        viewModel.getSpreads()
        viewModel.spreads.observe(viewLifecycleOwner) { spreads ->
            testListAdapter.dataSet = spreads
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