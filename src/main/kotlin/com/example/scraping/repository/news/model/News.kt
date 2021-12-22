package com.example.scraping.repository.news.model

data class News(
    val id: String,
    val title: String,
    val creation: String,
    val coverImage: String,
    val stats: NewsStats,
)