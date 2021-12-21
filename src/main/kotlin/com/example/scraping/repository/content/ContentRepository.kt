package com.example.scraping.repository.content

import org.springframework.http.ResponseEntity

interface ContentRepository {
    fun getMovieDetail(id: String): ResponseEntity<Any>

    fun changeContentSeenStatus(id: String, status: String): ResponseEntity<Any>
}