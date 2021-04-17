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

        val dataResponse: Flow<Response<Any>> = flow {
            val response =
                requestProcessor.sendRequest(retrofitApi) as Response<Any>
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
                        Log.d("successBody", "====${response.body()}")
                        if (saveInCache)
                            cacheUtil.put(apiKey, response)
                        requestProcessor.onResponse(response as Response<T>)
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
                        Log.d("errorBody", "====${response.errorBody()?.string()}")
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
                        val res = response.errorBody()!!.string()
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

    private fun getRefreshToken() {

    }

}
