package com.example.data.model

data class Chapter(
  val title: String,
  val timestampSeconds: Int,
  val formattedTime: String
)

data class VideoItem(
  val id: String,
  val youtubeId: String,
  val title: String,
  val subtitle: String,
  val duration: String,
  val durationSeconds: Int,
  val releaseYear: String,
  val category: String,
  val ageRating: String = "18+",
  val rating: Double = 9.2,
  val description: String,
  val caseDetails: String,
  val tags: List<String>,
  val thumbnailUrl: String,
  val backdropUrl: String? = null,
  val chapters: List<Chapter> = emptyList(),
  val isCustomAdded: Boolean = false,
  val viewsCount: Long = 450000L,
  val baseLikesCount: Long = 34000L
) {
  val cleanYoutubeId: String
    get() {
      // Handles urls like https://www.youtube.com/watch?v=XXXX, https://youtu.be/XXXX, or plain XXXX
      return when {
        youtubeId.contains("v=") -> youtubeId.substringAfter("v=").substringBefore("&")
        youtubeId.contains("youtu.be/") -> youtubeId.substringAfter("youtu.be/").substringBefore("?")
        youtubeId.contains("embed/") -> youtubeId.substringAfter("embed/").substringBefore("?")
        else -> youtubeId.trim()
      }
    }

  val effectiveThumbnailUrl: String
    get() {
      if (thumbnailUrl.isNotBlank()) return thumbnailUrl
      val id = cleanYoutubeId
      return if (id.isNotBlank()) "https://img.youtube.com/vi/$id/maxresdefault.jpg" else ""
    }
}
