package com.template.networkcalls

import com.template.models.BaseResponse
import retrofit2.Response
import retrofit2.http.*

interface RetrofitApi {

    @FormUrlEncoded
    @POST(LOGIN_API)
    suspend fun login(
        @Field("username") username: String?,
        @Field("password") password: String?,
        @Field("deviceType") deviceType: String?,
        @Field("deviceToken") deviceToken: String?
    ): Response<BaseResponse<Any>>

}