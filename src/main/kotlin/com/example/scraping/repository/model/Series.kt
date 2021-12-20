package com.example.scraping.repository.model

data class Series(
    val id: String,
    val name: String,
    val image: String,
    val score: Double?,
    val commentsQuantity: Int?,
)