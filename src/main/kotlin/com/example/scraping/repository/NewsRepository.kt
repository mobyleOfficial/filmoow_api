package com.example.scraping.repository

import com.example.scraping.repository.model.BASE_URL
import com.example.scraping.repository.model.news.News
import com.example.scraping.repository.model.news.NewsStats
import org.jsoup.Jsoup

class NewsRepository {
    fun getLastNews(page: Int): List<News> {
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

        return newsList
    }
}