package com.example.scraping.controller

import com.example.scraping.repository.content.ContentRepository
import com.example.scraping.repository.content.ContentRepositoryImpl
import com.example.scraping.repository.content.model.ChangeSeenStatusRequestBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping(value = ["/content"])
class ContentController {
    @Autowired
    lateinit var repository: ContentRepository

    @GetMapping(value = ["/{id}"])
    fun getContentDetail(@RequestHeader("Authorization") token: String, @PathVariable id: String) =
        repository.getContentDetail(token, id)

    @PostMapping()
    fun changeSeenStatus(
        @RequestHeader("Authorization") token: String,
        @RequestBody body: ChangeSeenStatusRequestBody
    ) =
        repository.changeContentSeenStatus(token, body.id, body.status)

    @GetMapping(value = ["/comments/{id}"])
    fun getComments(@RequestHeader("Authorization") token: String, @PathVariable id: String, @RequestParam page: Int) =
        repository.getContentComments(id, page, token)
}