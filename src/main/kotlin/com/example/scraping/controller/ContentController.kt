package com.example.scraping.controller

import com.example.scraping.repository.content.ContentRepositoryImpl
import com.example.scraping.repository.model.content_detail.ChangeSeenStatusRequestBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping(value = ["/content"])
class ContentController {
    @Autowired
    lateinit var repositoryImpl: ContentRepositoryImpl

    @GetMapping(value = ["/{id}"])
    fun getContentDetail(@PathVariable id: String) = repositoryImpl.getMovieDetail(id)

    @PostMapping()
    fun changeSeenStatus(@RequestBody body: ChangeSeenStatusRequestBody) =
        repositoryImpl.changeContentSeenStatus(body.id, body.status)
}