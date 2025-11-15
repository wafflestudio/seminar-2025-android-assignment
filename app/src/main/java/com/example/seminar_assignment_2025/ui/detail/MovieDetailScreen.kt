package com.example.seminar_assignment_2025.ui.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(navController: NavController) {

    val viewModel: MovieDetailViewModel = hiltViewModel()

    // 1. ViewModel의 StateFlow를 관찰합니다.
    val movieDetail by viewModel.movieDetail.collectAsState()

    // 2. movieDetail이 null이면 (로딩 중) 로딩 스피너 표시
    if (movieDetail == null) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar( // ⭐️ 뒤로가기 버튼이 있는 TopAppBar
                    title = { Text("") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White,
                        navigationIconContentColor = Color.Black
                    )
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator() // ⭐️ 로딩 스피너
            }
        }
    } else {
        // 3. 데이터가 도착하면 (null이 아니면) UI를 그립니다.
        val movie = movieDetail!! // non-null로 사용

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(movie.title, fontSize = 16.sp) },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.size(width = 22.dp, height = 25.dp)
                        ) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.Black,
                        navigationIconContentColor = Color.Black
                    ),
                    modifier = Modifier.height(35.dp)
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                item {
                    Box(modifier = Modifier.height(301.dp)) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/original${movie.backdropPath}",
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.4f))
                        )
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Card(
                                modifier = Modifier.size(164.dp, 246.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                            ) {
                                AsyncImage(
                                    model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                                    contentDescription = movie.title,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = movie.title,
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                StarRating(rating = movie.voteAverage)

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp) // 항목 간 간격
                                ) {
                                    // (1) 상영 시간 (ex: 1h 40m)
                                    if (movie.runtime > 0) {
                                        Text(
                                            text = formatRuntime(movie.runtime), // ⬅️ 헬퍼 함수 사용
                                            color = Color.White.copy(alpha = 0.8f), // 살짝 투명한 흰색
                                            fontSize = 12.sp
                                        )
                                    }

                                    // (2) 개봉 연도 (ex: 2003)
                                    // (StringIndexOutOfBoundsException 방지)
                                    if (movie.releaseDate.length >= 4) {
                                        Text(
                                            text = movie.releaseDate.take(4), // ⬅️ 연도만 (안전하게)
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontSize = 12.sp
                                        )
                                    }

                                    // (3) 청소년 불가 (R18+)
                                    if (movie.adult) {
                                        // 빨간색 R18+ 칩 (Surface/Card 대신 간단히 구현)
                                        Text(
                                            text = "R18+",
                                            color = Color.Red,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .border(BorderStroke(1.dp, Color.Red), RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val horizontalScrollState = rememberScrollState()

                        Row(
                            modifier = Modifier.horizontalScroll(horizontalScrollState),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            movie.genres.forEach { genreName ->
                                Chip(label = genreName)
                            }
                        }

                        // ⭐️ 상세 정보에만 있는 "Tagline" 추가
                        if (movie.tagline.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text("Tagline", style = MaterialTheme.typography.titleLarge, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "\"${movie.tagline}\"",
                                style = MaterialTheme.typography.bodyLarge,
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Summary", style = MaterialTheme.typography.titleLarge, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(movie.overview, style = MaterialTheme.typography.bodyLarge, fontSize = 12.sp)

                        // ⭐️ 상세 정보에만 있는 "Runtime" 추가
                        if (movie.runtime > 0) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text("Runtime", style = MaterialTheme.typography.titleLarge, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("${movie.runtime} minutes", style = MaterialTheme.typography.bodyLarge, fontSize = 12.sp)
                        }

                        // ⭐️ "Popularity"는 MovieDetail 모델에 없으므로 제거
                        /*
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Popularity", style = MaterialTheme.typography.titleLarge, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(movie.popularity.toString(), style = MaterialTheme.typography.bodyLarge, fontSize = 12.sp)
                        */
                    }
                }
            }
        }
    }
}

@Composable
fun Chip(label: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = Modifier
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 12.sp
        )
    }
}

@Composable
fun StarRating(rating: Double) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = String.format("%.1f", rating),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
        )
        Spacer(Modifier.width(8.dp))
        Row {
            val starCount = (rating / 2).roundToInt().coerceIn(0, 5)
            repeat(5) { index ->
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = if (index < starCount) Color(0xFFFFC107) else Color.Gray,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

private fun formatRuntime(minutes: Int): String {
    val hours = minutes / 60
    val mins = minutes % 60
    return "${hours}h ${mins}m"
}
