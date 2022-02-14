package com.template.networkcalls


import android.util.Log
import com.template.MainActivity
import com.template.R
import com.template.datastore.DataStoreUtil
import com.template.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


class Repository @Inject constructor(
    private val retrofitApi: RetrofitApi,
    private val cacheUtil: CacheUtil,
    private val dataStoreUtil: DataStoreUtil
) {
    fun <T> makeCall(
        apiKey: ApiEnums,
        loader: Boolean,
        saveInCache: Boolean,
        requestProcessor: ApiProcessor<Response<T>>
    ) {
        val activity = MainActivity.context.get()!!
        if (cacheUtil.snapshot().containsKey(apiKey)) {
            Log.d("cacheUtil", "========${cacheUtil[apiKey]}")
            requestProcessor.onResponse(cacheUtil[apiKey] as Response<T>)
            return
        }
        if (!activity.isNetworkAvailable()) {
            activity.showNegativeAlerter(activity.getString(R.string.your_device_offline))
            return
        }

        if (loader) {
            activity.showProgress()
        }

        val dataResponse: Flow<Response<T>> = flow {
            val response =
                requestProcessor.sendRequest(retrofitApi)
            emit(response)
        }.flowOn(Dispatchers.IO)

        CoroutineScope(Dispatchers.Main).launch {
            dataResponse.catch { exception ->
                exception.printStackTrace()
                hideProgress()
                activity.showNegativeAlerter(exception.message ?: "")
            }.collect { response ->
                Log.d("resCodeIs", "====${response.code()}")
                hideProgress()
                when {
                    response.code() in 100..199 -> {
                        /**Informational*/
                        requestProcessor.onError(
                            activity.resources?.getString(R.string.some_error_occured) ?: ""
                        )
                        activity.showNegativeAlerter(
                            activity.resources?.getString(R.string.some_error_occured) ?: ""
                        )
                    }
                    response.isSuccessful -> {
                        /**Success*/
                        if (saveInCache)
                            cacheUtil.put(apiKey, response as Response<Any>)
                        requestProcessor.onResponse(response )
                    }
                    response.code() in 300..399 -> {
                        /**Redirection*/
                        requestProcessor.onError(
                            activity.resources?.getString(R.string.some_error_occured) ?: ""
                        )
                        activity.showNegativeAlerter(
                            activity.resources?.getString(R.string.some_error_occured) ?: ""
                        )
                    }
                    response.code() == 401 -> {
                        /**UnAuthorized*/
                        getRefreshToken()
                        activity.sessionExpired()
                        requestProcessor.onError("unAuthorized")
                    }
                    response.code() == 404 -> {
                        /**Page Not Found*/
                        requestProcessor.onError(
                            activity.resources?.getString(R.string.some_error_occured) ?: ""
                        )
                        activity.showNegativeAlerter(
                            activity.resources?.getString(R.string.some_error_occured) ?: ""
                        )
                    }
                    response.code() in 500..599 -> {
                        /**ServerErrors*/
                        requestProcessor.onError(
                            activity.resources?.getString(R.string.some_error_occured) ?: ""
                        )
                        activity.showNegativeAlerter(
                            activity.resources?.getString(R.string.some_error_occured) ?: ""
                        )
                    }
                    else -> {
                        /**ClientErrors*/
                            response.errorBody()?.string()?.let {res->

                                val jsonObject = JSONObject(res)
                                when {
                                    jsonObject.has("message") -> {
                                        requestProcessor.onError(jsonObject.getString("message"))

                                        if (!jsonObject.getString("message").equals("Data not found", true))
                                            activity
                                                .showNegativeAlerter(jsonObject.getString("message"))
                                    }
                                    else -> {
                                        requestProcessor.onError(
                                            activity.resources?.getString(R.string.some_error_occured)
                                                ?: ""
                                        )
                                        activity.showNegativeAlerter(
                                            activity.resources?.getString(R.string.some_error_occured)
                                                ?: ""
                                        )
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

    private fun getRefreshToken() {

    }

}
