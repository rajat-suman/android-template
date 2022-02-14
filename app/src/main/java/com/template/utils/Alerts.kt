package com.template.utils


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.template.R
import com.template.databinding.AlertLayoutBinding
import com.template.databinding.ConfirmAlertBinding
import com.template.databinding.ProgressLayoutBinding

fun View.showAlertFullScreen(
    @LayoutRes layout: Int,
    cancelable: Boolean = false,
    viewSend: (View, Dialog) -> Unit
) {
    try {
        val dialog = Dialog(this.context, R.style.DialogFullScreen)
        val view = LayoutInflater.from(this.context).inflate(layout, null)
        dialog.setContentView(view)
        dialog.setCancelable(cancelable)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        viewSend(view, dialog)
        dialog.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun View.showConfirmDialog(message:String, data:()->Unit){
    showAlertWrap(R.layout.confirm_alert, cancelable = true, cancelableOnTouchOutside = true){view, dialog->
        val binding  = ConfirmAlertBinding.bind(view)
        binding.tvText.text= message
        binding.btNo.setOnClickListener {
            dialog.dismiss()
        }
        binding.btYes.setOnClickListener {
            data()
            dialog.dismiss()
        }
    }
}
fun View.showAlertWrap(
    @LayoutRes layout: Int,
    cancelable: Boolean = false,
    cancelableOnTouchOutside: Boolean = false,
    viewSend: (View, Dialog) -> Unit
) {
    try {
        val dialog = Dialog(this.context)
        val view = LayoutInflater.from(this.context).inflate(layout, null)
        dialog.setContentView(view)
        dialog.setCancelable(cancelable)
        dialog.setCanceledOnTouchOutside(cancelableOnTouchOutside)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        viewSend(view, dialog)
        dialog.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
//ErrorAlert

fun Context.alert(message: String) {
    try {
        val builder = AlertDialog.Builder(this)
        val layoutView = AlertLayoutBinding.inflate(LayoutInflater.from(this), null, false)
        builder.setCancelable(false)
        builder.setView(layoutView.root)
        val dialog = builder.create()

        layoutView.tvMessage.text = message
        layoutView.tvOkButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

//Loader

private var customDialog: AlertDialog? = null

fun Context.showProgress() {
    hideProgress()
    val customAlertBuilder = AlertDialog.Builder(this)
    val customAlertView =
        ProgressLayoutBinding.inflate(LayoutInflater.from(this), null, false)
    customAlertBuilder.setView(customAlertView.root)
    customAlertBuilder.setCancelable(false)
    customDialog = customAlertBuilder.create()

    customDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    customDialog?.show()
}

fun hideProgress() {
    try {
        if (customDialog != null && customDialog?.isShowing!!) {
            customDialog?.dismiss()
        }
    }catch (e:Exception){
        e.printStackTrace()
    }
}

/**Session Expired Alert*/
var sessionExpired:AlertDialog?=null
fun Context.sessionExpired() = try {
    sessionExpired?.dismiss()
    val aD = AlertDialog.Builder(this)
    aD.setTitle(getString(R.string.session_expired))
    aD.setCancelable(false)
    aD.setPositiveButton(getString(R.string.ok)) { dialogInterface, i ->

    }
    sessionExpired=  aD.create()
    aD.show()
} catch (e: Exception) {
    e.printStackTrace()
}!!


