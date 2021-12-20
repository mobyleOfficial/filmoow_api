package com.example.scraping.controller

import com.example.scraping.repository.ContentRepository
import com.example.scraping.repository.model.content_detail.ChangeSeenStatusRequestBody
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping(value = ["/content"])
class ContentController {
    val repository: ContentRepository = ContentRepository()

    @GetMapping(value = ["/{id}"])
    fun getContentDetail(@PathVariable id: String) = repository.getMovieDetail(id)

    @PostMapping()
    fun changeSeenStatus(@RequestBody body: ChangeSeenStatusRequestBody) =
        repository.changeContentSeenStatus(body.id, body.status)
}