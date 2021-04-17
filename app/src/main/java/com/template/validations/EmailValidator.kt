package com.template.validations


class EmailValidator(private val editable: String, val message: String) :
    Validator {
    override fun isValid(): Boolean {
        return ValidatorUtils.isEmailValid(editable.trim())
    }

    override fun message(): String {
        return message
    }
}