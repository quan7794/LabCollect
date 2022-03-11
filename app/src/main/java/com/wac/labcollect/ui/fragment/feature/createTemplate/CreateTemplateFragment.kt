package com.wac.labcollect.ui.fragment.feature.createTemplate

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.wac.labcollect.MainApplication
import com.wac.labcollect.R
import com.wac.labcollect.databinding.CreateTemplateFragmentBinding
import com.wac.labcollect.domain.models.DataType
import com.wac.labcollect.domain.models.Field
import com.wac.labcollect.domain.models.TYPE
import com.wac.labcollect.domain.models.Template
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.utils.Utils.createUniqueName
import com.wac.labcollect.utils.Utils.observeOnce
import com.wac.labcollect.utils.dragSwipeRecyclerview.DragDropSwipeRecyclerView
import com.wac.labcollect.utils.dragSwipeRecyclerview.listener.OnItemSwipeListener
import kotlinx.coroutines.launch
import timber.log.Timber

class CreateTemplateFragment : BaseFragment<CreateTemplateFragmentBinding>() {
    private val templateDataAdapter: TemplateDataAdapter by lazy {
        TemplateDataAdapter(mutableListOf(Pair("1", DataType(TYPE.INT)), Pair("2", DataType(TYPE.DOUBLE))))
    }
    private val viewModel: CreateTemplateViewModel by viewModels { CreateTemplateViewModelFactory(testRepository) }
    private val args: CreateTemplateFragmentArgs by navArgs()
    private var testUniqueName: String? = null
    private var lastDeletedItem: Pair<String, DataType>? = null

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_setting -> {
                Snackbar.make(binding.root, "Setting this template", Snackbar.LENGTH_SHORT).show()
            }
            R.id.action_save -> {
                if (templateDataAdapter.isValidateDataSet() && binding.templateName.text.toString().isNotEmpty()) {
                    val fieldList: ArrayList<Field> = arrayListOf()
                    templateDataAdapter.dataSet.forEach { field -> fieldList.add(Field(field.first, field.second.type)) }
                    val newTemp = Template(
                        title = binding.templateName.text.toString(),
                        uniqueName = binding.templateName.text.toString().createUniqueName(),
                        owner = FirebaseAuth.getInstance().currentUser?.email ?: "",
                        fields = fieldList
                    )
                    lifecycleScope.launch {
                        viewModel.insert(newTemp)
                        Timber.e("Saved: $newTemp")
                        Snackbar.make(binding.root, getString(R.string.saved), Snackbar.LENGTH_SHORT).show()
                        testUniqueName?.let { //Goto Test Manage Screen after create template if we are creating test.
                            val isSuccess = viewModel.addTemplateToNewTest(newTemp)
                            if (isSuccess) {
                                val action = CreateTemplateFragmentDirections.actionCreateTemplateFragmentToManageTestFragment(it)
                                this@CreateTemplateFragment.findNavController().navigate(action)
                            } else {
                                Snackbar.make(binding.root, getString(R.string.can_not_establish_test), Snackbar.LENGTH_SHORT).show()
                            }
                        }?: run { //Go to First Screen after standalone template created.
                            val action = CreateTemplateFragmentDirections.actionCreateTemplateFragmentToFirstScreenFragment()
                           this@CreateTemplateFragment.findNavController().navigate(action)
                        }
                    }

                } else Snackbar.make(binding.root, getString(R.string.please_fill_all_value), Snackbar.LENGTH_SHORT).show()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabAdd.setOnClickListener {
            templateDataAdapter.insertItem(templateDataAdapter.dataSet.size, Pair("", DataType(TYPE.TEXT)))
            binding.recyclerView.smoothScrollToPosition(templateDataAdapter.dataSet.size)
//            Snackbar.make(binding.root, "Added new item at ${templateDataAdapter.dataSet.size}", Snackbar.LENGTH_SHORT).show()
        }
        testUniqueName = args.testUniqueName

        testUniqueName?.let {
            viewModel.getTest(it).observeOnce { test ->
                viewModel.setParentTest(test)
                binding.templateName.setText( test.title)
            }
        }
        binding.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = templateDataAdapter
                orientation = DragDropSwipeRecyclerView.ListOrientation.VERTICAL_LIST_WITH_VERTICAL_DRAGGING
                swipeListener = onItemSwipeListener
//            setItemViewCacheSize(100)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private val onItemSwipeListener = object : OnItemSwipeListener<Pair<String, DataType>> {
        override fun onItemSwiped(position: Int, direction: OnItemSwipeListener.SwipeDirection, item: Pair<String, DataType>): Boolean {
            Snackbar.make(binding.root, "Đã xoá ${templateDataAdapter.dataSet[position].first}", Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo)) {
                    try {
                        val insertPosition = if (templateDataAdapter.dataSet.size < position) {
                            templateDataAdapter.dataSet.size - 1
                        } else position
                        templateDataAdapter.insertItem(insertPosition, lastDeletedItem!!)
                    } catch (e: Exception) {
                        Toast.makeText(context, getString(R.string.can_not_undo) + "\n Detail: $e", Toast.LENGTH_SHORT).show()
                    }
                }.show()
            lastDeletedItem = templateDataAdapter.dataSet[position]
            templateDataAdapter.removeItem(position)
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_template, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}