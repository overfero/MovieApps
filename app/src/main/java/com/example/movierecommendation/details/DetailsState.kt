package com.example.movierecommendation.details

import com.example.movierecommendation.movieList.domain.model.Movie

data class DetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)
