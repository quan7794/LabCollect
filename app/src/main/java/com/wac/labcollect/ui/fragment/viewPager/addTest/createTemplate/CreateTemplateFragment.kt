package com.wac.labcollect.ui.fragment.viewPager.addTest.createTemplate

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemDragListener
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener
import com.wac.labcollect.databinding.CreateTemplateFragmentBinding
import com.wac.labcollect.ui.BaseFragment

@SuppressLint("NotifyDataSetChanged")
class CreateTemplateFragment : BaseFragment() {
    private var _binding: CreateTemplateFragmentBinding? = null
    private val binding: CreateTemplateFragmentBinding
        get() = _binding!!

    private val templateDataAdapter: TemplateDataAdapter by lazy {
        TemplateDataAdapter(mutableListOf("sdfasdf","asdf","qqqq", "wwww","qqqq", "wwww","qqqq", "wwww","qqqq", "wwww","qqqq", "wwww"))
    }

    private lateinit var viewModel: CreateTemplateViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = CreateTemplateFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val onItemDragListener = object : OnItemDragListener<String> {
//            override fun onItemDragged(previousPosition: Int, newPosition: Int, item: String) {
////            this@TemplateDataAdapter.notifyItemMoved(previousPosition, newPosition)
//            }
//
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onItemDropped(initialPosition: Int, finalPosition: Int, item: String) {
//                templateDataAdapter.notifyItemRemoved(finalPosition)
//                Toast.makeText(context,"Itemremoved",Toast.LENGTH_LONG).show()
//            }
//        }

        val onItemSwipeListener = object : OnItemSwipeListener<String> {
            override fun onItemSwiped(position: Int, direction: OnItemSwipeListener.SwipeDirection, item: String): Boolean {
                // Handle action of item swiped
                // Return false to indicate that the swiped item should be removed from the adapter's data set (default behaviour)
                // Return true to stop the swiped item from being automatically removed from the adapter's data set (in this case, it will be your responsibility to manually update the data set as necessary)
                Toast.makeText(context,"Itemremoved",Toast.LENGTH_LONG).show()
                templateDataAdapter.removeItem(position)
                templateDataAdapter.notifyItemRemoved(position)
                return true
            }
        }

        binding.templateData.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = templateDataAdapter
            orientation = DragDropSwipeRecyclerView.ListOrientation.VERTICAL_LIST_WITH_VERTICAL_DRAGGING
//            dragListener = onItemDragListener
            swipeListener = onItemSwipeListener
        }

        viewModel = ViewModelProvider(this)[CreateTemplateViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}