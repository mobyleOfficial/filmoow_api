package com.example.scraping.repository.content

import org.springframework.http.ResponseEntity

interface ContentRepository {
    fun getMovieDetail(token: String, id: String): ResponseEntity<Any>

    fun changeContentSeenStatus(token: String, id: String, status: String): ResponseEntity<Any>
}