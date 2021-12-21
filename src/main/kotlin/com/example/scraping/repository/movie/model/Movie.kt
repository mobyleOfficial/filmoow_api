package com.example.scraping.repository.movie.model

data class Movie(
    val id: String,
    val name: String,
    val image: String,
    val score: Double?,
    val commentsQuantity: Int?
)