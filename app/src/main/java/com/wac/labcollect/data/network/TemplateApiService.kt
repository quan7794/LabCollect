package com.wac.labcollect.data.network

import com.wac.labcollect.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TemplateApiService {
    private fun getRetrofit() = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: TemplateAPI = getRetrofit().create(TemplateAPI::class.java)

}