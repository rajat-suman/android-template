package com.template.models

import java.io.Serializable

data class SignUpData(
    var _id: String = "",
    var email: String = "",
    var phone: String = ""
) : Serializable