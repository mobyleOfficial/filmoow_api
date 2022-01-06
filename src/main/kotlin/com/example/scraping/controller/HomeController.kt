package com.example.scraping.controller

import com.example.scraping.repository.list.ListRepository
import com.example.scraping.repository.movie.MovieRepository
import com.example.scraping.repository.news.NewsRepository
import com.example.scraping.repository.series.SeriesRepository
import com.example.scraping.repository.tvShow.TvShowRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping(value = ["/home"])
class HomeController {
    @Autowired
    lateinit var movieRepository: MovieRepository

    @Autowired
    lateinit var  seriesRepository: SeriesRepository

    @Autowired
    lateinit var  tvShowRepository: TvShowRepository

    @Autowired
    lateinit var  listRepository: ListRepository

    @Autowired
    lateinit var  newsRepository: NewsRepository

    @GetMapping(value = ["/popularMovies"])
    fun getPopularMovies(@RequestHeader("Authorization") token: String) = movieRepository.getPopularMovies(1, token)

    @GetMapping(value = ["/popularSeries"])
    fun getPopularSeries(@RequestHeader("Authorization") token: String) = seriesRepository.getPopularSeries(1)

    @GetMapping(value = ["/popularTvShow"])
    fun getPopularTvShow(@RequestHeader("Authorization") token: String) = tvShowRepository.getPopularTvShow(1)

    @GetMapping(value = ["/availableCinemaMovies"])
    fun getAvailableMovies(@RequestHeader("Authorization") token: String) = movieRepository.getAvailableMovies(token)

    @GetMapping(value = ["/comingSoonMovies"])
    fun getMoviesComingSoon(@RequestHeader("Authorization") token: String) = movieRepository.getMoviesComingSoon(token)

    @GetMapping(value = ["/moviesWeekPremiere"])
    fun getMoviesWeekPremiere(@RequestHeader("Authorization") token: String) = movieRepository.getMoviesWeekPremiere(token)

    @GetMapping(value = ["/popularLists"])
    fun getPopularLists(@RequestHeader("Authorization") token: String) = listRepository.getWeekPopularList()

    @GetMapping(value = ["/news"])
    fun getNews() = newsRepository.getLastNews(1)
}