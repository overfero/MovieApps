package com.example.movierecommendation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierecommendation.movieList.domain.repository.MovieListRepository
import com.example.movierecommendation.movieList.presentation.MovieListState
import com.example.movierecommendation.movieList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository,
    private val saveStateHandle: SavedStateHandle
): ViewModel() {
    private val movieId = saveStateHandle.get<Int>("movieId")
    private var _detailState = MutableStateFlow(DetailsState())
    val detailState = _detailState.asStateFlow()

    init {
        getMovie(movieId ?: -1)
    }

    private fun getMovie(id: Int){
        viewModelScope.launch {
            _detailState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovie(id).collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        _detailState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _detailState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Succes -> {
                        result.data?.let {movie ->
                            _detailState.update {
                                it.copy(movie = movie)
                            }
                        }
                    }
                }
            }
        }
    }
}