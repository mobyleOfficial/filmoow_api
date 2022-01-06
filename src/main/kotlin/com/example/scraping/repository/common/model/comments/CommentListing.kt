package com.example.scraping.repository.common.model.comments

data class CommentListing(
    val hasNext: Boolean,
    val commentList: List<Comment>,
)