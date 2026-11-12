package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DownloadState
import com.example.ui.MainViewModel
import com.example.ui.ScreenTab
import com.example.ui.SortOption
import com.example.ui.components.AddVideoDialog
import com.example.ui.components.CinemaTopBar
import com.example.ui.components.VideoPlayerView
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.DownloadsScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.VideoDetailSheet
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaRed
import com.example.ui.theme.SavelevTvTheme

class MainActivity : ComponentActivity() {

  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val themeMode by viewModel.themeMode.collectAsState()

      SavelevTvTheme(themeMode = themeMode) {
        SavelevTvApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun SavelevTvApp(viewModel: MainViewModel) {
  val currentTab by viewModel.currentTab.collectAsState()
  val activePlayingVideo by viewModel.activePlayingVideo.collectAsState()
  val selectedDetailVideo by viewModel.selectedDetailVideo.collectAsState()
  val isAddDialogOpen by viewModel.isAddVideoDialogOpen.collectAsState()

  val videos by viewModel.videos.collectAsState()
  val likedVideoIds by viewModel.likedVideoIds.collectAsState()
  val watchHistory by viewModel.watchHistory.collectAsState()
  val offlineItems by viewModel.offlineItems.collectAsState()
  val themeMode by viewModel.themeMode.collectAsState()

  val catalogVideos by viewModel.catalogVideos.collectAsState()
  val favoriteVideos by viewModel.favoriteVideos.collectAsState()
  val downloadedVideos by viewModel.downloadedVideos.collectAsState()
  val continueWatchingVideos by viewModel.continueWatchingVideos.collectAsState()

  val searchQuery by viewModel.searchQuery.collectAsState()
  val selectedCategory by viewModel.selectedCategory.collectAsState()
  val sortOption by viewModel.sortOption.collectAsState()

  // Map of video ID to DownloadState
  val offlineStateMap = remember(offlineItems) {
    offlineItems.mapValues { it.value.state }
  }

  // Handle system back navigation
  BackHandler(enabled = currentTab != ScreenTab.HOME && activePlayingVideo == null && selectedDetailVideo == null) {
    viewModel.selectTab(ScreenTab.HOME)
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      containerColor = MaterialTheme.colorScheme.background,
      topBar = {
        if (activePlayingVideo == null && selectedDetailVideo == null) {
          CinemaTopBar(
            themeMode = themeMode,
            onCycleTheme = {
              val nextTheme = when (themeMode) {
                AppThemeMode.DARK -> AppThemeMode.AMOLED
                AppThemeMode.AMOLED -> AppThemeMode.LIGHT
                AppThemeMode.LIGHT -> AppThemeMode.DARK
              }
              viewModel.setThemeMode(nextTheme)
            },
            onOpenAddVideo = {
              viewModel.openAddVideoDialog()
            },
            onSearchClick = {
              viewModel.selectTab(ScreenTab.CATALOG)
            }
          )
        }
      },
      bottomBar = {
        if (activePlayingVideo == null && selectedDetailVideo == null) {
          NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier.testTag("bottom_nav_bar")
          ) {
            NavigationBarItem(
              selected = currentTab == ScreenTab.HOME,
              onClick = { viewModel.selectTab(ScreenTab.HOME) },
              icon = { Icon(Icons.Default.Home, contentDescription = "Главная") },
              label = { Text("Главная", fontSize = 11.sp) },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CinemaRed,
                selectedTextColor = CinemaRed,
                indicatorColor = CinemaRed.copy(alpha = 0.15f)
              ),
              modifier = Modifier.testTag("nav_item_home")
            )

            NavigationBarItem(
              selected = currentTab == ScreenTab.CATALOG,
              onClick = { viewModel.selectTab(ScreenTab.CATALOG) },
              icon = { Icon(Icons.Default.Search, contentDescription = "Каталог") },
              label = { Text("Каталог", fontSize = 11.sp) },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CinemaRed,
                selectedTextColor = CinemaRed,
                indicatorColor = CinemaRed.copy(alpha = 0.15f)
              ),
              modifier = Modifier.testTag("nav_item_catalog")
            )

            NavigationBarItem(
              selected = currentTab == ScreenTab.FAVORITES,
              onClick = { viewModel.selectTab(ScreenTab.FAVORITES) },
              icon = {
                if (likedVideoIds.isNotEmpty()) {
                  BadgedBox(
                    badge = {
                      Badge(containerColor = CinemaRed) {
                        Text("${likedVideoIds.size}")
                      }
                    }
                  ) {
                    Icon(Icons.Default.Favorite, contentDescription = "Избранное")
                  }
                } else {
                  Icon(Icons.Default.Favorite, contentDescription = "Избранное")
                }
              },
              label = { Text("Лайки", fontSize = 11.sp) },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CinemaRed,
                selectedTextColor = CinemaRed,
                indicatorColor = CinemaRed.copy(alpha = 0.15f)
              ),
              modifier = Modifier.testTag("nav_item_favorites")
            )

            NavigationBarItem(
              selected = currentTab == ScreenTab.DOWNLOADS,
              onClick = { viewModel.selectTab(ScreenTab.DOWNLOADS) },
              icon = {
                val completedCount = downloadedVideos.count { it.second.state == DownloadState.COMPLETED }
                if (completedCount > 0) {
                  BadgedBox(
                    badge = {
                      Badge(containerColor = CinemaGold) {
                        Text(
                          text = "$completedCount",
                          color = Color.Black
                        )
                      }
                    }
                  ) {
                    Icon(Icons.Default.FileDownload, contentDescription = "Загрузки")
                  }
                } else {
                  Icon(Icons.Default.FileDownload, contentDescription = "Загрузки")
                }
              },
              label = { Text("Офлайн", fontSize = 11.sp) },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CinemaRed,
                selectedTextColor = CinemaRed,
                indicatorColor = CinemaRed.copy(alpha = 0.15f)
              ),
              modifier = Modifier.testTag("nav_item_downloads")
            )

            NavigationBarItem(
              selected = currentTab == ScreenTab.PROFILE,
              onClick = { viewModel.selectTab(ScreenTab.PROFILE) },
              icon = { Icon(Icons.Default.Person, contentDescription = "Профиль") },
              label = { Text("Профиль", fontSize = 11.sp) },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CinemaRed,
                selectedTextColor = CinemaRed,
                indicatorColor = CinemaRed.copy(alpha = 0.15f)
              ),
              modifier = Modifier.testTag("nav_item_profile")
            )
          }
        }
      }
    ) { innerPadding ->
      Crossfade(
        targetState = currentTab,
        label = "screen_crossfade",
        modifier = Modifier.padding(innerPadding)
      ) { tab ->
        when (tab) {
          ScreenTab.HOME -> {
            HomeScreen(
              videos = videos,
              likedVideoIds = likedVideoIds,
              watchHistoryMap = watchHistory,
              offlineStateMap = offlineStateMap,
              continueWatchingList = continueWatchingVideos,
              onVideoClick = { video -> viewModel.openPlayer(video) },
              onLikeClick = { video -> viewModel.toggleLike(video.id) },
              onOpenDetails = { video -> viewModel.showDetail(video) },
              onSeeAllClick = { cat ->
                viewModel.setSelectedCategory(cat)
                viewModel.selectTab(ScreenTab.CATALOG)
              }
            )
          }

          ScreenTab.CATALOG -> {
            CatalogScreen(
              videos = catalogVideos,
              searchQuery = searchQuery,
              selectedCategory = selectedCategory,
              sortOption = sortOption,
              likedVideoIds = likedVideoIds,
              watchHistoryMap = watchHistory,
              offlineStateMap = offlineStateMap,
              onSearchChange = { q -> viewModel.setSearchQuery(q) },
              onCategorySelect = { cat -> viewModel.setSelectedCategory(cat) },
              onSortSelect = { s -> viewModel.setSortOption(s) },
              onVideoClick = { video -> viewModel.showDetail(video) },
              onLikeClick = { video -> viewModel.toggleLike(video.id) }
            )
          }

          ScreenTab.FAVORITES -> {
            FavoritesScreen(
              favoriteVideos = favoriteVideos,
              likedVideoIds = likedVideoIds,
              watchHistoryMap = watchHistory,
              offlineStateMap = offlineStateMap,
              onVideoClick = { video -> viewModel.showDetail(video) },
              onLikeClick = { video -> viewModel.toggleLike(video.id) }
            )
          }

          ScreenTab.DOWNLOADS -> {
            DownloadsScreen(
              downloadedVideos = downloadedVideos,
              onPlayVideo = { video -> viewModel.openPlayer(video) },
              onDeleteDownload = { videoId -> viewModel.toggleDownload(videoId) },
              onClearAll = { viewModel.clearAllDownloads() }
            )
          }

          ScreenTab.PROFILE -> {
            ProfileScreen(
              themeMode = themeMode,
              onThemeChange = { mode -> viewModel.setThemeMode(mode) },
              onClearHistory = { viewModel.clearHistory() },
              onClearDownloads = { viewModel.clearAllDownloads() }
            )
          }
        }
      }
    }

    // Modal Video Detail Sheet
    AnimatedVisibility(
      visible = selectedDetailVideo != null,
      enter = slideInVertically(initialOffsetY = { it }),
      exit = slideOutVertically(targetOffsetY = { it })
    ) {
      selectedDetailVideo?.let { targetVideo ->
        VideoDetailSheet(
          video = targetVideo,
          isLiked = likedVideoIds.contains(targetVideo.id),
          offlineItem = offlineItems[targetVideo.id],
          onClose = { viewModel.hideDetail() },
          onPlayClick = {
            viewModel.hideDetail()
            viewModel.openPlayer(targetVideo)
          },
          onLikeClick = { viewModel.toggleLike(targetVideo.id) },
          onDownloadClick = { viewModel.toggleDownload(targetVideo.id) }
        )
      }
    }

    // Cinema Video Player View Overlay
    AnimatedVisibility(
      visible = activePlayingVideo != null,
      enter = fadeIn(),
      exit = fadeOut()
    ) {
      activePlayingVideo?.let { targetVideo ->
        VideoPlayerView(
          video = targetVideo,
          isLiked = likedVideoIds.contains(targetVideo.id),
          offlineItem = offlineItems[targetVideo.id],
          onClose = { viewModel.closePlayer() },
          onToggleLike = { viewModel.toggleLike(targetVideo.id) },
          onToggleDownload = { viewModel.toggleDownload(targetVideo.id) },
          onProgressUpdate = { pos, total ->
            viewModel.repository.updateWatchProgress(targetVideo.id, pos, total)
          }
        )
      }
    }

    // Add Video Dialog
    if (isAddDialogOpen) {
      AddVideoDialog(
        onDismiss = { viewModel.closeAddVideoDialog() },
        onAddVideo = { url, title, cat, desc ->
          viewModel.addNewVideo(url, title, cat, desc)
        }
      )
    }
  }
}
