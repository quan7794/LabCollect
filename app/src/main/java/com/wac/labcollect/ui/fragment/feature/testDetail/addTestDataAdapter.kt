package com.wac.labcollect.ui.fragment.feature.testDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wac.labcollect.databinding.SimpleEdittextItemBinding

class AddTestDataAdapter(var dataSet: List<String>) : RecyclerView.Adapter<AddTestDataAdapter.TestDataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestDataViewHolder {
        val binding = SimpleEdittextItemBinding.inflate(LayoutInflater.from(parent.context))
        return TestDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TestDataViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return dataSet.count()
    }

    fun updateData(newSet: List<String>) {
        dataSet = newSet
    }

    inner class TestDataViewHolder(private val itemBinding: SimpleEdittextItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    }
}