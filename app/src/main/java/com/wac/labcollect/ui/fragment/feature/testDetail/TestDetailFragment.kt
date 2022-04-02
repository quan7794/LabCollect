package com.wac.labcollect.ui.fragment.feature.testDetail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TimePicker
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.wac.labcollect.R
import com.wac.labcollect.databinding.FragmentTestDetailBinding
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.ui.fragment.feature.shareTest.ShareTestDialog
import com.wac.labcollect.utils.FileUtils.getColumnData
import com.wac.labcollect.utils.Utils.observeUntilNonNull
import timber.log.Timber
import java.lang.ref.WeakReference

class TestDetailFragment : BaseFragment<FragmentTestDetailBinding>(), View.OnClickListener {

    private val viewModel: TestDetailViewModel by viewModels { TestDetailViewModelFactory(testRepository, googleApiRepository) }
    private val args: TestDetailFragmentArgs by navArgs()
    private val testTableAdapter: TestTableAdapter by lazy { TestTableAdapter(WeakReference(context), this) }
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
                setAdapter(testTableAdapter)
                addHistorySize(100)
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
            testData.observeUntilNonNull(viewLifecycleOwner) {
                try {
                    testTableAdapter.apply {
                        Timber.e("Top data: ${it[0]}")
                        Timber.e("Left data: ${it.getColumnData(0)}")
                        setMajorData(it as List<List<String>>)
                    }
                } catch (e: Exception) {
                    Timber.e(e.stackTrace.toString())
                }
            }
        }
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