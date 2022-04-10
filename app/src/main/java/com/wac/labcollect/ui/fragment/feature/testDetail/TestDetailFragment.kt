package com.wac.labcollect.ui.fragment.feature.testDetail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentTestDetailBinding
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.ui.fragment.feature.shareTest.ShareTestDialog
import com.wac.labcollect.utils.FileUtils.getColumnData
import com.wac.labcollect.utils.Utils.observeUntilNonNull
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

class TestDetailFragment : BaseFragment<FragmentTestDetailBinding>(), View.OnClickListener {

    private val viewModel: TestDetailViewModel by viewModels { TestDetailViewModelFactory(testRepository, googleApiRepository) }
    private val args: TestDetailFragmentArgs by navArgs()
    private val testDataAdapter: TestTableAdapter by lazy { TestTableAdapter(WeakReference(context), this) }
    private val updateDataAdapter : AddTestDataAdapter by lazy { AddTestDataAdapter(arrayListOf())}

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(backPressCallback)
        initUi(args.spreadId)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.test_detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                viewModel.currentTest.value?.let { currentTest ->
                    val shareDialog = ShareTestDialog.newInstance(currentTest.spreadId, currentTest.title, currentTest.uniqueName)
                    shareDialog.show(childFragmentManager, ShareTestDialog::class.java.simpleName)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initUi(spreadId: String) {
        viewModel.apply {
            binding.testDataTable.apply {
                setAdapter(testDataAdapter)
                addHistorySize(100)
            }
            binding.apply { //Update new test data
                updateList.apply {
                    adapter = updateDataAdapter
                    layoutManager = LinearLayoutManager(context).also {
                        it.orientation = LinearLayoutManager.HORIZONTAL
                    }
                }
                saveButton.setOnClickListener {
                    if (isValidateUpdateData()) {
                        viewModel.updateNewData(updateDataAdapter.dataSet, spreadId)
                    } else {
                        Toast.makeText(context, "Hãy nhập tất cả dữ liệu trước khi lưu", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            init(spreadId)
            currentTest.observeUntilNonNull(viewLifecycleOwner) { test ->
                (requireActivity() as AppCompatActivity).supportActionBar?.title = test!!.title
                binding.apply {
                    name.text = test.title
                    type.text = test.type
                    startTime.text = test.startTime
                    endTime.text = test.endTime
                }
            }
            testData.observe(viewLifecycleOwner) {
                try {
                    testDataAdapter.apply {
                        Timber.e("Top data: ${it[0]}")
//                        Timber.e("Left data: ${it.getColumnData(0)}")
                        setMajorData(it as List<List<String>>)
                        val data = arrayListOf<String>()
                        data.addAll(it[0])
                        data.removeAt(0)
                        updateDataAdapter.createData(data)
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    private fun isValidateUpdateData(): Boolean {
        if (updateDataAdapter.dataSet.isEmpty()) return false
        updateDataAdapter.dataSet.forEach {
            if (it.second.isEmpty()) return false
        }
        return true
    }

    private val backPressCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val action = TestDetailFragmentDirections.toFirstScreenFragment()
            binding.root.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        backPressCallback.remove()
        super.onDestroyView()
    }

    override fun onClick(view: View) {
       val excelPosition= view.tag as Pair<*, *>
        Snackbar.make(view,"Clicked at position: $excelPosition", Snackbar.LENGTH_SHORT).show()
    }
}