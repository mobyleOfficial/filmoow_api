package com.example.scraping.repository.tvShow.model

data class TvShow(
    val id: String,
    val name: String,
    val image: String,
    val score: Double?,
    val commentsQuantity: Int?,
)