package com.template.genericadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.template.BR
import java.util.*
import kotlin.collections.ArrayList

class RecyclerAdapter<T : AbstractModel>(
    @LayoutRes val layoutId: Int,
) : RecyclerView.Adapter<RecyclerAdapter.VH<T>>() {

    val items = ArrayList<T>()
    private var inflater: LayoutInflater? = null
    private var onItemClick: OnItemClick? = null

    fun getItemAt(position: Int) = items[position]

    /**Delete Functions*/
    fun delete(index: Int? = null) {
        val customIndex = items.size - 1
        items.removeAt(index ?: customIndex)
//        notifyItemRemoved(index ?: customIndex)
//        notifyItemRangeChanged(index ?: customIndex, itemCount)
        notifyDataSetChanged()
    }

    fun deleteAll() {
        val size = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(items, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(items, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    fun swapItem(fromPosition: Int, toPosition: Int, swapped : (Boolean)->Unit) {

        val temp = items[fromPosition]
        items[fromPosition] = items[toPosition]
        items[toPosition] = temp

        notifyItemChanged(toPosition)
        swapped(true)
//            Collections.swap(items, fromPosition, toPosition)
//            notifyItemMoved(fromPosition, toPosition)
    }

    fun updateItem(position: Int?, item: T) {
        if (position == null || position >= items.size)
            return
        items[position] = item
        notifyItemChanged(position)
    }

    /**Add Functions*/
    fun insertAt(index: Int? = null, item: T) {
        items.add(index ?: items.size, item)
        notifyItemInserted(index ?: items.size - 1)
    }

    fun append(newItems: List<T>) {
        val start = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(start, newItems.size)
    }

    fun addItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemClick(onItemClick: OnItemClick?) {
        this.onItemClick = onItemClick
    }

    fun getAllItems() = items


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<T> {
        val layoutInflater = inflater ?: LayoutInflater.from(parent.context)

        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater,
            layoutId,
            parent,
            false
        )
        return VH(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH<T>, position: Int) {
        holder.setIsRecyclable(false)
        val model = items[position]
        model.viewHolder = holder
        model.length = itemCount
        onItemClick?.let { model.onItemClick = it }
        holder.bind(model)
    }


    class VH<T : AbstractModel>(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: T) {
            binding.setVariable(BR.model, model)
            binding.executePendingBindings()
        }
    }

    fun interface OnItemClick {
        fun onClick(view: View, viewHolder: RecyclerView.ViewHolder?, type: String)
    }
}