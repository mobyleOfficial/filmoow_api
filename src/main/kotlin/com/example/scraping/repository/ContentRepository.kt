package com.example.scraping.repository

import com.example.scraping.repository.model.*
import com.example.scraping.repository.model.content_detail.ContentDetail
import com.example.scraping.repository.model.content_detail.RecommendedContent
import org.jsoup.Jsoup
import org.springframework.http.*
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

class ContentRepository {
    fun getMovieDetail(id: String): ResponseEntity<Any> {
        val coverList = mutableListOf<String>()
        val directorList = mutableListOf<Director>()
        val actorList = mutableListOf<Actor>()
        val recommendedMovieList = mutableListOf<RecommendedContent>()

        Thread.sleep(2_000)

        val webPageMovie = Jsoup.connect("${BASE_URL}/$id")
            .cookie(
                "filmow_sessionid",
                ".eJxVy0EOwiAQQNG7sDYEcGgZly68BqEzTGjUNpESF8a7t5oudP3-f6mY2lJiq_kRS6pFnZQQQhcCWGGT0Xt2ho5mCD1TZ8FxQGFB8OrwO4-8rQ5s6BH_ZUh0zdOHZbzd56dORHOblqp3qfryhfMevlcI3zGW:1mw25k:pQyIaZchZU7SZZTMxn0X6v3bJ0c"
            )
            .header("Cache-control", "no-cache")
            .header("Cache-store", "no-store")
            .get()

        try {
            val coverImages = webPageMovie.getElementsByClass("carousel-inner page-avatars")
            val mainCover = coverImages
                .first()
                .getElementsByClass("item active")
                .first()
                .getElementsByTag("a")
                .attr("href")

            coverList.add(mainCover)

            val secondaryCovers = coverImages.first().getElementsByClass("item")
            repeat(secondaryCovers.size) {
                val imageUrl = secondaryCovers[it]
                    .getElementsByTag("a")
                    .attr("href")

                coverList.add(imageUrl)
            }

            coverList.removeLast()
        } catch (error: Exception) {
            coverList.add("")
        }

        try {
            val directorsListItem = webPageMovie
                .getElementsByClass("directors")
                .first()
                .getElementsByTag("a")

            repeat(directorsListItem.size) {
                val name = directorsListItem[it].text()
                val directorId = directorsListItem[it].getElementsByTag("a").attr("href")

                directorList.add(Director(directorId, name))
            }
        } catch (exception: Exception) {

        }

        val description = webPageMovie
            .getElementsByClass("description text-truncate")
            .first()
            .getElementsByTag("p")
            .text()

        val title = webPageMovie
            .getElementsByClass("movie-title")
            .first()
            .getElementsByTag("h1")
            .text()

        val releaseYear = webPageMovie
            .getElementsByClass("movie-title")
            .first()
            .getElementsByClass("release")
            .text()
            .toInt()

        val originalTitle = webPageMovie
            .getElementsByClass("movie-other-titles-wrapper")
            .first()
            .getElementsByTag("h2")
            .text()

        val generalScore = try {
            webPageMovie
                .getElementsByClass("movie-rating-average clearfix")
                .first()
                .getElementsByClass("average")
                .text()
        } catch (error: Exception) {
            null
        }

        val scoreQuantity = try {
            webPageMovie
                .getElementsByClass("summary")
                .first()
                .getElementsByTag("span")
                .text()
        } catch (exception: Exception) {
            null
        }

        val userScore = try {
            webPageMovie
                .getElementsByClass("star-rating-container")
                .first()
                .getElementsByClass("star-rating")
                .first()
                .attr("data-average")
        } catch (error: Exception) {
            null
        }

        val genreList = webPageMovie
            .getElementsByClass("btn-tags-list")
            .first()
            .getElementsByClass("btn-tags-list")
            .text().split(" ")

        val duration = try {
            webPageMovie
                .getElementsByClass("running_time")
                .first()
                .text()
        } catch (exception: Exception) {
            "none"
        }

        val classification = try {
            webPageMovie
                .getElementsByClass("movie-classification")
                .first()
                .text()
        } catch (exception: Exception) {
            "none"
        }

        try {
            val actorsListItem = webPageMovie
                .getElementsByClass("sidebar-section")
                .first {
                    it.className() == "sidebar-section"
                }
                .getElementById("casting-section")
                .getElementsByTag("li")

            addActorsToList(actorList, actorsListItem)
        } catch (error: Exception) {
            try {
                val actorsListItem = webPageMovie
                    .getElementsByClass("sidebar-section")
                    .filter {
                        it.className() == "sidebar-section"
                    }[1]
                    .getElementById("casting-section")
                    .getElementsByTag("li")

                addActorsToList(actorList, actorsListItem)
            } catch (exception: Exception) {
                print(exception.message)
            }
        }

        try {
            val recommendedMovieItems = webPageMovie
                .getElementsByClass("page-section-dark")
                .first()
                .getElementsByTag("li")

            repeat(recommendedMovieItems.size) {
                val item = recommendedMovieItems[it]
                val name = item.getElementsByClass("cover tip-movie")
                    .first()
                    .getElementsByTag("a")
                    .first()
                    .attr("title")

                val recommendedContentId = item.getElementsByClass("cover tip-movie")
                    .first()
                    .getElementsByTag("a")
                    .first()
                    .attr("href")

                val recommendedContentImgUrl = item.getElementsByClass("cover tip-movie")
                    .first()
                    .getElementsByTag("img")
                    .first()
                    .attr("src")

                recommendedMovieList.add(RecommendedContent(recommendedContentId, recommendedContentImgUrl, name))
            }

        } catch (exception: Exception) {
            print(exception.toString())
        }

        val seenStatus: SeenStatus = try {
            val wantToSeeStatus = webPageMovie.getElementsByClass("btns-see")
                .first()
                .getElementsByTag("button")[2]
                .className()

            val statusName = webPageMovie.getElementsByClass("btn-group")
                .first()
                .getElementsByTag("button")
                .first()
                .className()

            when {
                wantToSeeStatus == "btn btn-seen ws active" -> {
                    SeenStatus.WantToSee
                }
                statusName == "btn btn-seen btn-seen-main sn active" -> {
                    SeenStatus.Seen
                }
                else -> {
                    SeenStatus.NotSeen
                }
            }
        } catch (exception: Exception) {
            SeenStatus.NotSeen
        }

        if (title.isNullOrEmpty()) {
            return ResponseEntity(
                ErrorMessage("Not found", "Content detail not found"),
                HttpStatus.NOT_FOUND
            )
        }

        return ResponseEntity(
            ContentDetail(
                title,
                originalTitle,
                description,
                generalScore?.toDouble(),
                userScore?.toDouble(),
                scoreQuantity?.toInt(),
                classification,
                duration,
                releaseYear,
                seenStatus,
                genreList,
                coverList,
                actorList,
                recommendedMovieList,
            ),
            HttpStatus.OK
        )
    }

