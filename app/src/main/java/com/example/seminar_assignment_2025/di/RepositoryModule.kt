package com.example.seminar_assignment_2025.di

import com.example.seminar_assignment_2025.data.movie.MovieRepository
import com.example.seminar_assignment_2025.data.movie.MovieRepositoryImpl
import com.example.seminar_assignment_2025.data.search.SearchHistoryRepository
import com.example.seminar_assignment_2025.data.search.SearchHistoryRepositoryImpl
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
    abstract fun bindMovieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository

    @Binds
    @Singleton
    abstract fun bindSearchHistoryRepository(searchHistoryRepositoryImpl: SearchHistoryRepositoryImpl): SearchHistoryRepository
}
