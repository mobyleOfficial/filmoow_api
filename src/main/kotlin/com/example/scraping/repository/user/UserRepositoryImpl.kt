package com.example.scraping.repository.user

import com.example.scraping.repository.common.BASE_URL
import com.example.scraping.repository.common.ErrorMessage
import com.example.scraping.repository.user.model.UserInformation
import org.jsoup.Jsoup
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class UserRepositoryImpl : UserRepository {
    override fun getUserInformation(token: String): ResponseEntity<Any> {
        val webPageMovie = Jsoup.connect("$BASE_URL/usuarios/painel")
            .header("Cache-Control", "max-age=0")
            .cookie(
                "filmow_sessionid",
                token
            )
            .get()

        val scripts = webPageMovie.getElementsByTag("script")

        repeat(scripts.size) { i ->
            val script = scripts[i]
            val dataNodes = script.dataNodes()

            repeat(dataNodes.size) { j ->
                if(dataNodes[j].wholeData.contains("user_is_authenticated = false")){
                    return ResponseEntity(
                        ErrorMessage("Unauthorized", "User should be logged"),
                        HttpStatus.UNAUTHORIZED,
                    )
                }
            }
        }

        val name = webPageMovie
            .getElementsByClass("about-user")
            .first()
            .getElementsByClass("page-title")
            .first()
            .text()

        val userName = webPageMovie
            .getElementsByClass("dropdown not-mobile")
            .first()
            .getElementsByClass("dropdown-toggle")
            .first()
            .text()

        val imageUrl = webPageMovie
            .getElementsByClass("carousel-inner page-avatars")
            .first()
            .getElementsByClass("avatar")
            .first()
            .getElementsByTag("a")
            .attr("href")

        val stats = webPageMovie
            .getElementsByClass("cover-stats")
            .first()
            .getElementsByTag("a")

        val seenNumber = stats[0]
            .getElementsByClass("number")
            .first()
            .text()
            .toInt()

        val commentNumber = stats[1]
            .getElementsByClass("number")
            .first()
            .text()
            .toInt()

        val listNumber = stats[2]
            .getElementsByClass("number")
            .first()
            .text()
            .toInt()

        val timeSpent = webPageMovie
            .getElementsByClass("cover-stats")
            .first()
            .getElementsByClass("desc")
            .first()
            .getElementsByTag("b")
            .text()

        return ResponseEntity(
            UserInformation(
                name, userName, imageUrl,
                seenNumber, commentNumber, listNumber, timeSpent
            ),
            HttpStatus.OK,
        )
    }
}