package com.template.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.databinding.ObservableField
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
import java.util.*

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

fun Context.dialPhone(phone: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:${phone}")
    startActivity(intent)
}



