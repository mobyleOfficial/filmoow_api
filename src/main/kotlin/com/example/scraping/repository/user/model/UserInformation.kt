package com.example.scraping.repository.user.model

data class UserInformation(
    val name: String,
    val userName: String,
    val imageUrl: String?,
    val seenNumber : Int,
    val commentNumber : Int,
    val listNumber : Int,
    val timeSpent : String,

)