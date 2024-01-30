package com.example.movierecommendation.movieList.util

sealed class Screen(val route: String) {
    object Home: Screen(route = "main")
    object PopularMovieList: Screen(route = "popularMovie")
    object UpcomingMovieList: Screen(route = "upcomingMovie")
    object Details: Screen(route = "details")
}