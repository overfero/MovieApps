package com.example.movierecommendation.core.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.movierecommendation.movieList.data.remote.MovieApi.Companion.IMAGE_BASE_URL
import com.example.movierecommendation.movieList.domain.model.Movie
import com.example.movierecommendation.movieList.presentation.BottomNavGraph
import com.example.movierecommendation.movieList.presentation.MovieListUIEvent
import com.example.movierecommendation.movieList.presentation.MovieListViewModel
import com.example.movierecommendation.movieList.presentation.NavGraph
import com.example.movierecommendation.movieList.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieState = movieListViewModel.movieListState.collectAsState().value
    val bottomNavController = rememberNavController()
    

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = bottomNavController, onEvent = movieListViewModel::onEvent)
        },
        topBar = {
            TopAppBar(title = {
                Text(text = if (movieState.isCurrentPopularScreen) "Popular Movie" else "Upcoming Movie",
                    fontSize = 20.sp)
            },
                modifier = Modifier.shadow(2.dp),
                colors = topAppBarColors(MaterialTheme.colorScheme.inverseOnSurface)
            )
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)){
            BottomNavGraph(
                navHostController = bottomNavController,
                movieListState = movieState,
                onEvent = movieListViewModel::onEvent,
                detailController = navController)
        }
    }
}

@Composable
fun BottomNavBar(
    navController: NavHostController,
    onEvent: (MovieListUIEvent) -> Unit
    ) {
    val items = listOf(
        BottomItem(title = "Popular", icon = Icons.Rounded.Movie),
        BottomItem(title = "Upcoming", icon = Icons.Rounded.Upcoming)
    )
    var selected by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar() {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface),
            verticalAlignment = Alignment.Bottom
            ) {
            items.forEachIndexed { index, bottomItem ->
                NavigationBarItem(
                    selected = selected == index,
                    onClick = {
                              selected = index
                        when(selected){
                            0 -> {
                                onEvent(MovieListUIEvent.Navigate)
                                navController.popBackStack()
                                navController.navigate(Screen.PopularMovieList.route)
                            }
                            1 -> {
                                onEvent(MovieListUIEvent.Navigate)
                                navController.popBackStack()
                                navController.navigate(Screen.UpcomingMovieList.route)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = bottomItem.icon,
                            contentDescription = bottomItem.title,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }, label = {
                        Text(text = bottomItem.title, color = MaterialTheme.colorScheme.onBackground)
                    }
                )
            }
        }
    }
    
}

data class BottomItem(
    val title: String,
    val icon: ImageVector
)