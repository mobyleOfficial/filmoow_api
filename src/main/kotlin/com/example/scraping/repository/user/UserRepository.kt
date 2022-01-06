package com.example.scraping.repository.user

import org.springframework.http.ResponseEntity

interface UserRepository {
    fun getUserInformation(token: String) : ResponseEntity<Any>
}