package com.example.scraping.repository

import com.example.scraping.repository.mappers.statsToInt
import com.example.scraping.repository.model.BASE_URL
import com.example.scraping.repository.model.user_listing.ListStats
import com.example.scraping.repository.model.user_listing.UserListing
import org.jsoup.Jsoup
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ListsRepository {
    fun getPopularLists(page: Int): ResponseEntity<List<UserListing>> {
        val userListsList = mutableListOf<UserListing>()
        val webPage = Jsoup.connect("$BASE_URL/listas-populares/?pagina=$page")
            .get()

        val listDivs = webPage.getElementsByClass("lists_list")
            .first()
            .getElementsByClass("lists_list_item")

        repeat(listDivs.size) {
            val div = listDivs[it]
            val id = div.getElementsByTag("a")
                .first()
                .attr("href")
            val coverImage = div.getElementsByTag("img")
                .map { element ->
                    element.attr("src")
                }
            val title = div.getElementsByClass("list_title").text()
            val description = div.getElementsByClass("list_description").text()
            val owner = div.getElementsByClass("tip-user user").text()
            val stats = div.getElementsByClass("list_stats").text().split(" ")

            userListsList.add(
                UserListing(
                    id,
                    title,
                    description,
                    owner,
                    ListStats(
                        stats[0].statsToInt(),
                        stats[1].statsToInt(),
                        stats[2].statsToInt(),
                    ),
                    coverImage
                )
            )
        }

        if(userListsList.isEmpty()) {
            return ResponseEntity(
                userListsList,
                HttpStatus.NOT_FOUND
            )
        }

        return ResponseEntity(
            userListsList,
            HttpStatus.OK
        )
    }

    fun getWeekPopularList(): ResponseEntity<List<UserListing>> {
        val userListsList = mutableListOf<UserListing>()
        val webPage = Jsoup.connect("https://filmow.com")
            .get()

        val listDivs = webPage.getElementsByClass("page-section-dark")[1]
            .getElementById("lists_list")
            .getElementsByClass("lists_list_vertical")
            .first()
            .getElementsByClass("lists_list_item_vertical")

        repeat(listDivs.size) {
            val div = listDivs[it]
            val id = div.getElementsByTag("a")
                .first()
                .attr("href")

            val coverImage = div.getElementsByTag("img")
                .map { element ->
                    element.attr("src")
                }
                .subList(0, div.getElementsByTag("img").size - 1)
                .reversed()

            var title = div.getElementsByClass("list_details")
                .first()
                .getElementsByClass("list_title")
                .first()
                .getElementsByTag("a")
                .attr("title")

            if (title.isNullOrEmpty()) {
                title = div.getElementsByClass("list_title").text()
            }

            val stats = div.getElementsByClass("list_stats").text().split(" ")

            userListsList.add(
                UserListing(
                    id,
                    title,
                    null,
                    null,
                    ListStats(
                        stats[stats.size - 3].statsToInt(),
                        stats[stats.size - 2].statsToInt(),
                        stats[stats.size - 1].statsToInt(),
                    ),
                    coverImage
                )
            )
        }

        if(userListsList.isEmpty()) {
            return ResponseEntity(
                userListsList,
                HttpStatus.NOT_FOUND
            )
        }

        return ResponseEntity(
            userListsList,
            HttpStatus.OK
        )
    }
}