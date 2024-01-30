package com.example.movierecommendation.movieList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierecommendation.movieList.domain.model.Movie
import com.example.movierecommendation.movieList.domain.repository.MovieListRepository
import com.example.movierecommendation.movieList.util.Category
import com.example.movierecommendation.movieList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
): ViewModel(){
    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    init {
        getPopularMovieList(false)
        getUpcomingMovieList(false)
    }

    fun onEvent(event: MovieListUIEvent){
        when(event){
            MovieListUIEvent.Navigate -> {
                _movieListState.update { it.copy(isCurrentPopularScreen = !movieListState.value.isCurrentPopularScreen) }
            }
            is MovieListUIEvent.Paginate -> {
                if (event.category == Category.POPULAR){
                    getPopularMovieList(true)
                }else if (event.category == Category.UPCOMING){
                    getUpcomingMovieList(true)
                }
            }
        }
    }

    private fun getPopularMovieList(forceFetchFromRemote: Boolean){
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.POPULAR,
                movieListState.value.popularMovieListPage
            ).collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Succes -> {
                        result.data?.let {popularList ->
                            _movieListState.update {it.copy(
                                popularMovieList = movieListState.value.popularMovieList
                                        + popularList.shuffled(),
                                popularMovieListPage = movieListState.value.popularMovieListPage + 1
                            )}
                        }
                    }
                }
            }
        }
    }

    private fun getUpcomingMovieList(forceFetchFromRemote: Boolean){
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.UPCOMING,
                movieListState.value.upcomingMovieListPage
            ).collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Succes -> {
                        result.data?.let {upcomingList ->
                            _movieListState.update {it.copy(
                                upcomingMovieList = movieListState.value.upcomingMovieList
                                        + upcomingList.shuffled(),
                                upcomingMovieListPage = movieListState.value.upcomingMovieListPage + 1
                            )}
                        }
                    }
                }
            }
        }
    }
}

data class MovieListState(
    val isLoading: Boolean = false,

    val popularMovieListPage: Int = 1,
    val upcomingMovieListPage: Int = 1,

    val isCurrentPopularScreen: Boolean = true,

    val popularMovieList: List<Movie> = emptyList(),
    val upcomingMovieList: List<Movie> = emptyList()
)