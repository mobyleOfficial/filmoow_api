package com.example.scraping.repository.news

import com.example.scraping.repository.news.model.News
import org.springframework.http.ResponseEntity

interface NewsRepository {
    fun getLastNews(page: Int): ResponseEntity<List<News>>
}