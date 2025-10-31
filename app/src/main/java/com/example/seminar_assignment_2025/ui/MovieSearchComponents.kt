package com.example.seminar_assignment_2025.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.seminar_assignment_2025.data.Movie
import com.example.seminar_assignment_2025.data.formatGenres
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

@Composable
fun SearchResultList(
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp) // 아이템 사이 간격
    ) {
        // 헤더
        item {
            Text(
                "검색 결과 ${movies.size}개",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // 영화 목록
        items(movies) { movie ->
            MovieItem(
                movie = movie,
                onClick = onMovieClick
            )
        }
    }
}

@Composable
fun MovieItem(
    movie: Movie,
    onClick: (Movie) -> Unit, // 클릭 시 상세보기 이동을 위한 콜백
    modifier: Modifier = Modifier
) {
    // (스펙: 포스터 URL 조립)
    val posterUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"

    // (스펙: 연도 추출)
    val year = movie.release_date.substringBefore("-", "N/A") // "2024-10-30" -> "2024"

    // (스펙: 장르 변환)
    val genres = formatGenres(movie.genre_ids)

    // (스펙: 평점 반올림)
    val rating = String.format("%.1f", movie.vote_average) // 소수점 첫째 자리

    Card( // 카드 UI로 감싸서 그림자 효과
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick(movie) }, // (스펙: 클릭 시 상세보기 이동)
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. 포스터 이미지 (Coil 사용)
            AsyncImage(
                model = posterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .width(80.dp) // 너비 고정
                    .aspectRatio(2 / 3f) // 포스터 비율 (2:3)
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop // 비율에 맞게 이미지 자르기
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 2. 영화 정보 (Column)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.title, // (스펙: 제목)
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$year • $genres", // (스펙: 연도, 장르)
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                // (스펙: 평점)
                Text(
                    text = "⭐ $rating",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
