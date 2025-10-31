package com.example.seminar_assignment_2025.ui

import androidx.compose.foundation.clickable
import com.example.seminar_assignment_2025.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.seminar_assignment_2025.search.SearchViewModel
import androidx.compose.ui.text.input.ImeAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier,
                 viewModel: SearchViewModel = viewModel(),
                 onMovieClick: (Int) -> Unit) {

    // 상태 구독
    val searchQuery by viewModel.searchQuery.collectAsState()
    val recentSearches by viewModel.recentSearches.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    // (키보드 컨트롤러: 검색 후 키보드를 숨길 때 사용)
    val keyboardController = LocalSoftwareKeyboardController.current

    // 검색창을 눌렀는지 확인
    var isFocused by remember { mutableStateOf(false) }

    // 1. Column: 위(검색창)에서 아래(내용)로 쌓기 위해 사용
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 2. 검색창 (TextField)
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onQueryChanged(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState -> // 포커스 변경 시 상태 업데이트
                isFocused = focusState.isFocused
            },
            placeholder = { Text("영화 검색...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            singleLine = true,

            // '가위표(X)' 버튼
            trailingIcon = {
                if (searchQuery.isNotEmpty()) { // 만약 비어있지 않다면, X자를 표시.
                    IconButton(onClick = { viewModel.onQueryChanged("") }) { // 검색창 비우기
                        Icon(Icons.Default.Clear, contentDescription = "Clear search")
                    }
                }
            },

            // 키보드의 '검색' 버튼
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search // 키보드 액션 버튼을 '검색'으로 변경
            ),

            // 검색 버튼을 눌렀을 때 실행할 행동
            keyboardActions = KeyboardActions(
                onSearch = {
                    // ViewModel에 '검색어 저장'을 '보고'
                    viewModel.saveSearchQuery(searchQuery)
                    // 키보드 숨기기
                    keyboardController?.hide()
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. UI 조건부 렌더링
        if (searchQuery.isNotEmpty()) {
            // 검색 결과가 있으면 -> 'SearchResultList' 표시
            if (searchResults.isNotEmpty()) {
                SearchResultList(
                    movies = searchResults,
                    onMovieClick = { movie ->
                        // 영화 상세보기 이동
                        onMovieClick(movie.id)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            // 검색 결과가 없으면 -> '검색 결과 없음' EmptyState 표시
            else {
                // (EmptyState를 재사용하거나, '결과 없음' 전용 Composable을 만듭니다)
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("'${searchQuery}'에 대한 검색 결과가 없습니다.")
                }
            }
        } else {
            if (isFocused) {
                // 최근 검색어가 비어있을 때
                if (recentSearches.isNotEmpty()) {
                    // (1) 최근 검색어가 있으면 목록 보여주기
                    RecentSearchList(
                        recentSearches = recentSearches,
                        onSearch = { query ->
                            viewModel.onQueryChanged(query) // 항목 클릭 -> 검색창 채우기
                        },
                        onDelete = { query ->
                            viewModel.deleteSearch(query) // 개별 삭제
                        },
                        onClearAll = {
                            viewModel.clearAllSearches() // 전체 삭제
                        }
                    )
                    EmptyState(
                        modifier = Modifier
                            .weight(1f) // ⭐️ 최근 검색어를 제외한 "남은 공간"을 모두 차지
                            .fillMaxWidth()
                    )
                } else {
                    // (2) 최근 검색어도 없으면 EmptyState 보여주기
                    EmptyState(modifier = Modifier.fillMaxSize())
                }
            } else {
                EmptyState(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    // Box: 내부 아이템들을 정렬하기 편함
    Box(
        modifier = modifier, // 부모(Column)가 준 공간을 꽉 채움
        contentAlignment = Alignment.Center // 내용물을 정중앙에 배치
    ) {
        // Column: 아이콘과 텍스트를 세로로 나열
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 1. 영화 아이콘
            Icon(
                painter = painterResource(id = R.drawable.movie_svgrepo_com),
                contentDescription = "Movie Icon",
                modifier = Modifier.size(80.dp),
                tint = Color.Gray // 아이콘 색상을 회색으로
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 2. 텍스트
            Text(
                "영화를 검색해보세요",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "상단 검색 바를 통해\n원하는 영화를 찾아보세요",
                color = Color.Gray,
                textAlign = TextAlign.Center // 여러 줄일 때 중앙 정렬
            )
        }
    }
}