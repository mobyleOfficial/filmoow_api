package com.example.scraping.repository.movie

import com.example.scraping.repository.movie.model.Movie
import org.springframework.http.ResponseEntity

interface MovieRepository {
    fun getPopularMovies(page: Int): ResponseEntity<List<Movie>>

    fun getAvailableMovies(): ResponseEntity<List<Movie>>

    fun getMoviesComingSoon(): ResponseEntity<List<Movie>>

    fun getMoviesWeekPremiere(): ResponseEntity<List<Movie>>
}