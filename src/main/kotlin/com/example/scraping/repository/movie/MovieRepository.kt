package com.example.scraping.repository.movie

import com.example.scraping.repository.movie.model.Movie
import org.springframework.http.ResponseEntity

interface MovieRepository {
    fun getPopularMovies(page: Int, token: String): ResponseEntity<List<Movie>>

    fun getAvailableMovies(token: String): ResponseEntity<List<Movie>>

    fun getMoviesComingSoon(token: String): ResponseEntity<List<Movie>>

    fun getMoviesWeekPremiere(token: String): ResponseEntity<List<Movie>>
}