package com.example.seminar_assignment_2025.data.repository

import com.example.seminar_assignment_2025.data.Movie
import com.example.seminar_assignment_2025.network.TmdbApiService

// 1. 생성자에서 TmdbApiService를 주입받습니다.
class MovieRepositoryImpl(
    private val apiService: TmdbApiService
) : MovieRepository {

    // 2. 하드코딩된 movieRawData, genreMapData, json 객체 모두 삭제!

    // 3. 장르 정보는 매번 API로 가져오면 비효율적이므로, 내부에 캐시합니다.
    private val genreMap = mutableMapOf<Int, String>()

    // 장르 맵이 비어있을 때만 API를 호출해 채워 넣는 도우미 함수
    private suspend fun fetchGenresIfNeeded() {
        if (genreMap.isEmpty()) {
            val genreListDto = apiService.getGenres()
            genreMap.putAll(genreListDto.genres.associate { it.id to it.name })
        }
    }

    override suspend fun searchByTitle(query: String): List<Movie> {
        if (query.isBlank()) {
            return emptyList()
        }

        // 4. API 서비스를 호출해 응답을 받습니다.
        val response = apiService.searchMovie(query)

        fetchGenresIfNeeded()

        // 5. Dto 리스트를 Movie 모델 리스트로 변환 (기존 로직과 동일)
        return response.results.map { dto ->
            Movie(
                id = dto.id,
                title = dto.title,
                releaseDate = dto.releaseDate,
                voteAverage = dto.voteAverage,
                posterPath = dto.posterPath ?: "",
                genres = dto.genreIds.map { genreId ->
                    genreMap[genreId] ?: ""
                },
                backdropPath = dto.backdropPath ?: "",
                overview = dto.overview ?: "",
                popularity = dto.popularity
            )
        }
    }

    override suspend fun getGenreName(id: Int): String {
        // 6. 캐시된 맵을 사용합니다.
        fetchGenresIfNeeded() // 맵이 비어있으면 채워넣습니다.
        return genreMap[id] ?: "" // 맵에서 장르 이름을 찾습니다.
    }

    override suspend fun getMovieDetail(id: Int): MovieDetail {
        // 1. ApiService에서 새 함수를 호출해 DTO를 받습니다.
        val dto = apiService.getMovieDetail(id)

        // 2. MovieDetailDto를 UI용 MovieDetail 모델로 변환합니다.
        return MovieDetail(
            id = dto.id,
            title = dto.title,
            releaseDate = dto.releaseDate,
            voteAverage = dto.voteAverage,
            posterPath = dto.posterPath ?: "",
            backdropPath = dto.backdropPath ?: "",
            overview = dto.overview ?: "",

            // ⭐️ 상세 DTO는 GenreDto 리스트를 주므로, .name만 뽑아서 String 리스트로 만듭니다.
            genres = dto.genres.map { it.name },

            runtime = dto.runtime ?: 0,
            tagline = dto.tagline ?: "",
            adult = dto.adult ?: false
        )
    }

}