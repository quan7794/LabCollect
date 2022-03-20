package com.wac.labcollect.ui.fragment.firstScreen.homeTab

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wac.labcollect.R
import com.wac.labcollect.utils.dragSwipeRecyclerview.DragDropSwipeAdapter

class TestListAdapter(dataSet: List<Triple<String, String, List<String>>> = emptyList(), private val testClickCallback: OnTestClick) : DragDropSwipeAdapter<Triple<String, String, List<String>>, TestListAdapter.ViewHolder>(dataSet) {

    class ViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {
        val testName: TextView = itemView.findViewById(R.id.name)
        val dragImage: ImageView = itemView.findViewById(R.id.dragImage)
    }

    override fun getViewHolder(itemView: View) = ViewHolder(itemView)

    override fun onBindViewHolder(item: Triple<String, String, List<String>>, viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            testName.text = item.first
            itemView.setOnClickListener {
                testClickCallback.onClick(item.second)
            }
        }
    }

    override fun getViewToTouchToStartDraggingItem(item: Triple<String, String, List<String>>, viewHolder: ViewHolder, position: Int): View {
        return viewHolder.dragImage
    }

    interface OnTestClick {
        fun onClick(spreadId: String)
    }
}