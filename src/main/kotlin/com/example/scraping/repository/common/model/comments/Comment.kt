package com.example.scraping.repository.common.model.comments

import com.example.scraping.repository.common.model.User

data class Comment(
    val id: String,
    val owner: User,
    val creationTime: String,
    val comment: String,
    val spoilerList: List<String>?,
    val stats: CommentStats,
)