package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color as AndroidColor
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.DownloadState
import com.example.data.model.OfflineItem
import com.example.data.model.VideoItem
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaRed
import kotlinx.coroutines.delay

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun VideoPlayerView(
  video: VideoItem,
  isLiked: Boolean,
  offlineItem: OfflineItem?,
  onClose: () -> Unit,
  onToggleLike: () -> Unit,
  onToggleDownload: () -> Unit,
  onProgressUpdate: (Int, Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var isFullscreen by remember { mutableStateOf(false) }
  var isOfflineModeActive by remember {
    mutableStateOf(offlineItem?.state == DownloadState.COMPLETED)
  }

  // Offline simulated player controls
  var isPlayingOffline by remember { mutableStateOf(true) }
  var offlinePositionSeconds by remember { mutableIntStateOf(10) }
  var isDescriptionExpanded by remember { mutableStateOf(false) }

  BackHandler {
    if (isFullscreen) {
      isFullscreen = false
    } else {
      onClose()
    }
  }

  // Timer simulation for offline playback
  LaunchedEffect(isOfflineModeActive, isPlayingOffline) {
    if (isOfflineModeActive && isPlayingOffline) {
      while (isPlayingOffline && offlinePositionSeconds < video.durationSeconds) {
        delay(1000)
        offlinePositionSeconds += 1
        onProgressUpdate(offlinePositionSeconds, video.durationSeconds)
      }
    }
  }

  Surface(
    color = MaterialTheme.colorScheme.background,
    modifier = modifier.fillMaxSize()
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {
      // Top Cinema Player Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFF0A0A0E))
          .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onClose,
          modifier = Modifier.testTag("player_close_button")
        ) {
          Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Назад к каталогу",
            tint = Color.White
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
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
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Savelev TV Cinema",
            color = Color.White.copy(alpha = 0.9f),
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
          )
        }

        // Offline mode indicator/toggle
        if (offlineItem?.state == DownloadState.COMPLETED) {
          IconButton(
            onClick = { isOfflineModeActive = !isOfflineModeActive },
            modifier = Modifier.testTag("toggle_offline_stream_button")
          ) {
            Icon(
              imageVector = if (isOfflineModeActive) Icons.Default.WifiOff else Icons.Default.CheckCircle,
              contentDescription = "Офлайн режим",
              tint = if (isOfflineModeActive) CinemaGold else Color.LightGray
            )
          }
        } else {
          Spacer(modifier = Modifier.size(48.dp))
        }
      }

      // Video Display Area
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(16f / 9f)
          .background(Color.Black)
      ) {
        if (isOfflineModeActive) {
          // OFFLINE PLAYER
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(Color(0xFF101018)),
            contentAlignment = Alignment.Center
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center,
              modifier = Modifier.padding(16.dp)
            ) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(CinemaGold.copy(alpha = 0.2f))
                  .padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.WifiOff,
                    contentDescription = null,
                    tint = CinemaGold,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "ОФЛАЙН ВОСПРОИЗВЕДЕНИЕ (БЕЗ ИНТЕРНЕТА)",
                    color = CinemaGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              Text(
                text = video.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1
              )

              Spacer(modifier = Modifier.height(16.dp))

              // Player transport controls
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
              ) {
                IconButton(
                  onClick = {
                    offlinePositionSeconds = (offlinePositionSeconds - 10).coerceAtLeast(0)
                  }
                ) {
                  Icon(
                    imageVector = Icons.Default.Replay10,
                    contentDescription = "-10с",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                  )
                }

                Box(
                  modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(CinemaRed)
                    .clickable { isPlayingOffline = !isPlayingOffline },
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = if (isPlayingOffline) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlayingOffline) "Пауза" else "Играть",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                  )
                }

                IconButton(
                  onClick = {
                    offlinePositionSeconds = (offlinePositionSeconds + 10).coerceAtMost(video.durationSeconds)
                  }
                ) {
                  Icon(
                    imageVector = Icons.Default.Forward10,
                    contentDescription = "+10с",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Time slider
              Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                val curMin = offlinePositionSeconds / 60
                val curSec = offlinePositionSeconds % 60
                val totalMin = video.durationSeconds / 60
                val totalSec = video.durationSeconds % 60

                Text(
                  text = "%02d:%02d".format(curMin, curSec),
                  color = Color.White.copy(alpha = 0.7f),
                  fontSize = 11.sp
                )

                Slider(
                  value = offlinePositionSeconds.toFloat(),
                  onValueChange = { offlinePositionSeconds = it.toInt() },
                  valueRange = 0f..video.durationSeconds.toFloat(),
                  colors = SliderDefaults.colors(
                    thumbColor = CinemaRed,
                    activeTrackColor = CinemaRed,
                    inactiveTrackColor = Color.DarkGray
                  ),
                  modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                )

                Text(
                  text = "%02d:%02d".format(totalMin, totalSec),
                  color = Color.White.copy(alpha = 0.7f),
                  fontSize = 11.sp
                )
              }
            }
          }
        } else {
          // ONLINE YOUTUBE STREAM PLAYER
          AndroidView(
            factory = { ctx ->
              WebView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                  ViewGroup.LayoutParams.MATCH_PARENT,
                  ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(AndroidColor.BLACK)
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.mediaPlaybackRequiresUserGesture = false
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.cacheMode = WebSettings.LOAD_DEFAULT

                webChromeClient = WebChromeClient()
                webViewClient = WebViewClient()

                val html = """
                  <!DOCTYPE html>
                  <html>
                  <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
                    <style>
                      * { margin: 0; padding: 0; box-sizing: border-box; background: #000; }
                      body, html { width: 100%; height: 100%; overflow: hidden; }
                      iframe { width: 100%; height: 100%; border: none; }
                    </style>
                  </head>
                  <body>
                    <iframe 
                      src="https://www.youtube.com/embed/${video.cleanYoutubeId}?autoplay=1&enablejsapi=1&rel=0&modestbranding=1&playsinline=1" 
                      allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" 
                      allowfullscreen>
                    </iframe>
                  </body>
                  </html>
                """.trimIndent()

                loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "UTF-8", null)
              }
            },
            update = { webView ->
              // Updates if video changes
            },
            modifier = Modifier.fillMaxSize()
          )
        }
      }

      // Offline Notice Bar if active
      if (isOfflineModeActive) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2A2300))
            .padding(horizontal = 16.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = CinemaGold,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Воспроизведение из памяти устройства (${offlineItem?.fileSizeMb ?: 420} МБ)",
            color = CinemaGold,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      // Video Info & Details section
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        // Badges Row
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
              text = video.category,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp
            )
          }

          Text(
            text = "★ ${video.rating}",
            color = CinemaGold,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )

          Text(
            text = "• ${video.releaseYear}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
          ),
          color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Subtitle
        Text(
          text = video.subtitle,
          style = MaterialTheme.typography.bodyMedium,
          color = CinemaGold,
          fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Actions Row (In-app Like, Save Offline, Share)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // 1. IN-APP LIKE BUTTON
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .clickable { onToggleLike() }
              .padding(8.dp)
              .testTag("player_like_button")
          ) {
            Icon(
              imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Лайк",
              tint = if (isLiked) CinemaRed else MaterialTheme.colorScheme.onBackground,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            val currentLikes = (video.baseLikesCount + (if (isLiked) 1 else 0)) / 1000
            Text(
              text = if (isLiked) "Нравится" else "${currentLikes}K",
              fontSize = 11.sp,
              fontWeight = if (isLiked) FontWeight.Bold else FontWeight.Normal,
              color = if (isLiked) CinemaRed else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          // 2. OFFLINE DOWNLOAD BUTTON
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .clickable { onToggleDownload() }
              .padding(8.dp)
              .testTag("player_download_button")
          ) {
            when (offlineItem?.state) {
              DownloadState.COMPLETED -> {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = "Сохранено",
                  tint = CinemaGold,
                  modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "Сохранено",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = CinemaGold
                )
              }
              DownloadState.DOWNLOADING -> {
                Icon(
                  imageVector = Icons.Default.Download,
                  contentDescription = "Загрузка",
                  tint = CinemaRed,
                  modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "${offlineItem.progressPercent}%",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = CinemaRed
                )
              }
              else -> {
                Icon(
                  imageVector = Icons.Default.Download,
                  contentDescription = "Скачать офлайн",
                  tint = MaterialTheme.colorScheme.onBackground,
                  modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "Офлайн",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }

          // 3. SHARE BUTTON
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .clickable {
                val sendIntent = Intent().apply {
                  action = Intent.ACTION_SEND
                  putExtra(
                    Intent.EXTRA_TEXT,
                    "Смотри «${video.title}» в онлайн-кинотеатре Savelev TV (создатели Филипп и Арсений)!"
                  )
                  type = "text/plain"
                }
                context.startActivity(Intent.createChooser(sendIntent, "Поделиться фильмом"))
              }
              .padding(8.dp)
              .testTag("player_share_button")
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Поделиться",
              tint = MaterialTheme.colorScheme.onBackground,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Поделиться",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Download progress bar if downloading
        if (offlineItem?.state == DownloadState.DOWNLOADING) {
          LinearProgressIndicator(
            progress = { offlineItem.progressPercent / 100f },
            color = CinemaRed,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
              .fillMaxWidth()
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp))
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Загрузка для просмотра без интернета (${offlineItem.fileSizeMb} МБ)...",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(12.dp))
        }

        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
        Spacer(modifier = Modifier.height(14.dp))

        // Description / Case facts
        Text(
          text = "ОПИСАНИЕ И ДЕЛО",
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

        Spacer(modifier = Modifier.height(10.dp))

        // Case details card
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text(
              text = "Факты расследования:",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
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

        // Chapters / Timecodes
        if (video.chapters.isNotEmpty()) {
          Spacer(modifier = Modifier.height(18.dp))
          Text(
            text = "ГЛАВЫ ВЫПУСКА (${video.chapters.size})",
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
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                  if (isOfflineModeActive) {
                    offlinePositionSeconds = chapter.timestampSeconds
                  }
                }
                .padding(vertical = 8.dp, horizontal = 4.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = chapter.title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
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

        Spacer(modifier = Modifier.height(24.dp))

        // Savelev TV Creators Banner
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(
            containerColor = Color(0xFF191316)
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(CinemaRed),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "TV",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 12.sp
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Savelev TV — Эксклюзив",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color.White
              )
              Text(
                text = "Создатели кинотеатра: Филипп и Арсений",
                fontSize = 11.sp,
                color = CinemaGold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(32.dp))
      }
    }
  }
}
