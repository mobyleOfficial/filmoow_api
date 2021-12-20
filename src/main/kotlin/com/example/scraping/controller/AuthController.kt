package com.example.scraping.controller

import com.example.scraping.repository.model.BASE_URL
import org.jsoup.Jsoup
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/auth"])
class AuthController {
    @GetMapping
    fun getMessages() {
        val webPage = Jsoup.connect("$BASE_URL/mensagens")
            .cookie("filmow_sessionid", ".eJxVy0EOwiAQQNG7sDYEcGgZly68BqEzTGjUNpESF8a7t5oudP3-f6mY2lJiq_kRS6pFnZQQQhcCWGGT0Xt2ho5mCD1TZ8FxQGFB8OrwO4-8rQ5s6BH_ZUh0zdOHZbzd56dORHOblqp3qfryhfMevlcI3zGW:1mw25k:pQyIaZchZU7SZZTMxn0X6v3bJ0c")
            .get()

        print(webPage)
    }
}