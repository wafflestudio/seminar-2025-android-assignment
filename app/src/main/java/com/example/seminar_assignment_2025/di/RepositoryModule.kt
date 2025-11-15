package com.example.seminar_assignment_2025.di

import com.example.seminar_assignment_2025.data.repository.MovieRepository
import com.example.seminar_assignment_2025.data.repository.MovieRepositoryImpl
import com.example.seminar_assignment_2025.network.TmdbApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.content.Context
import com.example.seminar_assignment_2025.data.repository.SearchHistoryRepository
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(apiService: TmdbApiService): MovieRepository {
        // Hilt가 NetworkModule에서 apiService를 가져와서 주입해줍니다.
        return MovieRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideSearchHistoryRepository(
        @ApplicationContext context: Context // Hilt가 Context를 주입해줌
    ): SearchHistoryRepository {
        return SearchHistoryRepository(context)
    }

}