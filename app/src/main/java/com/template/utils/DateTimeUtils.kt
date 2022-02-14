package com.template.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.text.format.DateUtils
import androidx.databinding.ObservableField
import java.text.SimpleDateFormat
import java.util.*

val TIME_STAMP= "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
val DD_MM_YYYY= "dd-MM-yyyy"
const val YYYY_MM_DD_FORMAT = "yyyy-MM-dd"
const val YYYY_MM_DD_HH_MM_FORMAT = "yyyy-MM-dd HH:mm a"
const val YYYY_MMM_COMMA_DD_FORMAT = "yyyy MMM, dd"
const val HH_mm_a_FORMAT = "hh:mm a"


/**UTC TO LOCAL*/
@SuppressLint("SimpleDateFormat")
fun getUtcToLocal(input: String): String {
    var output = ""
    try {
        val simpleDateFormat = SimpleDateFormat(TIME_STAMP).apply {
            timeZone = TimeZone.getTimeZone("GMT")
        }.parse(input)
        output = SimpleDateFormat(TIME_STAMP, Locale.getDefault()).format(
            simpleDateFormat!!
        )
    } catch (e: Exception) {
    }
    return output
}

/**Change Time Format*/
@SuppressLint("SimpleDateFormat")
fun changeTimeFormat(input: String, inputFormat:String= TIME_STAMP, outputFormat:String= DD_MM_YYYY ): String {
    var output = ""
    try {
        val simpleDateFormat = SimpleDateFormat(inputFormat)
        val date = simpleDateFormat.parse(input)
        date?.let {
            output =   SimpleDateFormat(outputFormat) .format(date)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return output
}

@SuppressLint("SimpleDateFormat")
fun getDateFromMillis(milliSec: Long, outputFormat:String= TIME_STAMP): String {
    var output = ""
    try {
        val simple = SimpleDateFormat(outputFormat)
        val result = Date(milliSec)
        output = simple.format(result)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return output
}

@SuppressLint("SimpleDateFormat")
fun getTimeAgo(date: String?): String {
    if (date.isNullOrEmpty())
        return ""
    val sdf = SimpleDateFormat(TIME_STAMP)
    sdf.timeZone = TimeZone.getTimeZone("GMT")
    return try {
        sdf.parse(date)?.let {
            val time = it.time
            val now = System.currentTimeMillis()
            return when (val ago =
                DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)) {
                "0 minutes ago" -> {
                    "few minutes ago"
                }
                "In 0 minutes" -> {
                    "few minutes ago"
                }
                else -> {
                    ago.toString()
                }
            }
        }
        return ""

    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun parseDate(utcStringDate: String = TIME_STAMP, date: String): Date? {
    return if (date.isNotEmpty()) {
        val inputFormat = SimpleDateFormat(utcStringDate, Locale.getDefault())
        return inputFormat.parse(date)
    } else Date()
}

/**DatePicker*/
fun Context.selectDate(observableField: ObservableField<String>) {
    val calender: Calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        this,
        { view, year, month, dayOfMonth ->
            calender.set(Calendar.YEAR, year)
            calender.set(Calendar.MONTH, month)
            calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            observableField.set("$year-${month + 1}-$dayOfMonth")
        }, calender
            .get(Calendar.YEAR), calender.get(Calendar.MONTH),
        calender.get(Calendar.DAY_OF_MONTH)
    )
    datePicker.datePicker.maxDate = System.currentTimeMillis()
    datePicker.show()
}

