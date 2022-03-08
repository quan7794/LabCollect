package com.wac.labcollect.ui.fragment.feature.createTemplate

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener
import com.google.android.material.snackbar.Snackbar
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
            templateDataAdapter.addItem(Pair("", DataType(TYPE.TEXT)))
            Snackbar.make(binding.root, "Added new item", Snackbar.LENGTH_SHORT).show()
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
            setItemViewCacheSize(100)
        }

        viewModel = ViewModelProvider(this)[CreateTemplateViewModel::class.java]
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
                        templateDataAdapter.insertItem(position, templateDataAdapter.dataSet[position])
                    } catch(e: Exception) { Toast.makeText(context,getString(R.string.can_not_undo)+ "\n Detail: $e", Toast.LENGTH_SHORT).show() }
                }.show()
            templateDataAdapter.removeItem(position)
            return true
        }
    }
}