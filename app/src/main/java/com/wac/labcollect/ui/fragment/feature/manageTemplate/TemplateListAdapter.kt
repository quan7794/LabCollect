package com.wac.labcollect.ui.fragment.feature.manageTemplate

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wac.labcollect.databinding.TemplateItemBinding
import com.wac.labcollect.domain.models.Template
import com.wac.labcollect.utils.dragSwipeRecyclerview.DragDropSwipeAdapter

class TemplateListAdapter(
    dataSet: List<Template> = emptyList(),
    private val createTestCallback: CreateTestCallback,
    private var isCreateTest: Boolean = false
) : DragDropSwipeAdapter<Template, TemplateListAdapter.ViewHolder>(dataSet) {

    class ViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {
        val binding = TemplateItemBinding.bind(itemView)
        val itemText: TextView = binding.templateName
        val dragIcon: ImageView = binding.dragIcon
    }

    override fun getViewHolder(itemView: View) = ViewHolder(itemView)

    override fun onBindViewHolder(item: Template, viewHolder: ViewHolder, position: Int) {
        viewHolder.itemText.text = item.title
        if (isCreateTest) {
            viewHolder.itemView.setOnClickListener {
                createTestCallback.onClick(item)
            }
        }
    }

    override fun getViewToTouchToStartDraggingItem(item: Template, viewHolder: ViewHolder, position: Int): View {
        return viewHolder.dragIcon
    }
//
//    fun setIsCreateTest(value: Boolean) {
//        if (isCreateTest!= value) {
//            isCreateTest = value
//        }
//    }

    interface CreateTestCallback {
        fun onClick(template: Template)
    }
}