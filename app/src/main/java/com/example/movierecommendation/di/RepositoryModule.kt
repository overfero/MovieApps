package com.example.movierecommendation.di

import com.example.movierecommendation.movieList.data.repository.MovieListRepositoryImpl
import com.example.movierecommendation.movieList.domain.repository.MovieListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieListRepositoryImpl: MovieListRepositoryImpl
    ): MovieListRepository
}