package com.wac.labcollect.ui.fragment.feature.testDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.zhouchaoyuan.excelpanel.BaseExcelPanelAdapter
import com.wac.labcollect.databinding.SimpleTextItemBinding

class TestTableAdapter(context: Context?) : BaseExcelPanelAdapter<String, String, String>(context) {
    //=========================================normal cell=========================================
    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = SimpleTextItemBinding.inflate(LayoutInflater.from(parent.context))
        return CellViewHolder(binding)
    }

    override fun onBindCellViewHolder(viewHolder: RecyclerView.ViewHolder, verticalPosition: Int, horizontalPosition: Int) {
        if (viewHolder is CellViewHolder) {
            viewHolder.textContent.text = getMajorItem(verticalPosition, horizontalPosition)
        }
    }
    //=========================================top cell===========================================
    override fun onCreateTopViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindTopViewHolder(p0: RecyclerView.ViewHolder?, p1: Int) {
        TODO("Not yet implemented")
    }
    //=========================================left cell===========================================
    override fun onCreateLeftViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindLeftViewHolder(p0: RecyclerView.ViewHolder?, p1: Int) {
        TODO("Not yet implemented")
    }

    //=========================================top left cell=======================================
    override fun onCreateTopLeftView(): View {
        TODO("Not yet implemented")
    }

    class CellViewHolder(binding: SimpleTextItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val textContent = binding.simpleTextItem
    }
}