package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DownloadState
import com.example.data.model.VideoItem
import com.example.data.model.WatchHistoryItem
import com.example.ui.SortOption
import com.example.ui.components.VideoCard
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
  videos: List<VideoItem>,
  searchQuery: String,
  selectedCategory: String,
  sortOption: SortOption,
  likedVideoIds: Set<String>,
  watchHistoryMap: Map<String, WatchHistoryItem>,
  offlineStateMap: Map<String, DownloadState>,
  onSearchChange: (String) -> Unit,
  onCategorySelect: (String) -> Unit,
  onSortSelect: (SortOption) -> Unit,
  onVideoClick: (VideoItem) -> Unit,
  onLikeClick: (VideoItem) -> Unit,
  modifier: Modifier = Modifier
) {
  val categories = listOf("Все", "Маньяки", "Расследования", "Психология и кино", "Новые выпуски")

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("catalog_screen")
  ) {
    // Search Box
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        placeholder = { Text("Поиск по делам, маньякам, темам...", fontSize = 14.sp) },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = CinemaRed
          )
        },
        trailingIcon = {
          if (searchQuery.isNotBlank()) {
            IconButton(onClick = { onSearchChange("") }) {
              Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Очистить поиск",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = CinemaRed,
          unfocusedBorderColor = MaterialTheme.colorScheme.outline
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("catalog_search_field")
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Category filter chips
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(categories) { cat ->
          FilterChip(
            selected = selectedCategory == cat,
            onClick = { onCategorySelect(cat) },
            label = {
              Text(
                text = cat,
                fontSize = 12.sp,
                fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = CinemaRed,
              selectedLabelColor = Color.White
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Sort row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Найдено: ${videos.size}",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          SortChip(
            label = "Популярные",
            selected = sortOption == SortOption.POPULAR,
            onClick = { onSortSelect(SortOption.POPULAR) }
          )
          SortChip(
            label = "Рейтинг",
            selected = sortOption == SortOption.RATING,
            onClick = { onSortSelect(SortOption.RATING) }
          )
          SortChip(
            label = "Новые",
            selected = sortOption == SortOption.NEWEST,
            onClick = { onSortSelect(SortOption.NEWEST) }
          )
        }
      }
    }

    // Grid of videos
    if (videos.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(64.dp)
          )
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "Ничего не найдено",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Попробуйте изменить запрос или сбросить фильтры категорий.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    } else {
      LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(videos, key = { it.id }) { video ->
          VideoCard(
            video = video,
            isLiked = likedVideoIds.contains(video.id),
            watchHistory = watchHistoryMap[video.id],
            downloadState = offlineStateMap[video.id] ?: DownloadState.NOT_DOWNLOADED,
            onCardClick = { onVideoClick(video) },
            onLikeClick = { onLikeClick(video) },
            cardWidth = 180,
            cardHeight = 240
          )
        }
      }
    }
  }
}

@Composable
private fun SortChip(
  label: String,
  selected: Boolean,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(6.dp))
      .background(if (selected) CinemaRed.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
      .padding(horizontal = 8.dp, vertical = 4.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = label,
      color = if (selected) CinemaRed else MaterialTheme.colorScheme.onSurfaceVariant,
      fontSize = 11.sp,
      fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
      modifier = Modifier.testTag("sort_${label}")
    )
  }
}
