package com.template.models

data class BaseResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val code: Int? = null,
    val pageCount: Int? = null,
    val total: Int? = null,
)
