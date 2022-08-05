package com.example.socialx.repository

import com.example.socialx.network.RetrofitInstance

class NewsRepository {
    suspend fun getNewsArticles(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getNewsArticles(countryCode, pageNumber)
}