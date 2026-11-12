package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DownloadState
import com.example.data.model.VideoItem
import com.example.data.model.WatchHistoryItem
import com.example.ui.theme.CinemaRed

@Composable
fun VideoHorizontalRail(
  title: String,
  subtitle: String? = null,
  videos: List<VideoItem>,
  likedVideoIds: Set<String>,
  watchHistoryMap: Map<String, WatchHistoryItem>,
  offlineStateMap: Map<String, DownloadState> = emptyMap(),
  onVideoClick: (VideoItem) -> Unit,
  onLikeClick: (VideoItem) -> Unit,
  onSeeAllClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  if (videos.isEmpty()) return

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 10.dp)
  ) {
    // Header Row
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
          ),
          color = MaterialTheme.colorScheme.onBackground
        )
        if (!subtitle.isNullOrBlank()) {
          Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      if (onSeeAllClick != null) {
        TextButton(onClick = onSeeAllClick) {
          Text(
            text = "Все >",
            color = CinemaRed,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Horizontal List
    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(videos, key = { it.id }) { video ->
        VideoCard(
          video = video,
          isLiked = likedVideoIds.contains(video.id),
          watchHistory = watchHistoryMap[video.id],
          downloadState = offlineStateMap[video.id] ?: DownloadState.NOT_DOWNLOADED,
          onCardClick = { onVideoClick(video) },
          onLikeClick = { onLikeClick(video) }
        )
      }
    }
  }
}
