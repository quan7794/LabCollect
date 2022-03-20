package com.wac.labcollect.ui.fragment.feature.createTemplate

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.wac.labcollect.R
import com.wac.labcollect.databinding.CreateTemplateFragmentBinding
import com.wac.labcollect.domain.models.DataType
import com.wac.labcollect.domain.models.Field
import com.wac.labcollect.domain.models.TYPE
import com.wac.labcollect.domain.models.Template
import com.wac.labcollect.ui.base.BaseFragment
import com.wac.labcollect.utils.Utils.createUniqueName
import com.wac.labcollect.utils.Utils.currentTimestamp
import com.wac.labcollect.utils.dragSwipeRecyclerview.DragDropSwipeRecyclerView
import com.wac.labcollect.utils.dragSwipeRecyclerview.listener.OnItemSwipeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class CreateTemplateFragment : BaseFragment<CreateTemplateFragmentBinding>() {
    private val templateDataAdapter: TemplateDataAdapter by lazy {
        TemplateDataAdapter(mutableListOf(Pair("", DataType(TYPE.TEXT))))
    }
    private val viewModel: CreateTemplateViewModel by viewModels { CreateTemplateViewModelFactory(testRepository, googleApiRepository) }
    private val args: CreateTemplateFragmentArgs by navArgs()
    private var spreadId: String? = null
    private var lastDeletedItem: Pair<String, DataType>? = null

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_setting -> {
                Snackbar.make(binding.root, "Setting this template", Snackbar.LENGTH_SHORT).show()
            }
            R.id.action_save -> {
                if (templateDataAdapter.isValidateDataSet() && binding.templateName.text.toString().isNotEmpty()) {
                    val fieldList: ArrayList<Field> = arrayListOf(Field("time", TYPE.TEXT))
                    templateDataAdapter.dataSet.forEach { field -> fieldList.add(Field(field.first, field.second.type)) }
                    val newTemp = Template(
                        title = binding.templateName.text.toString(),
                        uniqueName = binding.templateName.text.toString().createUniqueName(),
                        owner = FirebaseAuth.getInstance().currentUser?.email ?: "",
                        fields = fieldList,
                        createTimestamp = currentTimestamp().toString()
                    )
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.insert(newTemp)
                        Timber.e("Saved: $newTemp")
                        Snackbar.make(binding.root, getString(R.string.saved), Snackbar.LENGTH_SHORT).show()
                        spreadId?.let { //Goto Test Detail Screen after create template if we are creating test.
                            val isSuccess = viewModel.addTemplateToNewTest(newTemp)
                            if (isSuccess) {
                                withContext(Dispatchers.Main) {
                                    navigate(CreateTemplateFragmentDirections.actionCreateTemplateFragmentToTestDetailFragment(it))
                                }
                            } else {
                                Snackbar.make(binding.root, getString(R.string.can_not_establish_test), Snackbar.LENGTH_SHORT).show()
                            }
                        } ?: run { //Go to First Screen after standalone template created.
                            withContext(Dispatchers.Main) {
                                navigate(CreateTemplateFragmentDirections.actionCreateTemplateFragmentToFirstScreenFragment())
                            }
                        }
                    }
                } else Snackbar.make(binding.root, getString(R.string.please_fill_all_value), Snackbar.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spreadId = args.spreadId
        spreadId?.let { id ->
            lifecycleScope.launch {
                val test = viewModel.getTestBySpreadId(id)
                if (test != null) {
                    viewModel.setParentTest(test)
                    binding.templateName.setText(test.title)
                }
            }
        }
        binding.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = templateDataAdapter
                orientation = DragDropSwipeRecyclerView.ListOrientation.VERTICAL_LIST_WITH_VERTICAL_DRAGGING
                swipeListener = onItemSwipeListener
            }
        }
        binding.fabAdd.setOnClickListener {
            templateDataAdapter.insertItem(templateDataAdapter.dataSet.size, Pair("", DataType(TYPE.TEXT)))
            binding.recyclerView.smoothScrollToPosition(templateDataAdapter.dataSet.size)
        }
    }

    override fun onDestroyView() {
        binding.recyclerView.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {}
            override fun onViewDetachedFromWindow(v: View?) {binding.recyclerView.adapter = null}
        })
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