package com.example.socialx.model

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)