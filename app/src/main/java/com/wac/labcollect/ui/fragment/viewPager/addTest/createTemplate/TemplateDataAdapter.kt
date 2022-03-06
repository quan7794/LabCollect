package com.wac.labcollect.ui.fragment.viewPager.addTest.createTemplate

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemDragListener
import com.wac.labcollect.R

class TemplateDataAdapter(private var mDataset: MutableList<String>) : DragDropSwipeAdapter<String, TemplateDataAdapter.ViewHolder>(mDataset) {

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(changedData: MutableList<String>) {
        this.mDataset = changedData
        notifyDataSetChanged()
    }

    override fun getViewHolder(itemView: View) = ViewHolder(itemView, CustomEditTextWatcher(), CustomEditTextWatcher())

    override fun getViewToTouchToStartDraggingItem(item: String, viewHolder: ViewHolder, position: Int): View? {
        return null
    }

    override fun onBindViewHolder(item: String, viewHolder: ViewHolder, position: Int) {
        // update MyCustomEditTextListener every time we bind a new item
        // so that it knows what item in mDataset to update
        viewHolder.colNameWatcher.updatePosition(viewHolder.absoluteAdapterPosition)
        viewHolder.colName.setText(item)

        viewHolder.colTypeWatcher.updatePosition(viewHolder.absoluteAdapterPosition)
        viewHolder.colType.apply {
            setText(item)
            val typeItems = arrayListOf("Số nguyên","Số thập phân","Văn bản","Đúng/sai")
            val mAdapter = ArrayAdapter(this.context, R.layout.list_item, typeItems)
            (viewHolder.colType as AutoCompleteTextView).setAdapter(mAdapter)
        }
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.template_data_item, parent, false)
//        // pass MyCustomEditTextListener to viewHolder in onCreateViewHolder
//        // so that we don't have to do this expensive allocation in onBindViewHolder
//        return ViewHolder(v, CustomEditTextListener(), CustomEditTextListener())
//    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        // update MyCustomEditTextListener every time we bind a new item
//        // so that it knows what item in mDataset to update
//        holder.colNameListener.updatePosition(holder.adapterPosition)
//        holder.colName.setText(mDataset[holder.adapterPosition])
//
//        holder.colTypeListener.updatePosition(holder.adapterPosition)
//        holder.colType.apply {
//            setText(mDataset[holder.adapterPosition])
//            val typeItems = arrayListOf("Số nguyên","Số thập phân","Văn bản","Đúng/sai")
//            val mAdapter = ArrayAdapter(this.context, R.layout.list_item, typeItems)
//            (holder.colType as AutoCompleteTextView).setAdapter(mAdapter)
//        }
//
//    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    class ViewHolder(itemView: View, var colNameWatcher: CustomEditTextWatcher, var colTypeWatcher: CustomEditTextWatcher) : DragDropSwipeAdapter.ViewHolder(itemView) {
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

    // we make TextWatcher to be aware of the position it currently works with
    // this way, once a new item is attached in onBindViewHolder, it will
    // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
    inner class CustomEditTextWatcher : TextWatcher {
        private var position = 0
        fun updatePosition(position: Int) { this.position = position }

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
            mDataset[position] = charSequence.toString()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        holder.enableTextWatcher()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        holder.disableTextWatcher()
    }




}