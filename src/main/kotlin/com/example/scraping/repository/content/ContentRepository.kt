package com.example.scraping.repository.content

import org.springframework.http.ResponseEntity

interface ContentRepository {
    fun getContentDetail(token: String, id: String): ResponseEntity<Any>

    fun changeContentSeenStatus(token: String, id: String, status: String): ResponseEntity<Any>

    fun getContentComments(id: String, page: Int, token: String) : ResponseEntity<Any>

    fun addContentComment(id: String, token: String, message: String) : ResponseEntity<Any>
}