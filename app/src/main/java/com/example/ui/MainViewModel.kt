package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.DownloadState
import com.example.data.model.OfflineItem
import com.example.data.model.VideoItem
import com.example.data.model.WatchHistoryItem
import com.example.data.repository.VideoRepository
import com.example.ui.theme.AppThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class ScreenTab {
  HOME,
  CATALOG,
  FAVORITES,
  DOWNLOADS,
  PROFILE
}

enum class SortOption {
  POPULAR,
  RATING,
  NEWEST,
  DURATION
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

  val repository = VideoRepository(application)

  val themeMode: StateFlow<AppThemeMode> = repository.themeMode
  val videos: StateFlow<List<VideoItem>> = repository.videos
  val likedVideoIds: StateFlow<Set<String>> = repository.likedVideoIds
  val watchHistory: StateFlow<Map<String, WatchHistoryItem>> = repository.watchHistory
  val offlineItems: StateFlow<Map<String, OfflineItem>> = repository.offlineItems

  private val _currentTab = MutableStateFlow(ScreenTab.HOME)
  val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

  private val _activePlayingVideo = MutableStateFlow<VideoItem?>(null)
  val activePlayingVideo: StateFlow<VideoItem?> = _activePlayingVideo.asStateFlow()

  private val _selectedDetailVideo = MutableStateFlow<VideoItem?>(null)
  val selectedDetailVideo: StateFlow<VideoItem?> = _selectedDetailVideo.asStateFlow()

  private val _isAddVideoDialogOpen = MutableStateFlow(false)
  val isAddVideoDialogOpen: StateFlow<Boolean> = _isAddVideoDialogOpen.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedCategory = MutableStateFlow("Все")
  val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

  private val _sortOption = MutableStateFlow(SortOption.POPULAR)
  val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

  // Filtered and sorted videos for catalog
  val catalogVideos: StateFlow<List<VideoItem>> = combine(
    videos,
    _searchQuery,
    _selectedCategory,
    _sortOption
  ) { list, query, category, sort ->
    var result = list

    if (query.isNotBlank()) {
      val q = query.trim().lowercase()
      result = result.filter {
        it.title.lowercase().contains(q) ||
            it.subtitle.lowercase().contains(q) ||
            it.description.lowercase().contains(q) ||
            it.tags.any { tag -> tag.lowercase().contains(q) }
      }
    }

    if (category != "Все") {
      result = result.filter { it.category == category }
    }

    result = when (sort) {
      SortOption.POPULAR -> result.sortedByDescending { it.viewsCount }
      SortOption.RATING -> result.sortedByDescending { it.rating }
      SortOption.NEWEST -> result.sortedByDescending { it.releaseYear }
      SortOption.DURATION -> result.sortedByDescending { it.durationSeconds }
    }

    result
  }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

  // Liked videos list
  val favoriteVideos: StateFlow<List<VideoItem>> = combine(
    videos,
    likedVideoIds
  ) { all, liked ->
    all.filter { liked.contains(it.id) }
  }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

  // Downloaded / offline videos list
  val downloadedVideos: StateFlow<List<Pair<VideoItem, OfflineItem>>> = combine(
    videos,
    offlineItems
  ) { all, off ->
    all.mapNotNull { v ->
      off[v.id]?.let { Pair(v, it) }
    }
  }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

  // Continue watching list
  val continueWatchingVideos: StateFlow<List<Pair<VideoItem, WatchHistoryItem>>> = combine(
    videos,
    watchHistory
  ) { all, hist ->
    val videoMap = all.associateBy { it.id }
    hist.values
      .sortedByDescending { it.lastWatchedTimestamp }
      .mapNotNull { h -> videoMap[h.videoId]?.let { Pair(it, h) } }
  }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

  fun selectTab(tab: ScreenTab) {
    _currentTab.value = tab
  }

  fun openPlayer(video: VideoItem) {
    _activePlayingVideo.value = video
    // Automatically record watch progress beginning
    repository.updateWatchProgress(video.id, 10, video.durationSeconds)
  }

  fun closePlayer() {
    _activePlayingVideo.value = null
  }

  fun showDetail(video: VideoItem) {
    _selectedDetailVideo.value = video
  }

  fun hideDetail() {
    _selectedDetailVideo.value = null
  }

  fun toggleLike(videoId: String) {
    repository.toggleLike(videoId)
  }

  fun toggleDownload(videoId: String) {
    val current = offlineItems.value[videoId]
    if (current != null && current.state == DownloadState.COMPLETED) {
      repository.removeOfflineDownload(videoId)
    } else {
      repository.startDownload(videoId)
    }
  }

  fun openAddVideoDialog() {
    _isAddVideoDialogOpen.value = true
  }

  fun closeAddVideoDialog() {
    _isAddVideoDialogOpen.value = false
  }

  fun addNewVideo(url: String, title: String, category: String, description: String) {
    val created = repository.addVideoByUrl(url, title, category, description)
    _isAddVideoDialogOpen.value = false
    _selectedDetailVideo.value = created
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setSelectedCategory(cat: String) {
    _selectedCategory.value = cat
  }

  fun setSortOption(sort: SortOption) {
    _sortOption.value = sort
  }

  fun setThemeMode(mode: AppThemeMode) {
    repository.setThemeMode(mode)
  }

  fun clearHistory() {
    repository.clearWatchHistory()
  }

  fun clearAllDownloads() {
    repository.clearAllDownloads()
  }
}
