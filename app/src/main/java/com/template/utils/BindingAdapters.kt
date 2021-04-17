package com.template.utils

import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mukesh.mukeshotpview.completeListener.MukeshOtpCompleteListener
import com.mukesh.mukeshotpview.mukeshOtpView.MukeshOtpView
import com.squareup.picasso.Picasso

/** Binding Adapters */
object BindingAdapters {

    @BindingAdapter(value = ["setRecyclerAdapter"], requireAll = false)
    @JvmStatic
    fun setRecyclerAdapter(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>
    ) {
        recyclerView.adapter = adapter
    }


    @BindingAdapter(value = ["addScrollListener"], requireAll = false)
    @JvmStatic
    fun addScrollListener(
        recyclerView: RecyclerView,
        listener: RecyclerView.OnScrollListener
    ) {
        recyclerView.addOnScrollListener(listener)
    }

    @BindingAdapter(value = ["otpListener"], requireAll = false)
    @JvmStatic
    fun otpListener(
        otpView: MukeshOtpView,
        listener: MukeshOtpCompleteListener
    ) {
        otpView.setOtpCompletionListener(listener)
    }

    @BindingAdapter(value = ["bottomNavigationListener"], requireAll = false)
    @JvmStatic
    fun bottomNavigationListener(
        bottomNavigationView: BottomNavigationView,
        listener: BottomNavigationView.OnNavigationItemSelectedListener
    ) {
        bottomNavigationView.setOnNavigationItemSelectedListener(listener)
    }

    @BindingAdapter(value = ["setColorOfText"], requireAll = false)
    @JvmStatic
    fun setColorOfText(
        textView: TextView,
        color: Int
    ) {
        textView.setTextColor(textView.context.getColor(color))
    }

    @BindingAdapter(value = ["onCheckChange"], requireAll = false)
    @JvmStatic
    fun onCheckChange(
        compoundButton: CompoundButton,
        listener: CompoundButton.OnCheckedChangeListener
    ) {
        compoundButton.setOnCheckedChangeListener(listener)
    }

    @BindingAdapter(value = ["setImageUrl"], requireAll = false)
    @JvmStatic
    fun setImageUrl(
        imageView: ImageView,
        url: String?
    ) {
        Log.d("ImageUrlIs", "+=======$url")
        when {
            url?.startsWith("/storage")!! -> Picasso.get().load(url).into(imageView)
            else -> Picasso.get().load(url).into(imageView)
        }

    }

    @BindingAdapter(value = ["setDrawable"], requireAll = false)
    @JvmStatic
    fun setDrawable(
        imageView: ImageView,
        drawable: Int
    ) {
        imageView.setImageResource(drawable)
    }

    @BindingAdapter(value = ["setBackground"], requireAll = false)
    @JvmStatic
    fun setBackground(
        view: View,
        drawable: Int
    ) {
        view.background = ContextCompat.getDrawable(view.context, drawable)
    }

    @BindingAdapter(value = ["radioGroupListener"], requireAll = false)
    @JvmStatic
    fun radioGroupListener(
        view: RadioGroup,
        listener: RadioGroup.OnCheckedChangeListener
    ) {
        view.setOnCheckedChangeListener(listener)
    }

    @BindingAdapter(value = ["addTextWatcher"], requireAll = false)
    @JvmStatic
    fun addTextWatcher(
        view: EditText,
        listener: TextWatcher
    ) {
        view.addTextChangedListener(listener)
    }
}