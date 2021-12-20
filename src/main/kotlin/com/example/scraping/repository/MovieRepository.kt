package com.example.scraping.repository

import com.example.scraping.repository.mappers.statsToInt
import com.example.scraping.repository.model.Movie
import com.example.scraping.repository.model.BASE_URL
import org.jsoup.Jsoup

class MovieRepository {
    fun getPopularMovies(page: Int): List<Movie> {
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

        return movieList
    }

    fun getAvailableMovies(): List<Movie> = getMovieList("filmes-nos-cinemas")

    fun getMoviesComingSoon(): List<Movie> = getMovieList("filmes-em-breve")

    fun getMoviesWeekPremiere(): List<Movie> = getMovieList("filmes-estreias")

    private fun getMovieList(page: String): List<Movie> {
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

        return movieList
    }
}

