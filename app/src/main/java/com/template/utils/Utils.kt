package com.template.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.template.R
import com.template.validations.ValidatorUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*


/** Fragment Transaction */
fun Context.loadFragment(fragment: Fragment, bundle: Bundle?) {

    ValidatorUtils.hideSoftKeyboard(this as Activity)

    if (bundle != null)
        fragment.arguments = bundle

    val fragmentManager =
        (this as FragmentActivity).supportFragmentManager.beginTransaction()

/*    fragmentManager
        .setCustomAnimations(
            R.anim.enter,
            R.anim.exit,
            R.anim.pop_enter,
            R.anim.pop_exit
        )
        .replace(R.id.flHome, fragment)
        .addToBackStack(null)
        .commit()*/

}


fun Context.pop() {
    val fragmentManager =
        (this as FragmentActivity).supportFragmentManager
    fragmentManager.popBackStack()
}

fun Context.loadFragmentAndClearStack(fragment: Fragment, bundle: Bundle?) {
    ValidatorUtils.hideSoftKeyboard(this as Activity)
    if (bundle != null)
        fragment.arguments = bundle
    val fragmentManager =
        (this as FragmentActivity).supportFragmentManager
    fragmentManager.popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    /* fragmentManager.beginTransaction()
         .setCustomAnimations(
             R.anim.enter,
             R.anim.exit,
             R.anim.pop_enter,
             R.anim.pop_exit
         )
         .replace(R.id.flHome, fragment)
         .addToBackStack(null)
         .commit()*/

}

fun Context.popCurrentAndLoad(fragment: Fragment, bundle: Bundle?) {
    ValidatorUtils.hideSoftKeyboard(this as Activity)
    if (bundle != null)
        fragment.arguments = bundle
    val fragmentManager =
        (this as FragmentActivity).supportFragmentManager
    fragmentManager.popBackStack()
    /*  fragmentManager.beginTransaction()
          .setCustomAnimations(
              R.anim.enter,
              R.anim.exit,
              R.anim.pop_enter,
              R.anim.pop_exit
          )
          .replace(R.id.flHome, fragment)
          .addToBackStack(null)
          .commit()*/
}

/**ActivityTransaction*/
fun Context.openActivity(intent: Intent, finishAffinity: Boolean) {
    ValidatorUtils.hideSoftKeyboard(this as Activity)
    startActivity(intent)
//    this.overridePendingTransition(R.anim.slide_up, R.anim.slide_up)
    if (finishAffinity) this.finishAffinity()
}

/** backPress */
fun Context.goBack() {
    ValidatorUtils.hideSoftKeyboard(this as Activity)
    this.onBackPressed()
}

/** monthName */
fun getMonthName(int: Int): String {
    return when (int) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        12 -> "December"
        else -> ""
    }
}

/** Status Bar Color Changer */
fun Context.setStatusBarColor(color: Int) {
    try {
        (this as Activity).window.statusBarColor = getResources().getColor(
            color,
            this.getTheme()
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/** Internet Connection Detector */
fun Context.isNetworkAvailable(): Boolean {
    val service = Context.CONNECTIVITY_SERVICE
    val manager = getSystemService(service) as ConnectivityManager?
    val network = manager?.activeNetworkInfo
    return (network != null)
}

/** Positive Alerter*/
fun Context.showNegativeAlerter(message: String) {
    Snackbar.make(
        (this as Activity).findViewById(android.R.id.content),
        message,
        Snackbar.LENGTH_SHORT
    ).show()

}

/** Negative Alerter*/
fun Context.showPositiveAlerter(message: String) {
    Snackbar.make(
        (this as Activity).findViewById(android.R.id.content),
        message,
        Snackbar.LENGTH_SHORT
    ).show()
}

/**Animation Util*/
fun Context.playAnim(view: View, anim: Int) {
    val myAnim = AnimationUtils.loadAnimation(this, anim)
    view.startAnimation(myAnim)
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

/**string to part request body*/
fun getPartRequestBody(string: String?): RequestBody? {
    return string?.trim()?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
}

/**File to Part*/
fun getPartFromFile(
    string: String,
    param: String,
    contentType: String
): MultipartBody.Part? {
    return if (string.isNotEmpty()) {
        val file = File(string)
        val reqFile = file.asRequestBody(contentType.toMediaTypeOrNull())
        MultipartBody.Part.createFormData(param, file.name, reqFile)
    } else
        null
}

/**Change Time Format*/
@SuppressLint("SimpleDateFormat")
fun changeTimeFormat(input: String): String {
    var output = ""
    try {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date = simpleDateFormat.parse(input)
        val simpleDateFormat1 = SimpleDateFormat("dd-MM-yyyy")
        output = simpleDateFormat1.format(date!!)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return output
}

@SuppressLint("SimpleDateFormat")
fun getTimeStampFromMillis(milliSec: Long): String {
    var output = ""
    try {
        val simple = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
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
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    sdf.timeZone = TimeZone.getTimeZone("GMT")
    return try {
        val time = sdf.parse(date).time
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
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

@SuppressLint("SimpleDateFormat")
fun getDateBooking(input: String?): String {
    var output = ""
    if (!input.isNullOrEmpty()) {
        try {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val date = simpleDateFormat.parse(input)
            val simpleDateFormat1 = SimpleDateFormat("MMM dd, yyyy")
            output = simpleDateFormat1.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return output
}

@SuppressLint("SimpleDateFormat")
fun getTimeBooking(input: String): String {
    var output = ""
    try {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").apply {
            timeZone = TimeZone.getTimeZone("GMT")
        }.parse(input)
        output = SimpleDateFormat("HH:mm a").format(simpleDateFormat!!)
    } catch (e: Exception) {
    }
    return output
}

/**Session Expired Alert*/

/*@Inject
private lateinit var dataStoreUtil: DataStoreUtil*/

fun Context.sessionExpired() = try {

    val aD = AlertDialog.Builder(this)
    aD.setTitle(getString(R.string.session_expired))
    aD.setCancelable(false)
    aD.setPositiveButton(getString(R.string.ok)) { dialogInterface, i ->
        /* dataStoreUtil.removeKey(LOGIN_DATA) {}
         dataStoreUtil.removeKey(REMEMBER) {
             dialogInterface.cancel()
             dialogInterface.dismiss()
             startActivity(Intent(this, MainActivity::class.java))
             (this as MainActivity).finishAffinity()
         }*/
    }
    aD.create()
    aD.show()
} catch (e: Exception) {
    e.printStackTrace()
}!!

fun Context.printHashKey() {
    try {
        val info = packageManager.getPackageInfo(
            "com.baron",
            PackageManager.GET_SIGNATURES
        )
        for (signature in info.signatures) {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.e("KeyHash:", encodeToString(md.digest(), DEFAULT))
        }
    } catch (e: PackageManager.NameNotFoundException) {

    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }

}


