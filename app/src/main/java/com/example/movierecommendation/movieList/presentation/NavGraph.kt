package com.example.movierecommendation.movieList.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.movierecommendation.core.presentation.HomeScreen
import com.example.movierecommendation.details.DetailScreen
import com.example.movierecommendation.movieList.util.Screen

@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Screen.Home.route){
        composable(route = Screen.Home.route){
            HomeScreen(navHostController)
        }
        composable(
            route = Screen.Details.route + "/{movieId}",
            arguments = listOf(navArgument("movieId"){type = NavType.IntType})
            ){
            DetailScreen()
        }
    }
}