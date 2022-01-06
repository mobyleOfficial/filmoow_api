package com.example.scraping.controller

import com.example.scraping.repository.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping(value = ["/profile"])
class UserController {
    @Autowired
    lateinit var userRepository: UserRepository

    @GetMapping()
    fun getUserInformation(@RequestHeader("Authorization") token: String) = userRepository.getUserInformation(token)
}