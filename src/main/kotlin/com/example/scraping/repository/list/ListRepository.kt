package com.example.scraping.repository.list

import com.example.scraping.repository.model.user_listing.UserListing
import org.springframework.http.ResponseEntity

interface ListRepository {
    fun getPopularLists(page: Int): ResponseEntity<List<UserListing>>

    fun getWeekPopularList(): ResponseEntity<List<UserListing>>
}