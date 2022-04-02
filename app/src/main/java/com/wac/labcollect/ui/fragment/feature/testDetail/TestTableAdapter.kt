package com.wac.labcollect.ui.fragment.feature.testDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import cn.zhouchaoyuan.excelpanel.BaseExcelPanelAdapter
import com.wac.labcollect.databinding.SimpleTextItemBinding
import com.wac.labcollect.utils.Utils.toExcelFormat
import java.lang.ref.WeakReference

class TestTableAdapter(context: WeakReference<Context?>, private val cellClickListener: View.OnClickListener) : BaseExcelPanelAdapter<String, String, String>(context.get()) {
    //=========================================normal cell=========================================
    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = SimpleTextItemBinding.inflate(LayoutInflater.from(parent.context))
        return CellViewHolder(binding)
    }

    override fun onBindCellViewHolder(viewHolder: RecyclerView.ViewHolder, verticalPosition: Int, horizontalPosition: Int) {
        if (viewHolder is CellViewHolder) {
            viewHolder.textContent.text = getMajorItem(verticalPosition, horizontalPosition)
            viewHolder.root.tag = Pair((horizontalPosition + 1).toExcelFormat(), verticalPosition + 1)
            viewHolder.root.setOnClickListener(cellClickListener)
        }
    }
    //=========================================top cell===========================================
    override fun onCreateTopViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = SimpleTextItemBinding.inflate(LayoutInflater.from(parent.context))
        return CellViewHolder(binding)
    }

    override fun onBindTopViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        val rowTitle = getTopItem(position)
        if (null == viewHolder || viewHolder !is CellViewHolder || rowTitle == null) return
        viewHolder.textContent.text = rowTitle
    }

    //=========================================left cell===========================================
    override fun onCreateLeftViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = SimpleTextItemBinding.inflate(LayoutInflater.from(parent.context))
        return CellViewHolder(binding)
    }

    override fun onBindLeftViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        val colTitle = getLeftItem(position)
        if (null == viewHolder || viewHolder !is CellViewHolder || colTitle == null) return
        viewHolder.textContent.text = colTitle
        val lp = LinearLayout.LayoutParams(56, 28)
        viewHolder.root.layoutParams = lp
    }

    //=========================================top left cell=======================================
    override fun onCreateTopLeftView(): View? {
//        val binding = SimpleTextItemBinding.inflate(LayoutInflater.from(context.get()))
        return null
    }

    class CellViewHolder(binding: SimpleTextItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val textContent = binding.simpleTextItem
        val root = binding.root
    }
}