package com.template.networkcalls

import android.util.Log
import com.template.MainActivity
import com.template.pref.PreferenceFile
import com.template.pref.refreshToken
import com.template.pref.token
import com.template.pref.tokenType
import com.template.utils.sessionExpired
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator : Authenticator {
    @Inject
    lateinit var retrofitApi: RetrofitApi

    @Inject
    lateinit var preferenceFile: PreferenceFile

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshTokenIs = preferenceFile.retrieveKey(refreshToken)
        val responseRefresh = retrofitApi.refreshToken(refreshTokenIs?:"")
        return if (responseRefresh.isSuccessful) {
            val newAccessToken = responseRefresh.body()
            preferenceFile.storeKey(token, newAccessToken?.token ?: "")
            preferenceFile.storeKey(refreshToken, newAccessToken?.refreshToken ?: "")
            preferenceFile.storeKey(tokenType, newAccessToken?.type ?: "")

            response.request.newBuilder()
                .header(AUTH_PARAM, "${newAccessToken?.type} ${newAccessToken?.token}")
                .build()
        } else {
            Log.e("sessionExpired", "===sessionExpired")
            MainActivity.context.get()?.sessionExpired()
            null
        }
    }
}