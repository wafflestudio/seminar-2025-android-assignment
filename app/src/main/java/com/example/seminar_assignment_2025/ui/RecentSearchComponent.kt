package com.example.seminar_assignment_2025.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 최신 검색어 틀
@Composable
fun RecentSearchList(
    recentSearches: List<String>,
    onSearch: (String) -> Unit,
    onDelete: (String) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    // LazyColumn을 쓰면 성능이 더 좋아진다고 함. (화면에 보이는 애들만 표시하는 느낌인듯)
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        // 헤더 (최근 검색어 ... 전체 삭제)
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("최근 검색어", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    "전체 삭제",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onClearAll() } // '전체 삭제' 클릭
                )
            }
        }

        // 목록 아이템
        // items는 List 등을 받아서 여러 개의 칸을 만든다고 함. (사기인듯)
        items(recentSearches) { query ->
            RecentSearchItem(
                query = query,
                onSearch = { onSearch(query) }, // '항목 클릭'
                onDelete = { onDelete(query) }  // '개별 삭제' 클릭
            )
            Divider(color = Color.Gray.copy(alpha = 0.3f))
        }
    }
}

// 각 최신 검색어 하나
@Composable
fun RecentSearchItem(
    query: String,
    onSearch: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSearch() } // X가 아닌 부분 클릭
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = "Recent search",
            tint = Color.Gray,
            modifier = Modifier.padding(end = 12.dp)
        )
        Text(
            text = query,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onDelete) { // X 버튼 클릭
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete search",
                tint = Color.Gray
            )
        }
    }
}