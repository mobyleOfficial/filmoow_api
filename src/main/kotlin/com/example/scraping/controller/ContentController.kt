package com.example.scraping.controller

import com.example.scraping.repository.content.ContentRepositoryImpl
import com.example.scraping.repository.content.model.ChangeSeenStatusRequestBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping(value = ["/content"])
class ContentController {
    @Autowired
    lateinit var repositoryImpl: ContentRepositoryImpl

    @GetMapping(value = ["/{id}"])
    fun getContentDetail(@RequestHeader("Authorization") token: String, @PathVariable id: String) =
        repositoryImpl.getMovieDetail(token, id)

    @PostMapping()
    fun changeSeenStatus(
        @RequestHeader("Authorization") token: String,
        @RequestBody body: ChangeSeenStatusRequestBody
    ) =
        repositoryImpl.changeContentSeenStatus(token, body.id, body.status)
}