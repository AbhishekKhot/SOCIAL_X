package com.example.socialx.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialx.R
import com.example.socialx.databinding.NewsItemBinding
import com.example.socialx.model.Article

class NewsAdapter(var list:MutableList<Article>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root)

//    val diffCallback = object : DiffUtil.ItemCallback<Article>() {
//        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
//            return oldItem.title == newItem.title
//        }
//
//        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
//            return oldItem == newItem
//        }
//    }
//
//    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = list[position]
        holder.binding.apply {
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvSource.text = article.source.toString()
            tvPublishedAt.text = article.publishedAt.toString()
        }
        Glide.with(holder.itemView).load(article.urlToImage)
            .placeholder(R.drawable.ic_image).into(holder.binding.ivArticleImage)
    }

    override fun getItemCount(): Int = list.size

    fun filterAdapter(filteredArticles:MutableList<Article>){
        list.clear()
        for (article in filteredArticles){
            list.add(article)
        }
        notifyDataSetChanged()
    }

}