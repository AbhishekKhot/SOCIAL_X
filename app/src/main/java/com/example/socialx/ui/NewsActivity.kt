package com.example.socialx.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialx.R
import com.example.socialx.adapter.NewsAdapter
import com.example.socialx.databinding.ActivityNewsBinding
import com.example.socialx.model.Article
import com.example.socialx.repository.NewsRepository
import com.example.socialx.util.Resource

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private var list= mutableListOf<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = NewsRepository()
        val viewModelProviderFactory = NewsViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        newsAdapter = NewsAdapter(list)

        binding.recyclerView.apply {
            this.layoutManager = LinearLayoutManager(this@NewsActivity)
            this.adapter = newsAdapter
        }


        viewModel.newsArticles.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    it.data?.let {
                        it.articles.forEach { article->
                            list.add(article)
                        }
                        newsAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                filterRecyclerItems(query!!)
                return true
            }
        })
    }

    private fun filterRecyclerItems(query: String) {
        val filteredList = mutableListOf<Article>()
        for(article in list){
            if(article.title.toLowerCase().contains(query.toLowerCase())){
                filteredList.add(article)
            }
        }
        newsAdapter.filterAdapter(filteredList)
    }
}