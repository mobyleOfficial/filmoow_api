package com.example.scraping.repository.model.news

data class News(
    val id: String,
    val title: String,
    val creation: String,
    val coverImage: String,
    val stats: NewsStats,
)