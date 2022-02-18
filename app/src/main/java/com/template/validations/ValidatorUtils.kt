package com.template.validations

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.util.regex.Matcher
import java.util.regex.Pattern

object ValidatorUtils {

    //PasswordValidator
    fun validPassword(password: String): Boolean {
        val passwordPattern =
            "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@*#$%^&+=!])(?=\\S+$).{4,}$"
        val pattern: Pattern = Pattern.compile(passwordPattern)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }

    //EmailValidator

    fun isEmailValid(string: String): Boolean {
        return Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z]{2,5}" +
                    ")+"
        ).matcher(string.trim())
            .matches()
    }

    //MobileValidator

    fun isMobileValid(string: String): Boolean {
        return Pattern.compile("([0-9]{7,15})")
            .matcher(string.trim())
            .matches()
    }

    //Hide Keyboard
    fun hideSoftKeyboard(activity: Activity) {

        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideSoftKeyboard(mContext: Context, view: View) {
        view.isFocusable = false
        view.isFocusableInTouchMode = true
        val imm =
            mContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

}