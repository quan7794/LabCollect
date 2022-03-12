package com.wac.labcollect.ui.fragment.firstScreen.homeTab

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.wac.labcollect.R
import com.wac.labcollect.domain.models.Test
import com.wac.labcollect.utils.dragSwipeRecyclerview.DragDropSwipeAdapter

class TestListAdapter(dataSet: List<Test> = emptyList()) : DragDropSwipeAdapter<Test, TestListAdapter.ViewHolder>(dataSet) {

    class ViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {
        val testName: EditText = itemView.findViewById(R.id.name)
        val testType: EditText = itemView.findViewById(R.id.type)
        val startDate: EditText = itemView.findViewById(R.id.startTime)
        val endDate: EditText = itemView.findViewById(R.id.endTime)
        val dragImage: ImageView = itemView.findViewById(R.id.dragImage)
    }

    override fun getViewHolder(itemView: View) = ViewHolder(itemView)

    override fun onBindViewHolder(item: Test, viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            testName.setText(item.title)
            testType.setText(item.type)
            startDate.setText(item.startTime)
            endDate.setText(item.endTime)
        }
    }

    override fun getViewToTouchToStartDraggingItem(item: Test, viewHolder: ViewHolder, position: Int): View? {
        return viewHolder.dragImage
    }
}