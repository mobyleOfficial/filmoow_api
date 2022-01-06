package com.example.scraping.repository.movie

import com.example.scraping.repository.mappers.statsToInt
import com.example.scraping.repository.common.BASE_URL
import com.example.scraping.repository.common.model.SeenStatus
import com.example.scraping.repository.movie.model.Movie
import org.jsoup.Jsoup
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class MovieRepositoryImpl : MovieRepository {
    override fun getPopularMovies(page: Int, token: String): ResponseEntity<List<Movie>> {
        val movieList = mutableListOf<Movie>()
        val webPageMovieList = Jsoup.connect("${BASE_URL}/populares/filmes/?page=$page")
            .header("Cache-Control", "max-age=0")
            .cookie(
                "filmow_sessionid",
                token
            )
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

            val seenStatus = try {
                val status = movieListItem.getElementsByClass("cover tip-movie ")
                    ?.first()
                    ?.getElementsByTag("span")
                    ?.first()
                    ?.text()

                when (status) {
                    "Quero Ver" -> {
                        SeenStatus.WantToSee
                    }
                    "Já Vi" -> {
                        SeenStatus.Seen
                    }
                    else -> {
                        SeenStatus.NotSeen
                    }
                }

            } catch (exception: Exception) {
                SeenStatus.NotSeen
            }

            movieList.add(Movie(id, title, image, score, commentsQuantity, seenStatus))
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

    override fun getAvailableMovies(token: String): ResponseEntity<List<Movie>> = getMovieList("filmes-nos-cinemas", token)

    override fun getMoviesComingSoon(token: String): ResponseEntity<List<Movie>> = getMovieList("filmes-em-breve", token)

    override fun getMoviesWeekPremiere(token: String): ResponseEntity<List<Movie>> = getMovieList("filmes-estreias", token)

    private fun getMovieList(page: String, token: String): ResponseEntity<List<Movie>> {
        val movieList = mutableListOf<Movie>()
        val webPageMovieList = Jsoup.connect("${BASE_URL}/$page")
            .header("Cache-Control", "max-age=0")
            .cookie(
                "filmow_sessionid",
                token
            )
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

            val seenStatus = try {
                val status = movieListItem.getElementsByClass("cover tip-movie ")
                    ?.first()
                    ?.getElementsByClass("legend sn")
                    ?.text()

                when (status) {
                    "Quero Ver" -> {
                        SeenStatus.WantToSee
                    }
                    "Já Vi" -> {
                        SeenStatus.Seen
                    }
                    else -> {
                        SeenStatus.NotSeen
                    }
                }

            } catch (exception: Exception) {
                SeenStatus.NotSeen
            }

            movieList.add(Movie(id, title, image, score, commentsQuantity, seenStatus))
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

