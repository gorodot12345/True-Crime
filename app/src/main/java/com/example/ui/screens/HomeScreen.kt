package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DownloadState
import com.example.data.model.VideoItem
import com.example.data.model.WatchHistoryItem
import com.example.ui.components.HeroBillboard
import com.example.ui.components.VideoHorizontalRail
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaRed

@Composable
fun HomeScreen(
  videos: List<VideoItem>,
  likedVideoIds: Set<String>,
  watchHistoryMap: Map<String, WatchHistoryItem>,
  offlineStateMap: Map<String, DownloadState>,
  continueWatchingList: List<Pair<VideoItem, WatchHistoryItem>>,
  onVideoClick: (VideoItem) -> Unit,
  onLikeClick: (VideoItem) -> Unit,
  onOpenDetails: (VideoItem) -> Unit,
  onSeeAllClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val featuredVideo = videos.firstOrNull { it.id == "sav_02" } ?: videos.firstOrNull()

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .testTag("home_screen")
  ) {
    // Featured Hero Billboard
    if (featuredVideo != null) {
      HeroBillboard(
        featuredVideo = featuredVideo,
        isLiked = likedVideoIds.contains(featuredVideo.id),
        onPlayClick = { onVideoClick(featuredVideo) },
        onDetailsClick = { onOpenDetails(featuredVideo) },
        onLikeClick = { onLikeClick(featuredVideo) }
      )
    }

    // Continue Watching Section
    if (continueWatchingList.isNotEmpty()) {
      Spacer(modifier = Modifier.height(10.dp))
      VideoHorizontalRail(
        title = "Продолжить просмотр",
        subtitle = "Серии, которые вы начали смотреть",
        videos = continueWatchingList.map { it.first },
        likedVideoIds = likedVideoIds,
        watchHistoryMap = watchHistoryMap,
        offlineStateMap = offlineStateMap,
        onVideoClick = onVideoClick,
        onLikeClick = onLikeClick,
        onSeeAllClick = null
      )
    }

    // Category: Новые расследования
    val newInvestigations = videos.filter { it.releaseYear == "2024" || it.isCustomAdded }
    VideoHorizontalRail(
      title = "🔥 Новые расследования MINDHUNTER",
      subtitle = "Свежие выпуски от Кости Савельева",
      videos = if (newInvestigations.isNotEmpty()) newInvestigations else videos.take(5),
      likedVideoIds = likedVideoIds,
      watchHistoryMap = watchHistoryMap,
      offlineStateMap = offlineStateMap,
      onVideoClick = onVideoClick,
      onLikeClick = onLikeClick,
      onSeeAllClick = { onSeeAllClick("Новые выпуски") }
    )

    // Category: Серийные убийцы и маньяки
    val maniacsList = videos.filter { it.category == "Маньяки" }
    VideoHorizontalRail(
      title = "🩸 Маньяки и серийные дела",
      subtitle = "Архивы уголовного розыска и допросы",
      videos = maniacsList,
      likedVideoIds = likedVideoIds,
      watchHistoryMap = watchHistoryMap,
      offlineStateMap = offlineStateMap,
      onVideoClick = onVideoClick,
      onLikeClick = onLikeClick,
      onSeeAllClick = { onSeeAllClick("Маньяки") }
    )

    // Category: Кинематограф и психоанализ
    val psychologyList = videos.filter { it.category == "Психология и кино" || it.category == "Расследования" }
    VideoHorizontalRail(
      title = "🧠 Психология, кино и профайлинг",
      subtitle = "Связь кинематографа и реальных преступлений",
      videos = psychologyList,
      likedVideoIds = likedVideoIds,
      watchHistoryMap = watchHistoryMap,
      offlineStateMap = offlineStateMap,
      onVideoClick = onVideoClick,
      onLikeClick = onLikeClick,
      onSeeAllClick = { onSeeAllClick("Психология и кино") }
    )

    // Creators tribute card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      ),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "ОНЛАЙН-КИНОТЕАТР SAVELEV TV",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          ),
          color = CinemaRed
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Все выпуски проекта MINDHUNTER Кости Савельева в высоком качестве, с историей просмотров, лайками и офлайн-режимом.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Создатели приложения: Филипп и Арсений",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = CinemaGold
        )
      }
    }

    Spacer(modifier = Modifier.height(80.dp))
  }
}
