package com.template.networkcalls

import com.template.models.ForgotPasswordData
import com.template.models.LoginData
import com.template.models.SignUpData
import com.template.models.TokenData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface RetrofitApi {
    @FormUrlEncoded
    @POST(REFRESH_TOKEN)
    fun refreshToken(
        @Field("refreshToken") refreshToken: String
    ): Response<TokenData>

    @FormUrlEncoded
    @POST(LOGIN_API)
    suspend fun login(
        @Field("username") username: String?,
        @Field("password") password: String?,
        @Field("deviceType") deviceType: String?,
        @Field("deviceToken") deviceToken: String?
    ): Response<LoginData>

    @FormUrlEncoded
    @POST(SIGNUP)
    suspend fun signUp(
        @Field("email") email: String?,
        @Field("countryCode") countryCode: String?,
        @Field("phone") phone: String?,
        @Field("password") password: String?,
        @Field("verificationType") verificationType: Int,
    ): Response<SignUpData>

    @FormUrlEncoded
    @POST(VERIFY_OTP)
    suspend fun verifyOtp(
        @Field("key") key: String?,
        @Field("code") code: String?,
    ): Response<TokenData>

    @FormUrlEncoded
    @POST(RESEND_OTP)
    suspend fun resendOtp(
        @Field("phone") phone: String?,
        @Field("id") id: String?,
    ): Response<TokenData>

    @FormUrlEncoded
    @POST(FORGOT_PASSWORD)
    suspend fun forgotPassword(
        @Field("key") key: String?,
    ): Response<ForgotPasswordData>

    @FormUrlEncoded
    @POST(CHANGE_PASSWORD)
    suspend fun changePassword(
        @Header(AUTH_PARAM) authToken: String?,
        @Field("password") password: String?,
        @Field("oldPassword") oldPassword: String? = null,
    ): Response<Any>

    @Multipart
    @POST(RESEND_OTP)
    suspend fun updateProfile(
        @Header(AUTH_PARAM) authToken: String?,
        @PartMap hashMap: HashMap<String, RequestBody?>,
        @Part part: MultipartBody.Part?
    ): Response<LoginData>
}