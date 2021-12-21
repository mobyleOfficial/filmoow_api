package com.example.scraping.repository.tvShow

import com.example.scraping.repository.tvShow.model.TvShow
import org.springframework.http.ResponseEntity

interface TvShowRepository {
    fun getPopularTvShow(page: Int): ResponseEntity<List<TvShow>>
}