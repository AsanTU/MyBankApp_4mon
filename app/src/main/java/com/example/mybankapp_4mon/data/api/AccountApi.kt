package com.example.mybankapp_4mon.data.api

import com.example.mybankapp_4mon.data.model.Account
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AccountApi {

    @GET("accounts")
    fun getAccounts(): Call<List<Account>>

    @POST("accounts")
    fun createAccount(@Body account: Account): Call<Account>
}