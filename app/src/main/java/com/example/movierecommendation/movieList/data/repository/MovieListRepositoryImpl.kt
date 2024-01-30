package com.example.movierecommendation.movieList.data.repository

import com.example.movierecommendation.movieList.data.local.movie.MovieDatabase
import com.example.movierecommendation.movieList.data.mappers.toMovie
import com.example.movierecommendation.movieList.data.mappers.toMovieEntity
import com.example.movierecommendation.movieList.data.remote.MovieApi
import com.example.movierecommendation.movieList.domain.model.Movie
import com.example.movierecommendation.movieList.domain.repository.MovieListRepository
import com.example.movierecommendation.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : MovieListRepository {
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))

            val localMovieList = movieDatabase.movieDao.getMovieListByCategory(category)
            val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocalMovie){
                emit(Resource.Succes(data = localMovieList.map { it.toMovie(category) }))
                emit(Resource.Loading(false))
                return@flow
            }

            val movieListFromApi = try {
                movieApi.getMovieList(category,page)
            } catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: Exception){
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }

            val movieEntity = movieListFromApi.results.let {
                it.map { movieDto ->
                    movieDto.toMovieEntity(category)
                }
            }

            movieDatabase.movieDao.upsertMovieList(movieEntity)
            emit(Resource.Succes(data = movieEntity.map { it.toMovie(category) }))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))

            val movieEntity = movieDatabase.movieDao.getMovieById(id)

            if (movieEntity != null){
                emit(Resource.Succes(data = movieEntity.toMovie(movieEntity.category)))
                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Error(message = "Error no such movie"))
            emit(Resource.Loading(false))
        }
    }
}