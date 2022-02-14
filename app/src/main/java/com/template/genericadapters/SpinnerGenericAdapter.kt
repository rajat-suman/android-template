package com.template.genericadapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.template.BR

class SpinnerGenericAdapter<T>  (
    @LayoutRes val layoutId: Int,
    private val disabledPosition: Int = -1
) : BaseAdapter() {

    private var list = mutableListOf<T>()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater: LayoutInflater = LayoutInflater.from(parent?.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, layoutId, parent,false)
        binding.setVariable(BR.model, list[position])
        if (binding.hasPendingBindings()) {
            binding.executePendingBindings()
        }
        return binding.root
    }

    /**
     * @see[addItems] is for assign new List
     * @param[newList] is for newList i.e of Generic Type
     * */
    fun addItems(newList: List<T>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    /**
     * @see[getItem] is return value at given position
     * @param[position] on which you want to fetch value
     *
     * */
    override fun getItem(position: Int): T {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * @see[getCount] is return total size of the list
     * @return[Int]
     * */
    override fun getCount(): Int = list.size


    /**
     * @param position Index of the item
     * @return True if the item is not a separator
     * */
    override fun isEnabled(position: Int): Boolean {
        return position != disabledPosition
    }

    fun getAllItems(): List<T> {
        return list.toList()
    }
}