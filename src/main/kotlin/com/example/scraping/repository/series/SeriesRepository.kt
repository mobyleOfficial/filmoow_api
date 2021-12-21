package com.example.scraping.repository.series

import com.example.scraping.repository.series.model.Series
import org.springframework.http.ResponseEntity

interface SeriesRepository {
    fun getPopularSeries(page: Int): ResponseEntity<List<Series>>
}