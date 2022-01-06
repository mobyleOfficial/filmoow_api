package com.example.scraping.repository.movie.model

import com.example.scraping.repository.common.model.SeenStatus

data class Movie(
    val id: String,
    val name: String,
    val image: String,
    val score: Double?,
    val commentsQuantity: Int?,
    val seenStatus: SeenStatus?,
)