    private fun addActorsToList(actorList: MutableList<Actor>, actorListItem: List<org.jsoup.nodes.Element>) {
        repeat(actorListItem.size) {
            val item = actorListItem[it]
            val actorName = item.text()
            val actorId = item.getElementsByTag("a").first().attr("href")
            val actorPhotoUrl = item.getElementsByTag("img").first().attr("src")
            actorList.add(Actor(actorId, actorName, actorPhotoUrl))
        }
    }

    fun changeContentSeenStatus(id: String, status: String): ResponseEntity<Any> {
        try {
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
            headers.add(
                "Cookie",
                "filmow_sessionid=.eJxVy0EOwiAQQNG7sDYEcGgZly68BqEzTGjUNpESF8a7t5oudP3-f6mY2lJiq_kRS6pFnZQQQhcCWGGT0Xt2ho5mCD1TZ8FxQGFB8OrwO4-8rQ5s6BH_ZUh0zdOHZbzd56dORHOblqp3qfryhfMevlcI3zGW:1mw25k:pQyIaZchZU7SZZTMxn0X6v3bJ0c"
            )

            val restTemplate = RestTemplate()
            restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())

            val map: MultiValueMap<String, String> = LinkedMultiValueMap()
            map.add("movie_pk", id)
            map.add("status", status)
            map.add("times", "1")

            val entity: HttpEntity<MultiValueMap<String, String>> =
                HttpEntity<MultiValueMap<String, String>>(map, headers)

            val response = restTemplate.exchange(
                "$BASE_URL/marcar/filme/",
                HttpMethod.POST,
                entity,
                String::class.java,
            )
            val responseText = response.headers["set-cookie"]?.get(0) ?: ""
            val isReturnStatusNotSeen = responseText.contains("Removido com sucesso")

            val seenStatus = if (isReturnStatusNotSeen) {
                SeenStatus.NotSeen
            } else if (!isReturnStatusNotSeen && status == "WS") {
                SeenStatus.WantToSee
            } else {
                SeenStatus.Seen
            }

            return ResponseEntity(
                seenStatus,
                HttpStatus.OK
            )
        } catch (exception: Exception) {
            return ResponseEntity(
                ErrorMessage("BAD REQUEST", "Could not change status"),
                HttpStatus.BAD_REQUEST
            )
        }
    }
}

