package com.example.scraping.repository.list.model

data class UserListing(
    val id: String,
    val name: String,
    val description: String?,
    val owner: String?,
    val stats: ListStats,
    val imagesList: List<String>
)