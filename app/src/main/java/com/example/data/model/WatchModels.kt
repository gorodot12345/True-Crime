package com.example.data.model

data class WatchHistoryItem(
  val videoId: String,
  val positionSeconds: Int,
  val totalDurationSeconds: Int,
  val lastWatchedTimestamp: Long = System.currentTimeMillis()
) {
  val progressPercent: Float
    get() = if (totalDurationSeconds > 0) {
      (positionSeconds.toFloat() / totalDurationSeconds.toFloat()).coerceIn(0f, 1f)
    } else 0f
}

enum class DownloadState {
  NOT_DOWNLOADED,
  DOWNLOADING,
  COMPLETED
}

data class OfflineItem(
  val videoId: String,
  val state: DownloadState,
  val progressPercent: Int,
  val fileSizeMb: Int,
  val downloadedTimestamp: Long = System.currentTimeMillis()
)
