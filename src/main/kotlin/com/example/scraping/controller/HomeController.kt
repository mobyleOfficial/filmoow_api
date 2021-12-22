package com.example.scraping.controller

import com.example.scraping.repository.list.ListRepositoryImpl
import com.example.scraping.repository.movie.MovieRepositoryImpl
import com.example.scraping.repository.news.NewsRepositoryImpl
import com.example.scraping.repository.series.SeriesRepositoryImpl
import com.example.scraping.repository.tvShow.TvShowRepositoryImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping(value = ["/home"])
class HomeController {
    @Autowired
    lateinit var movieRepositoryImpl: MovieRepositoryImpl

    @Autowired
    lateinit var  seriesRepositoryImpl: SeriesRepositoryImpl

    @Autowired
    lateinit var  tvShowRepositoryImpl: TvShowRepositoryImpl

    @Autowired
    lateinit var  listRepositoryImpl: ListRepositoryImpl

    @Autowired
    lateinit var  newsRepositoryImpl: NewsRepositoryImpl

    @GetMapping(value = ["/popularMovies"])
    fun getPopularMovies() = movieRepositoryImpl.getPopularMovies(1)

    @GetMapping(value = ["/popularSeries"])
    fun getPopularSeries() = seriesRepositoryImpl.getPopularSeries(1)

    @GetMapping(value = ["/popularTvShow"])
    fun getPopularTvShow() = tvShowRepositoryImpl.getPopularTvShow(1)

    @GetMapping(value = ["/availableCinemaMovies"])
    fun getAvailableMovies() = movieRepositoryImpl.getAvailableMovies()

    @GetMapping(value = ["/comingSoonMovies"])
    fun getMoviesComingSoon() = movieRepositoryImpl.getMoviesComingSoon()

    @GetMapping(value = ["/moviesWeekPremiere"])
    fun getMoviesWeekPremiere() = movieRepositoryImpl.getMoviesWeekPremiere()

    @GetMapping(value = ["/popularLists"])
    fun getPopularLists() = listRepositoryImpl.getWeekPopularList()

    @GetMapping(value = ["/news"])
    fun getNews() = newsRepositoryImpl.getLastNews(1)
}