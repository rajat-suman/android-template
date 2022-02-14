package com.template.networkcalls

import com.template.pref.PreferenceFile
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
       /* val refreshTokenIs = preferenceFile.retrieveKey(refreshToken)
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
        }*/
        return null
    }
}