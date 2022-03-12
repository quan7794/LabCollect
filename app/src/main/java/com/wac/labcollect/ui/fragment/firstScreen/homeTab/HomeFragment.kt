package com.wac.labcollect.ui.fragment.firstScreen.homeTab

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentHomeBinding
import com.wac.labcollect.ui.base.BaseFragment
import timber.log.Timber

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: HomeTabViewModel by viewModels { HomeTabViewModelFactory(testRepository) }
    private val testListAdapter: TestListAdapter by lazy { TestListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchAction()
        initTestList()
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
            try {
                shortcutSearch.setOnClickListener { Navigation.findNavController(root).navigate(R.id.action_firstScreenFragment_to_searchFragment) }
            } catch (e: Exception) {
                Timber.e("Error when start search fragment: $e")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.home)
    }

}