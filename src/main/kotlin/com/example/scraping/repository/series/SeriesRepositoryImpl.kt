package com.example.scraping.repository.series

import com.example.scraping.repository.mappers.statsToInt
import com.example.scraping.repository.common.BASE_URL
import com.example.scraping.repository.series.model.Series
import org.jsoup.Jsoup
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class SeriesRepositoryImpl : SeriesRepository {
    override fun getPopularSeries(page: Int): ResponseEntity<List<Series>> {
        val seriesList = mutableListOf<Series>()
        val webPageMovieList = Jsoup.connect("${BASE_URL}/populares/series/?pagina=$page")
            .get()
            .select("li.movie_list_item")

        repeat(webPageMovieList.size) {
            val seriesListItem = webPageMovieList[it]
            val id = seriesListItem.getElementsByClass("cover tip-movie ")
                .first()
                .attr("href")
            val title = seriesListItem.getElementsByTag("a").first().attr("title")
            val image = seriesListItem.getElementsByTag("img").first().attr("data-src")

            val seriesScoreClass = seriesListItem.getElementsByClass("movie-rating-average")
            val score = if (seriesScoreClass.size == 0) null else seriesScoreClass.first().text().toDouble()

            val commentsQuantityClass = seriesListItem.getElementsByTag("span").first { tag ->
                tag.className() == "badge badge-num-comments tip"
            }
            val originalTitle = commentsQuantityClass.attr("title")
            val commentQuantity = if (!originalTitle.isNullOrEmpty())
                originalTitle.split(" ").first().statsToInt()
            else
                commentsQuantityClass.text().statsToInt()

            seriesList.add(Series(id, title, image, score, commentQuantity))
        }

        if (seriesList.isEmpty()) {
            return ResponseEntity(
                seriesList,
                HttpStatus.NOT_FOUND
            )
        }

        return ResponseEntity(
            seriesList,
            HttpStatus.OK
        )
    }
}