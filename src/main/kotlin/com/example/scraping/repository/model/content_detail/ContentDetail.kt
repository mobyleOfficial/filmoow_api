package com.example.scraping.repository.model.content_detail

import com.example.scraping.repository.model.Actor
import com.example.scraping.repository.model.SeenStatus

data class ContentDetail(
    val title: String,
    val originalTitle: String,
    val description: String,
    val generalScore: Double?,
    val userScore: Double?,
    val scoreQuantity: Int?,
    val movieClassification: String?,
    val duration: String,
    val releaseYear: Int,
    val seenStatus: SeenStatus,
    val genres: List<String>,
   // val directors: List<Director>,
    val coverImages: List<String>,
    val actors: List<Actor>,
    val recommendedContent: List<RecommendedContent>
)