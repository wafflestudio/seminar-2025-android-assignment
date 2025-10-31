package com.example.seminar_assignment_2025.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.seminar_assignment_2025.data.Movie
import com.example.seminar_assignment_2025.data.genreMap

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MovieDetailScreen(
    movieId: Int,
    onBackClick: () -> Unit, // (NavGenerate로부터 받음)
    viewModel: MovieDetailViewModel = viewModel()
) {
    val movie by viewModel.movie.collectAsState()

    LaunchedEffect(key1 = movieId) {
        viewModel.fetchMovieById(movieId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(
                    text = movie?.title ?: "",
                    maxLines = 1, // (제목이 길어도 한 줄로)
                    fontSize = 16.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { // (전달받은 onBackClick 함수 연결)
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    navigationIconContentColor = Color.Black
                )
            )
        }
    ) { innerPadding ->

        // (null)이면 로딩 스피너 표시
        if (movie == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        // 영화 정보가 로드되면, 스크롤 가능한 Column 표시
        else {
            val loadedMovie = movie!!

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .graphicsLayer { clip = false }
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(300.dp))

                    // --- (2) 메인 콘텐츠 ---
                    Column(modifier = Modifier.padding(22.dp)) {
                        // ⭐️ 4. 포스터가 튀어나올 공간(30dp)을 여기서 확보합니다.
                        //    (제거하면 포스터가 장르 칩을 가리게 됩니다)
                        Spacer(modifier = Modifier.height(16.dp))

                        GenreChips(genreIds = loadedMovie.genre_ids)

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Summary", style = MaterialTheme.typography.titleLarge, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(loadedMovie.overview ?: "No summary.", style = MaterialTheme.typography.bodyMedium, fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Popularity", style = MaterialTheme.typography.titleLarge, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(loadedMovie.popularity.toString(), style = MaterialTheme.typography.bodyMedium, fontSize = 12.sp)
                    }
                }

                // ⭐️ 5. 상단 헤더 (Z-Index = 1, 위에 표시됨)
                //    이 Composable은 스크롤되지 않고 화면 상단에 "고정"됩니다.
                MovieHeader(
                    movie = loadedMovie,
                    modifier = Modifier
                        .zIndex(1f) // <-- 콘텐츠(z=0)보다 위에 그리도록 함
                        .align(Alignment.TopCenter) // <-- Box의 상단에 고정
                )
            }
        }
    }
}

@Composable
private fun MovieHeader(movie: Movie,
                        modifier: Modifier = Modifier)
{
    val backdropUrl = "https://image.tmdb.org/t/p/original${movie.backdrop_path ?: ""}"
    val posterUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
    val rating = String.format("%.1f", movie.vote_average)

    val backdropHeight = 300.dp        // 화면에 보이는 배경 높이
    val overhang = 20.dp               // 포스터가 내려오는 높이
    val headerHeight = backdropHeight + overhang

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(headerHeight)
            .graphicsLayer { clip = false }
    ) {
        // 1. Backdrop (배경 이미지)
        AsyncImage(
            model = backdropUrl,
            contentDescription = "Backdrop",
            contentScale = ContentScale.Crop,
            modifier = Modifier.height(backdropHeight)
        )

        // 2. 검은색 그라데이션 (Figma 참고)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(backdropHeight)
                .background(Color.Black.copy(alpha = 0.4f))  // ★ 40% 검정
        )

        // 3. 텍스트 (하단 정렬)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart) // (Box의 하단에 배치)
                .padding(start = 16.dp, end = 16.dp), // 하단 패딩 추가
            verticalAlignment = Alignment.Bottom
        ) {
            // 포스터 공간 확보
            Spacer(
                modifier = Modifier
                    .width(164.dp) // 포스터 자리를 시각적으로 차지시키기 위한 더미
                    .aspectRatio(2 / 3f)
            )

            Spacer(modifier = Modifier.width(20.dp))
            Column (
                modifier = Modifier.align(Alignment.Bottom) // Column 내부 아이템들을 하단 정렬
            ) {
                Text(text = movie.title,
                    modifier = Modifier.width(150.dp),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically // 별과 텍스트 중앙 정렬
                ) {
                    Text(
                        text = rating, // 숫자 평점
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.width(4.dp)) // 별과 숫자 사이 간격

                    RatingStars(
                        rating = movie.vote_average.toFloat(),
                        maxStars = 5 // 총 별 개수
                    )
                }
                Spacer(modifier = Modifier.height(overhang + 36.dp))
            }
        }

        AsyncImage(
            model = posterUrl,
            contentDescription = movie.title,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp)
                .shadow(
                    elevation = 12.dp,                 // ★ 그림자 세기
                    clip = false,
                    ambientColor = Color.Black.copy(alpha = 0.35f), // 옵션
                    spotColor   = Color.Black.copy(alpha = 0.45f)   // 옵션
                )
                .width(164.dp)
                .aspectRatio(2 / 3f)
                .zIndex(2f)
        )
    }
}

@Composable
fun RatingStars(
    rating: Float,
    maxStars: Int = 5,
    starColor: Color = Color(0xFFFFA000) // Figma 주황색 별
) {
    Row {
        // 0.0 ~ 5.0 스케일로 변환
        val scaledRating = rating / 2f

        repeat(maxStars) { index ->
            val starType: ImageVector = when {
                index + 1 <= scaledRating -> Icons.Filled.Star // 꽉 찬 별
                index < scaledRating && index + 1 > scaledRating -> Icons.Filled.StarHalf // 반쪽 별
                else -> Icons.Filled.StarOutline // 빈 별
            }
            Icon(
                imageVector = starType,
                contentDescription = null, // 접근성 고려: "별점" 같은 텍스트 필요
                tint = starColor,
                modifier = Modifier.size(20.dp) // 별 아이콘 크기
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GenreChips(genreIds: List<Int>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val genreNames = genreIds.mapNotNull { genreMap[it] }
        genreNames.forEach { genreName ->
            SuggestionChip(
                onClick = { /* (클릭 X) */ },
                label = { Text(genreName) }
            )
        }
    }
}