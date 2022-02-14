package com.template.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File


/**string to part request body*/
fun String?.getPartRequestBody(): RequestBody? {
    return this?.trim()?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
}

/**File to Part*/
fun String?.getPartFromFile(
    param: String,
    contentType: String
): MultipartBody.Part? {
    return if (!isNullOrBlank()) {
        val file = File(this)
        val reqFile = file.asRequestBody(contentType.toMediaTypeOrNull())
        MultipartBody.Part.createFormData(param, file.name, reqFile)
    } else
        null
}

/**JSON to request body*/
fun JSONObject.getRequestBody(mediaType: String = "application/json"): RequestBody {
    return this.toString().toRequestBody(mediaType.toMediaTypeOrNull())
}

/**JSONArray to request body*/
fun JSONArray.getRequestBody(mediaType: String = "application/json"): RequestBody {
    return this.toString().toRequestBody(mediaType.toMediaTypeOrNull())
}

/**String to request body*/
fun String.getRequestBody(mediaType: String = "application/json"): RequestBody {
    return this.toRequestBody(mediaType.toMediaTypeOrNull())
}

