package com.example.scraping.repository.content

import com.example.scraping.repository.common.BASE_URL
import com.example.scraping.repository.common.ErrorMessage
import com.example.scraping.repository.common.model.SeenStatus
import com.example.scraping.repository.common.model.User
import com.example.scraping.repository.common.model.comments.Comment
import com.example.scraping.repository.common.model.comments.CommentListing
import com.example.scraping.repository.common.model.comments.CommentStats
import com.example.scraping.repository.content.model.Actor
import com.example.scraping.repository.content.model.ContentDetail
import com.example.scraping.repository.content.model.Director
import com.example.scraping.repository.content.model.RecommendedContent
import com.fasterxml.jackson.databind.JsonNode
import org.jsoup.Jsoup
import org.springframework.http.*
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Component
class ContentRepositoryImpl : ContentRepository {

    override fun getContentDetail(token: String, id: String): ResponseEntity<Any> {
        val coverList = mutableListOf<String>()
        val directorList = mutableListOf<Director>()
        val actorList = mutableListOf<Actor>()
        val recommendedMovieList = mutableListOf<RecommendedContent>()

        val webPageMovie = Jsoup.connect("${BASE_URL}/$id")
            .header("Cache-Control", "max-age=0")
            .cookie(
                "filmow_sessionid",
                token
            )
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

    override fun changeContentSeenStatus(token: String, id: String, status: String): ResponseEntity<Any> {
        try {
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
            headers.add(
                "Cookie",
                "filmow_sessionid=$token"
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

    override fun getContentComments(id: String, page: Int, token: String): ResponseEntity<Any> {
        try {
            val commentList = mutableListOf<Comment>()

            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
            headers.add(
                "Cookie",
                "filmow_sessionid=$token"
            )
            headers.add("cache-control", "max-age=0")
            headers.add("content-Type", "application/json")

            val restTemplate = RestTemplate()
            restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())

            val entity: HttpEntity<MultiValueMap<String, String>> =
                HttpEntity<MultiValueMap<String, String>>(headers)

            val response = restTemplate.exchange(
                "$BASE_URL/async/comments/?content_type=22&object_pk=$id&user=all&order_by=-created&page=$page",
                HttpMethod.GET,
                entity,
                JsonNode::class.java
            ).body

            val hasNext = response?.get("pagination")?.get("has_next")?.asText() == "true"
            val responseBodyHtml = response?.get("html")?.asText()
            val commentPage = Jsoup.parse(responseBodyHtml)

            if (commentPage.text().contains("Seja o primeiro a comentar")) {
                return ResponseEntity(
                    CommentListing(
                        hasNext,
                        commentList
                    ),
                    HttpStatus.OK
                )
            }

            val commentsList = commentPage.getElementsByTag("li")

            repeat(commentsList.size) {
                val commentComponent = commentsList[it]
                val commentId = commentComponent.getElementsByClass("age").first().attr("href")
                val userSection = commentComponent.getElementsByClass("user-name tip-user").first()
                val commentSection = commentComponent.getElementsByClass("text comment-text")
                val creationTime = commentComponent.getElementsByClass("age").first().text()
                val spoilerList = commentSection.first().getElementsByClass("spoiler")
                    ?.map { spoiler ->
                        spoiler.text()
                    }
                val fullText =
                    commentSection
                        .first()
                        .text()
                        .replace("Comentário contando partes do filme. Mostrar.", "")
                        .replace("[spoiler]", "")
                        .replace("[/spoiler]", "")
                val userId = userSection.attr("href")
                val userName = userSection.text()
                val photoUrl = commentComponent
                    .getElementsByClass("avatar tip-user")
                    ?.first()
                    ?.getElementsByTag("img")
                    ?.attr("src") ?: ""
                val likesQuantity = commentComponent.getElementsByTag("button")
                    ?.first { element ->
                        element.attr("name") == "like_count"
                    }?.text()?.toInt() ?: 0

                val rating =
                    commentComponent.getElementsByClass("tip star-rating star-rating-small stars")
                        ?.first()
                        ?.attr("title")
                        ?.replace("Nota: ", "")
                        ?.replace(" estrelas", "")
                        ?.replace(" estrela", "")
                        ?.toDouble()

                val hasDislike =
                    commentComponent.getElementsByClass("btn btn-link btn-dislike btn-like-active").size == 1

                val repliesComponent = commentComponent
                    .getElementsByClass("comments-replies")
                    ?.first()
                    ?.getElementsByTag("a")
                    ?.text()
                    ?.split(" ")
                    ?.first()

                val repliesQuantity = if (repliesComponent.isNullOrEmpty()) 0 else
                    repliesComponent.toInt()

                val hasUserLike =
                    commentComponent.getElementsByClass("media-footer")
                        .first()
                        .getElementsByClass("like-tag")
                        .first()
                        .getElementsByTag("button")
                        .first()
                        .attr("class") == "btn btn-link btn-like btn-like-active tip-who-liked"

                commentList.add(
                    Comment(
                        commentId, User(userId, userName, photoUrl), creationTime, fullText, spoilerList,
                        CommentStats(rating, likesQuantity, repliesQuantity, hasDislike, hasUserLike)
                    )
                )
            }

            return ResponseEntity(
                CommentListing(
                    hasNext,
                    commentList
                ),
                HttpStatus.OK
            )
        } catch (exception: Exception) {
            return ResponseEntity(
                ErrorMessage("BAD REQUEST", "Could not request list"),
                HttpStatus.BAD_REQUEST
            )
        }
    }
}

