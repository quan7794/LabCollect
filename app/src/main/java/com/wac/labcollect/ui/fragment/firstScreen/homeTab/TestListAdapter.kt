package com.wac.labcollect.ui.fragment.firstScreen.homeTab

import android.view.View
import android.view.WindowId
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.wac.labcollect.R
import com.wac.labcollect.domain.models.Test
import com.wac.labcollect.utils.dragSwipeRecyclerview.DragDropSwipeAdapter

class TestListAdapter(dataSet: List<Test> = emptyList(), private val testClickCallback: OnTestClick) : DragDropSwipeAdapter<Test, TestListAdapter.ViewHolder>(dataSet) {

    class ViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {
        val testName: TextView = itemView.findViewById(R.id.name)
        val testType: TextView = itemView.findViewById(R.id.type)
        val startDate: TextView = itemView.findViewById(R.id.startTime)
        val endDate: TextView = itemView.findViewById(R.id.endTime)
        val dragImage: ImageView = itemView.findViewById(R.id.dragImage)
    }

    override fun getViewHolder(itemView: View) = ViewHolder(itemView)

    override fun onBindViewHolder(item: Test, viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            testName.text = item.title
            testType.text = item.type
            startDate.text = item.startTime
            endDate.text = item.endTime
            itemView.setOnClickListener {
                testClickCallback.onClick(item.spreadId)
            }
        }
    }

    override fun getViewToTouchToStartDraggingItem(item: Test, viewHolder: ViewHolder, position: Int): View {
        return viewHolder.dragImage
    }

    interface OnTestClick {
        fun onClick(spreadId: String)
    }

}