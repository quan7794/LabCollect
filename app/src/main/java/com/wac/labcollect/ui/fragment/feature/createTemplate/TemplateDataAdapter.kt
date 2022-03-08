package com.wac.labcollect.ui.fragment.feature.createTemplate

import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.wac.labcollect.R
import com.wac.labcollect.domain.models.DataType
import timber.log.Timber

class TemplateDataAdapter(mDataSet: MutableList<Pair<String, DataType>>) : DragDropSwipeAdapter<Pair<String, DataType>, TemplateDataAdapter.ViewHolder>(mDataSet) {

    override fun onBindViewHolder(item: Pair<String, DataType>, viewHolder: ViewHolder, position: Int) {
        Timber.e("bindingAdapterPosition: ${viewHolder.absoluteAdapterPosition}, item: $item, pos:${dataSet[viewHolder.bindingAdapterPosition]} ")
        viewHolder.colName.setText(dataSet[viewHolder.bindingAdapterPosition].first)

        viewHolder.colType.apply {
            setText(dataSet[viewHolder.bindingAdapterPosition].second.type.textValue)
            val typeItems =  DataType.getDataTypes()
            val mAdapter = ArrayAdapter(this.context, R.layout.list_item, typeItems)
            (viewHolder.colType as AutoCompleteTextView).setAdapter(mAdapter)
        }
    }
    class ViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {
        // each data item is just a string in this case
        var colName: EditText = itemView.findViewById(R.id.colName)
        var colType: EditText = itemView.findViewById(R.id.colType)
    }

    override fun getViewHolder(itemView: View) = ViewHolder(itemView)

    override fun getViewToTouchToStartDraggingItem(item: Pair<String, DataType>, viewHolder: ViewHolder, position: Int): View? { return null }

}