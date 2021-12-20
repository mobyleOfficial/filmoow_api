package com.example.scraping.repository.model.user_listing

data class UserListing(
    val id: String,
    val name: String,
    val description: String?,
    val owner: String?,
    val stats: ListStats,
    val imagesList: List<String>
)