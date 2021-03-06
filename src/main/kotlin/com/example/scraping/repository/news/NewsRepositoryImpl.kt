package com.example.scraping.repository.news

import com.example.scraping.repository.common.BASE_URL
import com.example.scraping.repository.news.model.News
import com.example.scraping.repository.news.model.NewsStats
import org.jsoup.Jsoup
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class NewsRepositoryImpl : NewsRepository {
    override fun getLastNews(page: Int): ResponseEntity<List<News>> {
        val newsList = mutableListOf<News>()
        val webPage = Jsoup.connect("$BASE_URL/noticias/?pagina=$page")
            .get()

        val newsUl = webPage
            .getElementsByClass("articles-list")
            .first()
            .getElementsByClass("article-list-item")

        repeat(newsUl.size) {
            val newsDiv = newsUl[it]
            val splittedId = newsDiv.getElementsByTag("a")
                .attr("href")
                .split("/")

            val title = newsDiv.getElementsByTag("a")
                .attr("title")

            val coverImage = newsDiv.getElementsByTag("img")
                .map { element ->
                    element.attr("src")
                }.first()

            val footer = newsDiv
                .getElementsByClass("article-footer")
                .first()

            val likes = footer
                .getElementsByClass("btn-like")
                .select("span.count")
                .text()
                .toInt()

            val comments = footer
                .select("a.num-comments")
                .text()
                .toInt()

            val date = footer.select("span.age")
                .text()

            newsList.add(
                News(
                    splittedId[2] + "/" + splittedId[3],
                    title,
                    date,
                    coverImage,
                    NewsStats(likes, comments)
                )
            )
        }

        if (newsList.isEmpty()) {
            return ResponseEntity(
                newsList,
                HttpStatus.NOT_FOUND
            )
        }

        return ResponseEntity(
            newsList,
            HttpStatus.OK
        )
    }
}