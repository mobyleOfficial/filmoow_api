package com.example.scraping.repository.movie

import com.example.scraping.repository.mappers.statsToInt
import com.example.scraping.repository.model.BASE_URL
import com.example.scraping.repository.model.Movie
import org.jsoup.Jsoup
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class MovieRepositoryImpl : MovieRepository {
    override fun getPopularMovies(page: Int): ResponseEntity<List<Movie>> {
        val movieList = mutableListOf<Movie>()
        val webPageMovieList = Jsoup.connect("${BASE_URL}/populares/filmes/?page=$page")
            .get()
            .select("li.movie_list_item")

        repeat(webPageMovieList.size) {
            val movieListItem = webPageMovieList[it]
            val id = movieListItem.getElementsByClass("cover tip-movie ")
                .first()
                .attr("href")
            val title = movieListItem.getElementsByTag("a").first().attr("title")
            val image = movieListItem.getElementsByTag("img").first().attr("data-src")
            val movieScoreClass = movieListItem.getElementsByClass("movie-rating-average")
            val score = if (movieScoreClass.size == 0) null else movieScoreClass.first().text().toDouble()
            val commentsQuantityClass = movieListItem.getElementsByClass("badge badge-num-comments tip")
            val commentsQuantity =
                if (commentsQuantityClass.size == 0) null else commentsQuantityClass.first().text().statsToInt()

            movieList.add(Movie(id, title, image, score, commentsQuantity))
        }

        if (movieList.isEmpty()) {
            return ResponseEntity(
                movieList,
                HttpStatus.NOT_FOUND
            )
        }

        return ResponseEntity(
            movieList,
            HttpStatus.OK
        )
    }

    override fun getAvailableMovies(): ResponseEntity<List<Movie>> = getMovieList("filmes-nos-cinemas")

    override fun getMoviesComingSoon(): ResponseEntity<List<Movie>> = getMovieList("filmes-em-breve")

    override fun getMoviesWeekPremiere(): ResponseEntity<List<Movie>> = getMovieList("filmes-estreias")

    private fun getMovieList(page: String): ResponseEntity<List<Movie>> {
        val movieList = mutableListOf<Movie>()
        val webPageMovieList = Jsoup.connect("${BASE_URL}/$page")
            .get()
            .select("li.movie_list_item")

        repeat(webPageMovieList.size) {
            val movieListItem = webPageMovieList[it]
            val id = movieListItem.getElementsByClass("cover tip-movie ")
                .first()
                .attr("href")
            val title = movieListItem.getElementsByTag("a").first().attr("title")
            val image = movieListItem.getElementsByTag("img").first().attr("data-src")
            val movieScoreClass = movieListItem.getElementsByClass("movie-rating-average")
            val score = if (movieScoreClass.size == 0) null else movieScoreClass.first().text().toDouble()
            val commentsQuantityClass = movieListItem.getElementsByClass("badge badge-num-comments tip")
            val commentsQuantity =
                if (commentsQuantityClass.size == 0) null else commentsQuantityClass.first().text().statsToInt()

            movieList.add(Movie(id, title, image, score, commentsQuantity))
        }

        if (movieList.isEmpty()) {
            return ResponseEntity(
                movieList,
                HttpStatus.NOT_FOUND
            )
        }

        return ResponseEntity(
            movieList,
            HttpStatus.OK
        )
    }
}

