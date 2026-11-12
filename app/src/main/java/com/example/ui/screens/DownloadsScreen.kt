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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.FileDownloadOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.DownloadState
import com.example.data.model.OfflineItem
import com.example.data.model.VideoItem
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaRed

@Composable
fun DownloadsScreen(
  downloadedVideos: List<Pair<VideoItem, OfflineItem>>,
  onPlayVideo: (VideoItem) -> Unit,
  onDeleteDownload: (String) -> Unit,
  onClearAll: () -> Unit,
  modifier: Modifier = Modifier
) {
  val totalOccupiedMb = downloadedVideos
    .filter { it.second.state == DownloadState.COMPLETED }
    .sumOf { it.second.fileSizeMb }

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("downloads_screen")
  ) {
    // Header & Storage Bar
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      ),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(CinemaGold.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.WifiOff,
                contentDescription = null,
                tint = CinemaGold,
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Офлайн-хранилище",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "${downloadedVideos.size} выпуска • $totalOccupiedMb МБ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          if (downloadedVideos.isNotEmpty()) {
            TextButton(onClick = onClearAll) {
              Text("Очистить", color = CinemaRed, fontSize = 12.sp)
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Storage progress
        val storageRatio = (totalOccupiedMb.toFloat() / 8000f).coerceIn(0.02f, 1f)
        LinearProgressIndicator(
          progress = { storageRatio },
          color = CinemaGold,
          trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp))
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "Занято Savelev TV: $totalOccupiedMb МБ",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "Доступно: 16.4 ГБ",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    if (downloadedVideos.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.FileDownloadOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(64.dp)
          )
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "Нет сохранённых видео",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Нажмите значок «Офлайн» в плеере любого выпуска, чтобы смотреть ролики Кости Савельева без подключения к интернету.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    } else {
      LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(downloadedVideos, key = { it.first.id }) { (video, offline) ->
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("download_item_${video.id}")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Thumbnail
              Box(
                modifier = Modifier
                  .size(width = 110.dp, height = 70.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color.Black)
              ) {
                AsyncImage(
                  model = video.effectiveThumbnailUrl,
                  contentDescription = video.title,
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
                )

                Box(
                  modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(3.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xCC000000))
                    .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                  Text(
                    text = video.duration,
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium
                  )
                }
              }

              Spacer(modifier = Modifier.width(12.dp))

              // Title and details
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = video.title,
                  style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                  ),
                  color = MaterialTheme.colorScheme.onSurface,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                  text = "${offline.fileSizeMb} МБ • HD 1080p",
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (offline.state == DownloadState.DOWNLOADING) {
                  LinearProgressIndicator(
                    progress = { offline.progressPercent / 100f },
                    color = CinemaRed,
                    modifier = Modifier
                      .fillMaxWidth()
                      .height(3.dp)
                  )
                  Text(
                    text = "Загрузка: ${offline.progressPercent}%",
                    fontSize = 10.sp,
                    color = CinemaRed,
                    fontWeight = FontWeight.Bold
                  )
                } else {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.DownloadDone,
                      contentDescription = null,
                      tint = CinemaGold,
                      modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "Готово к просмотру без сети",
                      fontSize = 10.sp,
                      color = CinemaGold,
                      fontWeight = FontWeight.SemiBold
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.width(8.dp))

              // Play Offline Button
              if (offline.state == DownloadState.COMPLETED) {
                IconButton(
                  onClick = { onPlayVideo(video) },
                  modifier = Modifier
                    .clip(CircleShape)
                    .background(CinemaRed)
                    .size(36.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Смотреть офлайн",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                  )
                }
              }

              IconButton(onClick = { onDeleteDownload(video.id) }) {
                Icon(
                  imageVector = Icons.Default.DeleteOutline,
                  contentDescription = "Удалить загрузку",
                  tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }
  }
}
