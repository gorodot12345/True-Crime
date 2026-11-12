package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.DownloadState
import com.example.data.model.VideoItem
import com.example.data.model.WatchHistoryItem
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaRed

@Composable
fun VideoCard(
  video: VideoItem,
  isLiked: Boolean,
  watchHistory: WatchHistoryItem?,
  downloadState: DownloadState = DownloadState.NOT_DOWNLOADED,
  onCardClick: () -> Unit,
  onLikeClick: () -> Unit,
  modifier: Modifier = Modifier,
  cardWidth: Int = 200,
  cardHeight: Int = 260
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    modifier = modifier
      .width(cardWidth.dp)
      .clickable { onCardClick() }
      .testTag("video_card_${video.id}")
  ) {
    Column {
      // Thumbnail Box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp)
          .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
          .background(Color(0xFF14141A))
      ) {
        AsyncImage(
          model = video.effectiveThumbnailUrl,
          contentDescription = video.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )

        // Dark gradient overlay
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color(0x77000000),
                  Color.Transparent,
                  Color(0xCC000000)
                )
              )
            )
        )

        // Duration Badge (bottom right)
        Box(
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(6.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xDD000000))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = video.duration,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
          )
        }

        // 18+ and Category Badges (bottom left)
        Row(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(6.dp),
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(3.dp))
              .background(CinemaRed)
              .padding(horizontal = 4.dp, vertical = 1.dp)
          ) {
            Text(
              text = "18+",
              color = Color.White,
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold
            )
          }

          if (downloadState == DownloadState.COMPLETED) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(3.dp))
                .background(CinemaGold)
                .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
              Text(
                text = "ОФЛАЙН",
                color = Color.Black,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Top Row: Rating and Like Button
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(Color(0xAA000000))
              .padding(horizontal = 5.dp, vertical = 2.dp)
          ) {
            Text(
              text = "★ ${video.rating}",
              color = CinemaGold,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }

          // Like Button (In-app like)
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(Color(0xAA000000))
              .clickable { onLikeClick() },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Лайкнуть",
              tint = if (isLiked) CinemaRed else Color.White,
              modifier = Modifier.size(16.dp)
            )
          }
        }

        // Center Play Circle
        Box(
          modifier = Modifier
            .align(Alignment.Center)
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0x66000000)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Воспроизвести",
            tint = Color.White,
            modifier = Modifier.size(22.dp)
          )
        }

        // Watch progress indicator bar if in history
        if (watchHistory != null && watchHistory.progressPercent > 0.05f) {
          LinearProgressIndicator(
            progress = { watchHistory.progressPercent },
            color = CinemaRed,
            trackColor = Color.White.copy(alpha = 0.3f),
            modifier = Modifier
              .fillMaxWidth()
              .height(3.dp)
              .align(Alignment.BottomCenter)
          )
        }
      }

      // Title & Subtitle Info
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(10.dp)
      ) {
        Text(
          text = video.title,
          style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          ),
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
          text = video.subtitle,
          style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 11.sp
          ),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "${video.releaseYear} • ${video.category}",
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 10.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
          )

          // Likes display
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Favorite,
              contentDescription = null,
              tint = if (isLiked) CinemaRed else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
              modifier = Modifier.size(11.dp)
            )
            val count = (video.baseLikesCount + (if (isLiked) 1 else 0)) / 1000
            Text(
              text = "${count}K",
              fontSize = 10.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}
