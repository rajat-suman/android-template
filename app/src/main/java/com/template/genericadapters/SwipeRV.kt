package com.template.genericadapters

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeRV(
    dragDirs: Int = 0,
    swipeDirs: Int = 0,
    val response: (Int, Int?, Int?) -> Unit
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    /**For drag and drop*/
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
//        response(viewHolder.absoluteAdapterPosition, null, target.absoluteAdapterPosition)
        response(viewHolder.bindingAdapterPosition, null, target.bindingAdapterPosition)
        return true
    }

    /** For swipe */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        response(viewHolder.bindingAdapterPosition, direction, null)
    }
}