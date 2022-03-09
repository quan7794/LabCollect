package com.wac.labcollect.data.network

import com.wac.labcollect.domain.models.Test
import com.wac.labcollect.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface TemplateAPI {
    @GET(Constants.API_ENDPOINT)
    suspend fun getTemplates(@Query(Constants.API_KEY) apiKey: String): List<Test>
}