package com.wac.labcollect.ui.fragment.feature.testDetail

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.wac.labcollect.databinding.SimpleEdittextItemBinding

class AddTestDataAdapter(var dataSet: ArrayList<Pair<String, String>>) : RecyclerView.Adapter<AddTestDataAdapter.TestDataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestDataViewHolder {
        val binding = SimpleEdittextItemBinding.inflate(LayoutInflater.from(parent.context))
        return TestDataViewHolder(binding, EditTextWatcher())
    }

    override fun onBindViewHolder(holder: TestDataViewHolder, position: Int) {
        holder.textWatcher.updatePosition(holder.bindingAdapterPosition)
        holder.editTextContainer.hint = dataSet[position].first
        holder.editText.hint = dataSet[position].first
        holder.editText.setText(dataSet[position].second)
    }

    override fun getItemCount(): Int {
        return dataSet.count()
    }

    fun createData(set: ArrayList<String>) {
        if (dataSet.size > 0) return
        set.forEach { dataSet.add(Pair(it, "")) }
        notifyDataSetChanged()
    }

    class TestDataViewHolder(private val itemBinding: SimpleEdittextItemBinding, val textWatcher: EditTextWatcher) : RecyclerView.ViewHolder(itemBinding.root) {
        val editText: TextInputEditText = itemBinding.editText
        val editTextContainer = itemBinding.editTextContainer

        fun enableTextWatcher() {
            editText.addTextChangedListener(textWatcher)
        }

        fun disableTextWatcher() {
            editText.removeTextChangedListener(textWatcher)
        }
    }

    inner class EditTextWatcher : TextWatcher {
        private var position = 0
        fun updatePosition(position: Int) {
            this.position = position
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            dataSet[position] = dataSet[position].copy(second = s.toString())
        }

    }

    override fun onViewAttachedToWindow(holder: TestDataViewHolder) {
        holder.enableTextWatcher()
    }

    override fun onViewDetachedFromWindow(holder: TestDataViewHolder) {
        holder.disableTextWatcher()
    }
}


