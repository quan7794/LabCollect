package com.wac.labcollect.ui.fragment.viewPager.addTest.createTemplate

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.wac.labcollect.R
import com.wac.labcollect.domain.models.DataType
import timber.log.Timber

class TemplateDataAdapter(private var mDataset: MutableList<Pair<String, DataType>>) : DragDropSwipeAdapter<Pair<String, DataType>, TemplateDataAdapter.ViewHolder>(mDataset) {

    override fun getViewHolder(itemView: View) = ViewHolder(itemView, NameTextWatcher(), TypeTextWatcher())

    override fun getViewToTouchToStartDraggingItem(item: Pair<String, DataType>, viewHolder: ViewHolder, position: Int): View? { return null }

    override fun onBindViewHolder(item: Pair<String, DataType>, viewHolder: ViewHolder, position: Int) {
        // update MyCustomEditTextListener every time we bind a new item
        // so that it knows what item in mDataset to update
        Timber.e("bindingAdapterPosition: ${viewHolder.absoluteAdapterPosition}, position: $position")
        viewHolder.colNameWatcher.updatePosition(viewHolder.absoluteAdapterPosition)
        viewHolder.colName.setText(mDataset[viewHolder.bindingAdapterPosition].first)

        viewHolder.colTypeWatcher.updatePosition(viewHolder.absoluteAdapterPosition)
        viewHolder.colType.apply {
            setText(mDataset[viewHolder.bindingAdapterPosition].second.type.textValue)
            val typeItems =  DataType.getDataTypes()
            val mAdapter = ArrayAdapter(this.context, R.layout.list_item, typeItems)
            (viewHolder.colType as AutoCompleteTextView).setAdapter(mAdapter)
        }
    }

    class ViewHolder(itemView: View, var colNameWatcher: NameTextWatcher, var colTypeWatcher: TypeTextWatcher) : DragDropSwipeAdapter.ViewHolder(itemView) {
        // each data item is just a string in this case
        var colName: EditText = itemView.findViewById(R.id.colName)
        var colType: EditText = itemView.findViewById(R.id.colType)

        fun enableTextWatcher() {
            colName.addTextChangedListener(colNameWatcher)
            colType.addTextChangedListener(colTypeWatcher)
        }

        fun disableTextWatcher() {
            colName.removeTextChangedListener(colNameWatcher)
            colType.removeTextChangedListener(colTypeWatcher)
        }
    }

    inner class NameTextWatcher : TextWatcher {
        private var position = 0
        fun updatePosition(position: Int) { this.position = position }
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
        override fun afterTextChanged(editable: Editable) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
            mDataset[position] = mDataset[position].copy(first = charSequence.toString())
        }
    }

    inner class TypeTextWatcher : TextWatcher {
        private var position = 0
        fun updatePosition(position: Int) { this.position = position }
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
        override fun afterTextChanged(editable: Editable) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
            mDataset[position] = mDataset[position].copy(second = DataType(DataType.getTypeFromTextVal(charSequence.toString())))
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) { holder.enableTextWatcher() }
    override fun onViewDetachedFromWindow(holder: ViewHolder) { holder.disableTextWatcher() }
}