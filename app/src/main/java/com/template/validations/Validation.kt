package com.template.validations

import android.content.Context
import com.template.utils.showNegativeAlerter

class Validation(private val baseContext: Context?) {
    private var validators: ArrayList<Validator>? = null

    init {
        validators = ArrayList()
    }

    companion object {
        fun create(baseContext: Context?): Validation {
            return Validation(baseContext)
        }
    }

    fun isEmpty(editable: String?, message: String): Validation {
        validators?.add(EmptyValidator(editable ?: "", message))
        return this
    }

    fun isEmailValid(editable: String?, message: String): Validation {
        validators?.add(EmailValidator(editable ?: "", message))
        return this
    }

    fun isPhoneValid(editable: String?, message: String): Validation {
        validators?.add(PhoneValidator(editable ?: "", message))
        return this
    }

    fun isValidPassword(editable: String?, message: String): Validation {
        validators?.add(PasswordValidator(editable ?: "", message))
        return this
    }

    fun areEqual(editable: String?, editable2: String?, message: String): Validation {
        validators?.add(CompareValidator(editable ?: "", editable2 ?: "", message))
        return this
    }

    fun areOldAndNewNotEqual(editable: String?, editable2: String?, message: String): Validation {
        validators?.add(CompareNotValidator(editable ?: "", editable2 ?: "", message))
        return this
    }

    fun isValid(): Boolean {
        validators.let {
            it?.forEach { validator ->
                if (!validator.isValid()) {
                    baseContext?.showNegativeAlerter(validator.message() ?: "")
                    return false
                }
            }
        }
        return true
    }

}