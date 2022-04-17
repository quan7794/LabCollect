package com.wac.labcollect.ui.fragment.firstScreen.homeTab

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wac.labcollect.R
import com.wac.labcollect.data.repository.googleApi.GoogleApiConstant.DRIVE_BASE_DIR_URL
import com.wac.labcollect.data.repository.googleApi.GoogleApiConstant.ROOT_DIR_ID
import com.wac.labcollect.databinding.FragmentHomeBinding
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.ui.fragment.firstScreen.FirstScreenFragmentDirections
import com.wac.labcollect.utils.Status
import timber.log.Timber


class HomeFragment : BaseFragment<FragmentHomeBinding>(), TestListAdapter.OnTestClick {
    private val viewModel: HomeTabViewModel by viewModels { HomeTabViewModelFactory(testRepository, googleApiRepository) }
    private val testListAdapter: TestListAdapter by lazy { TestListAdapter(testClickCallback = this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initTestList()
        initScanQRCode()
        initProgress(viewModel, binding.loadingAnimation.id)
    }

    private fun initTestList() {
        if (lastSignedAccount() == null) return
        binding.testList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = testListAdapter
        }
        viewModel.getSpreads()
        viewModel.spreads.observe(viewLifecycleOwner) { spreads ->
            if (binding.swipeToRefresh.isRefreshing) {
                binding.swipeToRefresh.isRefreshing = false
            }
            if (spreads.isEmpty()) {
                binding.errorMessageContainer.visibility = View.VISIBLE
                binding.testListContainer.visibility = View.GONE
                binding.requestSystemAccess.setOnClickListener {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(DRIVE_BASE_DIR_URL + ROOT_DIR_ID))
                    startActivity(browserIntent)
                }
            } else {
                testListAdapter.dataSet = spreads
                binding.errorMessageContainer.visibility = View.GONE
                binding.testListContainer.visibility = View.VISIBLE
            }
        }
    }

    private fun initView() {
        binding.apply {
            shortcutSearch.setOnClickListener {
                navigate(FirstScreenFragmentDirections.actionFirstScreenFragmentToSearchFragment())
            }
            swipeToRefresh.setOnRefreshListener {
                viewModel.getSpreads()
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