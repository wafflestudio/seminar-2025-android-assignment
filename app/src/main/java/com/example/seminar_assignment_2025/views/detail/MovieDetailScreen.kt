package com.example.seminar_assignment_2025.views.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.seminar_assignment_2025.domainmodel.Movie
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(movieId: Int, navController: NavController, viewModel: MovieDetailViewModel = hiltViewModel()) {

    LaunchedEffect(key1 = movieId) {
        viewModel.getMovieDetail(movieId)
    }

    val movie by viewModel.movieDetail.collectAsState()

    movie?.let { movieDetail ->
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(movieDetail.title, color = Color.Black, fontWeight = FontWeight.SemiBold, fontSize = 16.sp) },
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
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                Box(modifier = Modifier.height(301.dp)) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/original${movieDetail.backdropPath}",
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
                            .padding(horizontal = 16.dp)
                            .offset(y = 20.dp), // Push the row down to create overlap
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Card(
                            modifier = Modifier.size(164.dp, 246.dp),
                            shape = RoundedCornerShape(0.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            AsyncImage(
                                model = "https://image.tmdb.org/t/p/w500${movieDetail.posterPath}",
                                contentDescription = movieDetail.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = movieDetail.title,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            StarRating(rating = movieDetail.voteAverage)
                            Spacer(modifier = Modifier.height(8.dp))
                            MovieMetadata(movie = movieDetail)
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                }

                Column(modifier = Modifier.padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)) { // Add top padding to avoid overlap
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        movieDetail.genreIds.forEach { genreId ->
                            Chip(label = viewModel.getGenreName(genreId))
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Summary", style = typography.titleLarge, color = Color.Black, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(movieDetail.overview, style = typography.bodyLarge, color = Color.Black, fontWeight = FontWeight.Light, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Original Title", style = typography.titleLarge, color = Color.Black, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(movieDetail.originalTitle, style = typography.bodyLarge, color = Color.Black, fontWeight = FontWeight.Light, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Status", style = typography.titleLarge, color = Color.Black, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(movieDetail.status ?: "", style = typography.bodyLarge, color = Color.Black, fontWeight = FontWeight.Light, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Budget", style = typography.titleLarge, color = Color.Black, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(formatCurrency(movieDetail.budget), style = typography.bodyLarge, color = Color.Black, fontWeight = FontWeight.Light, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Revenue", style = typography.titleLarge, color = Color.Black, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(formatCurrency(movieDetail.revenue), style = typography.bodyLarge, color = Color.Black, fontWeight = FontWeight.Light, fontSize = 12.sp)
                }
            }
        }
    }
}

private fun formatCurrency(amount: Long?): String {
    if (amount == null || amount == 0L) return "-"
    return NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }.format(amount)
}

@Composable
fun MovieMetadata(movie: Movie) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        movie.runtime?.let {
            Text("${it / 60}h ${it % 60}m", color = Color.White, fontWeight = FontWeight.Light, fontSize = 10.sp)
        }
        Text(movie.releaseDate.substring(0, 4), color = Color.White, fontWeight = FontWeight.Light, fontSize = 10.sp)
        if (movie.adult) {
            Text("R18+", color = Color.Red, fontWeight = FontWeight.Medium, fontSize = 10.sp)
        } else {
            Text("All Ages", color = Color.White, fontWeight = FontWeight.Light, fontSize = 10.sp)
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
            color = Color.Black,
            style = typography.bodyMedium,
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
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
        )
        Spacer(Modifier.width(8.dp))
        Row {
            val starCount = (rating / 2).roundToInt().coerceIn(0, 5)
            repeat(5) { index ->
                val imageVector = if (index < starCount) Icons.Default.Star else Icons.Outlined.Star
                val tint = if (index < starCount) Color(0xFFFFC107) else Color.Gray
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
