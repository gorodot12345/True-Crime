package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.DownloadState
import com.example.data.model.OfflineItem
import com.example.data.model.VideoItem
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaRed

@Composable
fun VideoDetailSheet(
  video: VideoItem,
  isLiked: Boolean,
  offlineItem: OfflineItem?,
  onClose: () -> Unit,
  onPlayClick: () -> Unit,
  onLikeClick: () -> Unit,
  onDownloadClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  BackHandler {
    onClose()
  }

  Surface(
    color = MaterialTheme.colorScheme.background,
    modifier = modifier.fillMaxSize()
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .testTag("video_detail_sheet")
    ) {
      // Backdrop with gradient overlay
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(16f / 10f)
      ) {
        AsyncImage(
          model = video.effectiveThumbnailUrl,
          contentDescription = video.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )

        // Gradient
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color(0x88000000),
                  Color.Transparent,
                  MaterialTheme.colorScheme.background
                )
              )
            )
        )

        // Back Button
        IconButton(
          onClick = onClose,
          modifier = Modifier
            .padding(16.dp)
            .align(Alignment.TopStart)
            .clip(CircleShape)
            .background(Color(0x77000000))
        ) {
          Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Назад",
            tint = Color.White
          )
        }

        // Floating Play Button on poster
        Box(
          modifier = Modifier
            .align(Alignment.Center)
            .size(64.dp)
            .clip(CircleShape)
            .background(CinemaRed)
            .clickable { onPlayClick() },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Смотреть",
            tint = Color.White,
            modifier = Modifier.size(36.dp)
          )
        }
      }

      // Details Content
      Column(modifier = Modifier.padding(16.dp)) {
        // Badges
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(CinemaRed)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = "MINDHUNTER",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            )
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = video.category,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontSize = 11.sp
            )
          }

          Text(
            text = "★ ${video.rating}",
            color = CinemaGold,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )

          Text(
            text = "• ${video.duration}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp
          )

          Text(
            text = "• 18+",
            color = CinemaRed,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Title
        Text(
          text = video.title,
          style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = video.subtitle,
          style = MaterialTheme.typography.bodyMedium,
          color = CinemaGold,
          fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Play Button
        Button(
          onClick = onPlayClick,
          colors = ButtonDefaults.buttonColors(
            containerColor = CinemaRed,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("detail_play_button")
        ) {
          Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(24.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Смотреть выпуск", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action Buttons Row (Like, Download, Share)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Like Button
          OutlinedButton(
            onClick = onLikeClick,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(
              imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = null,
              tint = if (isLiked) CinemaRed else MaterialTheme.colorScheme.onBackground,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (isLiked) "Нравится" else "Лайк",
              color = if (isLiked) CinemaRed else MaterialTheme.colorScheme.onBackground
            )
          }

          // Offline Download Button
          OutlinedButton(
            onClick = onDownloadClick,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(
              imageVector = if (offlineItem?.state == DownloadState.COMPLETED) {
                Icons.Default.CheckCircle
              } else Icons.Default.Download,
              contentDescription = null,
              tint = if (offlineItem?.state == DownloadState.COMPLETED) CinemaGold else MaterialTheme.colorScheme.onBackground,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = when (offlineItem?.state) {
                DownloadState.COMPLETED -> "Офлайн ✓"
                DownloadState.DOWNLOADING -> "${offlineItem.progressPercent}%"
                else -> "Скачать"
              },
              color = if (offlineItem?.state == DownloadState.COMPLETED) CinemaGold else MaterialTheme.colorScheme.onBackground
            )
          }

          // Share Button
          OutlinedButton(
            onClick = {
              val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                  Intent.EXTRA_TEXT,
                  "Смотри «${video.title}» в онлайн-кинотеатре Savelev TV (создатели Филипп и Арсений)!"
                )
                type = "text/plain"
              }
              context.startActivity(Intent.createChooser(sendIntent, "Поделиться фильмом"))
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onBackground,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Репост", color = MaterialTheme.colorScheme.onBackground)
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Synopsis
        Text(
          text = "О ВЫПУСКЕ",
          style = MaterialTheme.typography.labelMedium.copy(
            letterSpacing = 1.sp,
            fontWeight = FontWeight.Bold
          ),
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = video.description,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurface,
          lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Investigation Details Card
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Хроника и материалы следствия:",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = CinemaGold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = video.caseDetails,
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        // Chapters
        if (video.chapters.isNotEmpty()) {
          Spacer(modifier = Modifier.height(18.dp))
          Text(
            text = "ГЛАВЫ И ТАЙМКОДЫ",
            style = MaterialTheme.typography.labelMedium.copy(
              letterSpacing = 1.sp,
              fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(8.dp))

          video.chapters.forEach { chapter ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = chapter.title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
              )
              Text(
                text = chapter.formattedTime,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = CinemaRed
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(80.dp))
      }
    }
  }
}
