package com.example.scraping.repository.common.model.comments

data class CommentStats(
    val rating: Double?,
    val likesQuantity: Int,
    val repliesQuantity: Int,
    val hasDislikes: Boolean,
    val hasUserLike: Boolean,
)