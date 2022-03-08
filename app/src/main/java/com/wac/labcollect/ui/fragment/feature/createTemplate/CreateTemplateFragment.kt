package com.wac.labcollect.ui.fragment.feature.createTemplate

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wac.labcollect.utils.dragSwipeRecyclerview.DragDropSwipeRecyclerView
import com.wac.labcollect.utils.dragSwipeRecyclerview.listener.OnItemSwipeListener
import com.google.android.material.snackbar.Snackbar
import com.wac.labcollect.MainApplication
import com.wac.labcollect.R
import com.wac.labcollect.databinding.CreateTemplateFragmentBinding
import com.wac.labcollect.domain.models.DataType
import com.wac.labcollect.domain.models.TYPE
import com.wac.labcollect.ui.base.BaseFragment
import kotlin.Exception

@SuppressLint("NotifyDataSetChanged")
class CreateTemplateFragment : BaseFragment(R.layout.create_template_fragment) {
    private var _binding: CreateTemplateFragmentBinding? = null
    private val binding: CreateTemplateFragmentBinding
        get() = _binding!!
    private val templateDataAdapter: TemplateDataAdapter by lazy {
        TemplateDataAdapter(mutableListOf(Pair("1", DataType(TYPE.INT)), Pair("2", DataType(TYPE.DOUBLE))))
    }
    private var lastDeletedItem: Pair<String, DataType>? = null

    private lateinit var viewModel: CreateTemplateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_template, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_setting -> {
                Snackbar.make(binding.root, "Setting this template", Snackbar.LENGTH_SHORT).show()
            }
            R.id.action_save -> {
                Snackbar.make(binding.root, "Save ", Snackbar.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = CreateTemplateFragmentBinding.inflate(inflater)
        binding.fabAdd.setOnClickListener {
            templateDataAdapter.insertItem(templateDataAdapter.dataSet.size, Pair("", DataType(TYPE.TEXT)))
            binding.recyclerView.smoothScrollToPosition(templateDataAdapter.dataSet.size)
            Snackbar.make(binding.root, "Added new item at ${templateDataAdapter.dataSet.size}", Snackbar.LENGTH_SHORT).show()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = templateDataAdapter
            orientation = DragDropSwipeRecyclerView.ListOrientation.VERTICAL_LIST_WITH_VERTICAL_DRAGGING
            swipeListener = onItemSwipeListener
//            setItemViewCacheSize(100)
        }
        val factory = CreateTemplateViewModelFactory((activity?.application as MainApplication).repository)
        viewModel = ViewModelProvider(this, factory)[CreateTemplateViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val onItemSwipeListener = object : OnItemSwipeListener<Pair<String, DataType>> {
        override fun onItemSwiped(position: Int, direction: OnItemSwipeListener.SwipeDirection, item: Pair<String, DataType>): Boolean {
            Snackbar.make(binding.root, "Đã xoá ${templateDataAdapter.dataSet[position].first}", Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo)) {
                    try {
                        val insertPosition = if (templateDataAdapter.dataSet.size < position) { templateDataAdapter.dataSet.size -1 } else position
                        templateDataAdapter.insertItem(insertPosition, lastDeletedItem!!)
                    } catch(e: Exception) { Toast.makeText(context,getString(R.string.can_not_undo)+ "\n Detail: $e", Toast.LENGTH_SHORT).show() }
                }.show()
            lastDeletedItem = templateDataAdapter.dataSet[position]
            templateDataAdapter.removeItem(position)
            return true
        }
    }
}
//
//class TemplateDataItem : AbstractBindingItem<TemplateDataItemBinding>() {
//    var name: String? = null
//
//    override val type: Int
//        get() = R.id.templateItem
//
//    override fun bindView(binding: TemplateDataItemBinding, payloads: List<Any>) {
//        binding.colName.setText(name)
//        binding.colType.setText(name)
//    }
//
//    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): TemplateDataItemBinding {
//        return TemplateDataItemBinding.inflate(inflater, parent, false)
//    }
//}