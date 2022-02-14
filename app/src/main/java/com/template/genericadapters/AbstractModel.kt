package com.template.genericadapters

import androidx.recyclerview.widget.RecyclerView
import kotlinx.parcelize.RawValue

abstract class AbstractModel{
    @Transient
    var vpPosition: Int = -1

    @Transient
    var length: Int = 0

    @Transient
    var viewHolder: RecyclerView.ViewHolder? = null

    @Transient
    var onItemClick: @RawValue RecyclerAdapter.OnItemClick? = null

    fun getRVPosition(): Int {
        return viewHolder?.absoluteAdapterPosition ?: -1
    }
}