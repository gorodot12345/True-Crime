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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.components.VideoCard
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaRed

@Composable
fun FavoritesScreen(
  favoriteVideos: List<VideoItem>,
  likedVideoIds: Set<String>,
  watchHistoryMap: Map<String, WatchHistoryItem>,
  offlineStateMap: Map<String, DownloadState>,
  onVideoClick: (VideoItem) -> Unit,
  onLikeClick: (VideoItem) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("favorites_screen")
  ) {
    // Header
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      ),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(CinemaRed),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(26.dp)
          )
        }

        Column(modifier = Modifier.padding(start = 14.dp)) {
          Text(
            text = "Понравившиеся выпуски",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Сохранены локально в Savelev TV • ${favoriteVideos.size} видео",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    if (favoriteVideos.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(64.dp)
          )
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "Список лайков пуст",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Нажмите на сердечко у любого выпуска MINDHUNTER, чтобы добавить его в коллекцию любимых расследований.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    } else {
      LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 90.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(favoriteVideos, key = { it.id }) { video ->
          VideoCard(
            video = video,
            isLiked = true,
            watchHistory = watchHistoryMap[video.id],
            downloadState = offlineStateMap[video.id] ?: DownloadState.NOT_DOWNLOADED,
            onCardClick = { onVideoClick(video) },
            onLikeClick = { onLikeClick(video) }
          )
        }
      }
    }
  }
}
