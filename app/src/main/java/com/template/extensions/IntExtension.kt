package com.template.extensions

import android.content.Context
import android.util.DisplayMetrics
import kotlin.math.roundToInt

fun Int.dpToPx(context : Context): Int {
    val displayMetrics: DisplayMetrics = context.resources.displayMetrics
    return (this * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}