package com.template.utils

import android.content.Context
import com.template.R
import java.lang.Math.floor
import java.lang.Math.log10
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

val df = DecimalFormat("#,###,##0.00", DecimalFormatSymbols(Locale.US))

fun Context.prettyCount(number: Long): String? {
    val suffix = resources.getStringArray(R.array.number_count_suffix)
    val value = kotlin.math.floor(kotlin.math.log10(number.toDouble())).toInt()
    val base = value / 3
    return if (value >= 3 && base < suffix.size) {
        DecimalFormat("#0.0").format(
                number / Math.pow(
                        10.0,
                        (base * 3).toDouble()
                )
        ) + suffix[base]
    } else {
        DecimalFormat("#,##0").format(number)
    }
}