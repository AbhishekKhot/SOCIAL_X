package com.example.socialx.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialx.model.NewsResponse
import com.example.socialx.repository.NewsRepository
import com.example.socialx.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val repository: NewsRepository): ViewModel() {

    val newsArticles : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    init {
        getNewsArticle("us")
    }

    private fun getNewsArticle(countryCode:String) = viewModelScope.launch {
        newsArticles.postValue(Resource.Loading())
        val response = repository.getNewsArticles(countryCode, breakingNewsPage)
        newsArticles.postValue(handleNewsResponse(response))
    }

    private fun handleNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}