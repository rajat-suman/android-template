package com.template.genericadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewpager.widget.PagerAdapter
import com.template.BR

class ViewPagerAdapter<T : AbstractModel>(@LayoutRes val layoutId: Int) : PagerAdapter() {
    private val itemsList = ArrayList<T>()
    private var onItemClick: RecyclerAdapter.OnItemClick? = null
    fun setOnItemClick(onItemClick: RecyclerAdapter.OnItemClick?) {
        this.onItemClick = onItemClick
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int = itemsList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(container.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutId, container, false)
        val model = itemsList[position]
        model.vpPosition = position
        onItemClick?.let { model.onItemClick = it }
        binding.setVariable(BR.model, model)
        binding.executePendingBindings()
        val view: View = binding.root
        container.addView(view, 0)
        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun addItems(items: List<T>) {
        itemsList.clear()
        itemsList.addAll(items)
        notifyDataSetChanged()
    }

}