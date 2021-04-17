package com.template.recycleradapter

abstract class AbstractModel{
    var adapterPosition: Int = -1
    var onItemClick: RecyclerAdapter.OnItemClick? = null
}