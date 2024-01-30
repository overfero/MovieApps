package com.example.movierecommendation.movieList.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.movierecommendation.core.presentation.HomeScreen
import com.example.movierecommendation.movieList.util.Screen

@Composable
fun BottomNavGraph(
    navHostController: NavHostController,
    movieListState: MovieListState,
    onEvent: (MovieListUIEvent) -> Unit,
    detailController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Screen.PopularMovieList.route){
        composable(route = Screen.PopularMovieList.route){
            PopularMovieScreen(movieListState = movieListState, navController = detailController, onEvent = onEvent)
        }
        composable(route = Screen.UpcomingMovieList.route){
            UpcomingMovieScreen(movieListState = movieListState, navController = detailController, onEvent = onEvent)
        }
    }
}

