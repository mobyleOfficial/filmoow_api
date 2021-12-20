package com.example.scraping.repository

import com.example.scraping.repository.mappers.statsToInt
import com.example.scraping.repository.model.BASE_URL
import com.example.scraping.repository.model.TvShow
import org.jsoup.Jsoup
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class TvShowRepository {
    fun getPopularTvShow(page: Int): ResponseEntity<List<TvShow>> {
        val tvShow = mutableListOf<TvShow>()
        val webPageMovieList = Jsoup.connect("${BASE_URL}/populares/tv/?pagina=$page")
            .get()
            .select("li.movie_list_item")

        repeat(webPageMovieList.size) {
            val tvShowListItem = webPageMovieList[it]
            val id = tvShowListItem.getElementsByClass("cover tip-movie ")
                .first()
                .attr("href")
            val title = webPageMovieList[it].getElementsByTag("a").first().attr("title")
            val image = webPageMovieList[it].getElementsByTag("img").first().attr("data-src")

            val seriesScoreClass = tvShowListItem.getElementsByClass("movie-rating-average")
            val score = if (seriesScoreClass.size == 0) null else seriesScoreClass.first().text().toDouble()

            val commentsQuantityClass = tvShowListItem.getElementsByTag("span").first { tag ->
                tag.className() == "badge badge-num-comments tip"
            }
            val originalTitle = commentsQuantityClass.attr("title")
            val commentQuantity = if (!originalTitle.isNullOrEmpty())
                originalTitle.split(" ").first().statsToInt()
            else
                commentsQuantityClass.text().statsToInt()

            tvShow.add(TvShow(id, title, image, score, commentQuantity))
        }

        if(tvShow.isEmpty()) {
            return ResponseEntity(
                tvShow,
                HttpStatus.NOT_FOUND
            )
        }

        return ResponseEntity(
            tvShow,
            HttpStatus.OK
        )
    }
}