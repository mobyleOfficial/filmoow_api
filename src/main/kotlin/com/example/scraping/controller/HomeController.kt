package com.example.scraping.controller

import com.example.scraping.repository.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping(value = ["/home"])
class HomeController {
    val movieRepository: MovieRepository = MovieRepository()

    val seriesRepository: SeriesRepository = SeriesRepository()

    val tvShowRepository: TvShowRepository = TvShowRepository()

    val listRepository: ListsRepository = ListsRepository()

    val newsRepository: NewsRepository = NewsRepository()

    @GetMapping(value = ["/popularMovies"])
    fun getPopularMovies() = movieRepository.getPopularMovies(1)

    @GetMapping(value = ["/popularSeries"])
    fun getPopularSeries() = seriesRepository.getPopularSeries(1)

    @GetMapping(value = ["/popularTvShow"])
    fun getPopularTvShow() = tvShowRepository.getPopularTvShow(1)

    @GetMapping(value = ["/availableCinemaMovies"])
    fun getAvailableMovies() = movieRepository.getAvailableMovies()

    @GetMapping(value = ["/comingSoonMovies"])
    fun getMoviesComingSoon() = movieRepository.getMoviesComingSoon()

    @GetMapping(value = ["/moviesWeekPremiere"])
    fun getMoviesWeekPremiere() = movieRepository.getMoviesWeekPremiere()

    @GetMapping(value = ["/popularLists"])
    fun getPopularLists() = listRepository.getWeekPopularList()

    @GetMapping(value = ["/news"])
    fun getNews() = newsRepository.getLastNews(1)
}