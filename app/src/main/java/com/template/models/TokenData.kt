package com.template.models

import java.io.Serializable

data class TokenData(
    var expire: Int = 0,
    var jwtCreatedAt: Int = 0,
    var refreshToken: String = "",
    var token: String = "",
    var type: String = ""
) : Serializable