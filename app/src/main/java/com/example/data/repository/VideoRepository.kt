package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.Chapter
import com.example.data.model.DownloadState
import com.example.data.model.OfflineItem
import com.example.data.model.VideoItem
import com.example.data.model.WatchHistoryItem
import com.example.ui.theme.AppThemeMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class VideoRepository(private val context: Context) {

  private val prefs: SharedPreferences =
    context.getSharedPreferences("savelev_tv_prefs", Context.MODE_PRIVATE)

  private val scope = CoroutineScope(Dispatchers.IO)

  private val _videos = MutableStateFlow<List<VideoItem>>(emptyList())
  val videos: StateFlow<List<VideoItem>> = _videos.asStateFlow()

  private val _likedVideoIds = MutableStateFlow<Set<String>>(emptySet())
  val likedVideoIds: StateFlow<Set<String>> = _likedVideoIds.asStateFlow()

  private val _watchHistory = MutableStateFlow<Map<String, WatchHistoryItem>>(emptyMap())
  val watchHistory: StateFlow<Map<String, WatchHistoryItem>> = _watchHistory.asStateFlow()

  private val _offlineItems = MutableStateFlow<Map<String, OfflineItem>>(emptyMap())
  val offlineItems: StateFlow<Map<String, OfflineItem>> = _offlineItems.asStateFlow()

  private val _themeMode = MutableStateFlow(AppThemeMode.DARK)
  val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

  init {
    loadPreferences()
    loadInitialVideos()
  }

  private fun loadPreferences() {
    // Likes
    val likesSet = prefs.getStringSet("liked_video_ids", emptySet()) ?: emptySet()
    _likedVideoIds.value = likesSet

    // Theme
    val themeStr = prefs.getString("theme_mode", AppThemeMode.DARK.name) ?: AppThemeMode.DARK.name
    _themeMode.value = try {
      AppThemeMode.valueOf(themeStr)
    } catch (_: Exception) {
      AppThemeMode.DARK
    }

    // Watch history
    val historyJson = prefs.getString("watch_history_json", null)
    if (!historyJson.isNullOrBlank()) {
      try {
        val map = mutableMapOf<String, WatchHistoryItem>()
        val jsonArray = JSONArray(historyJson)
        for (i in 0 until jsonArray.length()) {
          val obj = jsonArray.getJSONObject(i)
          val id = obj.getString("videoId")
          val pos = obj.getInt("positionSeconds")
          val total = obj.getInt("totalDurationSeconds")
          val time = obj.optLong("timestamp", System.currentTimeMillis())
          map[id] = WatchHistoryItem(id, pos, total, time)
        }
        _watchHistory.value = map
      } catch (_: Exception) {}
    }

    // Offline items
    val offlineJson = prefs.getString("offline_items_json", null)
    if (!offlineJson.isNullOrBlank()) {
      try {
        val map = mutableMapOf<String, OfflineItem>()
        val jsonArray = JSONArray(offlineJson)
        for (i in 0 until jsonArray.length()) {
          val obj = jsonArray.getJSONObject(i)
          val id = obj.getString("videoId")
          val state = DownloadState.valueOf(obj.getString("state"))
          val progress = obj.getInt("progress")
          val size = obj.getInt("fileSizeMb")
          val time = obj.optLong("timestamp", System.currentTimeMillis())
          map[id] = OfflineItem(id, state, progress, size, time)
        }
        _offlineItems.value = map
      } catch (_: Exception) {}
    }
  }

  private fun loadInitialVideos() {
    val defaultList = listOf(
      VideoItem(
        id = "sav_01",
        youtubeId = "dQw4w9WgXcQ", // Representative Kostya Savelev Mindhunter episode
        title = "Переходный возраст: Молоточники",
        subtitle = "Дело иркутских подростков-убийц",
        duration = "1ч 18мин",
        durationSeconds = 4680,
        releaseYear = "2023",
        category = "Маньяки",
        ageRating = "18+",
        rating = 9.8,
        description = "Масштабный документальный фильм-расследование о самом страшном деле Иркутского Академгородка. Как двое школьников устроили террор целого микрорайона, вдохновляясь маньяками, и почему никто не мог поймать их долгие месяцы.",
        caseDetails = "Место: Иркутск, Академгородок • Фигуранты: Артём Ануфриев, Никита Лыткин • Хронология: 2010–2011 гг.",
        tags = listOf("MINDHUNTER", "Иркутск", "Молоточники", "Тру-крайм", "Психология"),
        thumbnailUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
        chapters = listOf(
          Chapter("Введение: Тихое место Академгородка", 0, "00:00"),
          Chapter("Первое нападение и молчание", 720, "12:00"),
          Chapter("Паника в городе и ложные следы", 1850, "30:50"),
          Chapter("Фатальная видеозапись и задержание", 3100, "51:40"),
          Chapter("Суд, мотивы и приговор", 4100, "1:08:20")
        ),
        viewsCount = 1250000L,
        baseLikesCount = 94200L
      ),
      VideoItem(
        id = "sav_02",
        youtubeId = "dQw4w9WgXcQ",
        title = "Битцевский маньяк | Пичушкин",
        subtitle = "Шахматная доска и 64 клетки безумия",
        duration = "1ч 45мин",
        durationSeconds = 6300,
        releaseYear = "2023",
        category = "Маньяки",
        ageRating = "18+",
        rating = 9.9,
        description = "Финал первого сезона MINDHUNTER. Глубокое погружение в психологию Александра Пичушкина, убивавшего в Битцевском парке Москвы. Разбор допросов, воспоминания следователей и анализ шахматной карты жертв.",
        caseDetails = "Место: Москва, Битцевский лесопарк • Фигурант: Александр Пичушкин • Период: 1992–2006 гг.",
        tags = listOf("MINDHUNTER", "Битцевский", "Пичушкин", "Москва", "Психоанализ"),
        thumbnailUrl = "https://images.unsplash.com/photo-1448375240586-882707db888b?w=800&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1473448912268-2022ce9509d8?w=1200&auto=format&fit=crop&q=80",
        chapters = listOf(
          Chapter("Битцевский лес: Парк ужаса", 0, "00:00"),
          Chapter("Первая жертва 1992 года", 900, "15:00"),
          Chapter("Шахматная доска и закрытый колодец", 2100, "35:00"),
          Chapter("Записка в кармане: Главная ошибка", 4200, "1:10:00"),
          Chapter("Судебный процесс века", 5700, "1:35:00")
        ),
        viewsCount = 2100000L,
        baseLikesCount = 168000L
      ),
      VideoItem(
        id = "sav_03",
        youtubeId = "dQw4w9WgXcQ",
        title = "357: Квартира потрошителя | Спесивцев",
        subtitle = "Кошмар в Новокузнецке",
        duration = "1ч 32мин",
        durationSeconds = 5520,
        releaseYear = "2023",
        category = "Маньяки",
        ageRating = "18+",
        rating = 9.7,
        description = "Одно из самых мрачных и засекреченных дел 90-х. Квартира номер 357 на улице Пионерской в Новокузнецке. Как Александр Спесивцев и его мать годами скрывали жуткую тайну за железной дверью.",
        caseDetails = "Место: Новокузнецк • Фигуранты: Александр и Людмила Спесивцевы • Год: 1996",
        tags = listOf("MINDHUNTER", "Спесивцев", "Архивы 90-х", "Следствие"),
        thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200&auto=format&fit=crop&q=80",
        chapters = listOf(
          Chapter("Трубы отопления: Прорыв в подъезде", 0, "00:00"),
          Chapter("Вскрытие квартиры №357", 840, "14:00"),
          Chapter("Дневники маньяка", 2200, "36:40"),
          Chapter("Побег и задержание на крыше", 4100, "1:08:20")
        ),
        viewsCount = 1890000L,
        baseLikesCount = 142000L
      ),
      VideoItem(
        id = "sav_04",
        youtubeId = "dQw4w9WgXcQ",
        title = "The Ripper Policeman | Чуплинский",
        subtitle = "Милиционер-оборотень из Новосибирска",
        duration = "1ч 10мин",
        durationSeconds = 4200,
        releaseYear = "2024",
        category = "Расследования",
        ageRating = "18+",
        rating = 9.5,
        description = "Новосибирский маньяк, скрывавшийся под погонами старшего сержанта вневедомственной охраны. Уникальный архив допросов и генетическая экспертиза, которая раскрыла дело спустя два десятилетия.",
        caseDetails = "Место: Новосибирск • Фигурант: Евгений Чуплинский • Период: 1998–2005 гг.",
        tags = listOf("MINDHUNTER", "Чуплинский", "ДНК", "Милиция", "Следствие"),
        thumbnailUrl = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=800&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1473448912268-2022ce9509d8?w=1200&auto=format&fit=crop&q=80",
        viewsCount = 950000L,
        baseLikesCount = 76000L
      ),
      VideoItem(
        id = "sav_05",
        youtubeId = "dQw4w9WgXcQ",
        title = "Вожатый: Анатолий Сливко",
        subtitle = "Заслуженный учитель и тайный клуб «Чергид»",
        duration = "1ч 26мин",
        durationSeconds = 5160,
        releaseYear = "2024",
        category = "Маньяки",
        ageRating = "18+",
        rating = 9.6,
        description = "Уважаемый наставник молодежи, депутат горсовета, кавалер орденов и создатель туристического клуба. Как Анатолий Сливко вел двойную жизнь на глазах у всего города более двадцати лет.",
        caseDetails = "Место: Невинномысск, Ставропольский край • Фигурант: Анатолий Сливко • Годы: 1964–1985",
        tags = listOf("MINDHUNTER", "Сливко", "Чергид", "СССР", "Скрытые тайны"),
        thumbnailUrl = "https://images.unsplash.com/photo-1478760329108-5c3ed9d495a0?w=800&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200&auto=format&fit=crop&q=80",
        viewsCount = 1430000L,
        baseLikesCount = 112000L
      ),
      VideoItem(
        id = "sav_06",
        youtubeId = "dQw4w9WgXcQ",
        title = "Вымышленные маньяки: От кино к реальности",
        subtitle = "Настоящий детектив, Пила, Семь и реальные дела",
        duration = "58мин",
        durationSeconds = 3480,
        releaseYear = "2024",
        category = "Психология и кино",
        ageRating = "18+",
        rating = 9.8,
        description = "Специальный кинематографический выпуск Кости Савельева. Разбор культовых фильмов Финчера, сериала True Detective и франшизы Saw через призму реальных уголовных дел и профайлинга ФБР.",
        caseDetails = "Тема: Киноязык триллеров, профайлинг и реальные дела Джона Дугласа",
        tags = listOf("MINDHUNTER", "Кино", "True Detective", "Финчер", "Кинематограф"),
        thumbnailUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=800&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
        viewsCount = 890000L,
        baseLikesCount = 85000L
      ),
      VideoItem(
        id = "sav_07",
        youtubeId = "dQw4w9WgXcQ",
        title = "Фишер: Сергей Головкин",
        subtitle = "Подвал в Одинцовском районе",
        duration = "1ч 20мин",
        durationSeconds = 4800,
        releaseYear = "2023",
        category = "Маньяки",
        ageRating = "18+",
        rating = 9.7,
        description = "Последний приговорённый к высшей мере наказания в новейшей истории России. Зоотехник Московского конного завода и его тайный подземный бункер под гаражом.",
        caseDetails = "Место: Московская область • Фигурант: Сергей Головкин (Фишер) • Годы: 1986–1992",
        tags = listOf("MINDHUNTER", "Фишер", "Головкин", "Одинцово", "Архив"),
        thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1473448912268-2022ce9509d8?w=1200&auto=format&fit=crop&q=80",
        viewsCount = 1670000L,
        baseLikesCount = 131000L
      ),
      VideoItem(
        id = "sav_08",
        youtubeId = "dQw4w9WgXcQ",
        title = "Ангарский маньяк: Михаил Попков",
        subtitle = "Самый массовый серийный преступник в истории",
        duration = "1ч 40мин",
        durationSeconds = 6000,
        releaseYear = "2024",
        category = "Маньяки",
        ageRating = "18+",
        rating = 9.9,
        description = "Милицейский «УАЗик» на ночной трассе. Более 80 доказанных эпизодов. Беспрецедентная работа следственной группы под руководством Евгения Карчевского и полная психологическая реконструкция личности.",
        caseDetails = "Место: Ангарск, Иркутская область • Фигурант: Михаил Попков • Период: 1992–2010",
        tags = listOf("MINDHUNTER", "Попков", "Ангарск", "Карчевский", "Следствие"),
        thumbnailUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=1200&auto=format&fit=crop&q=80",
        viewsCount = 2300000L,
        baseLikesCount = 195000L
      ),
      VideoItem(
        id = "sav_09",
        youtubeId = "dQw4w9WgXcQ",
        title = "Василий Кулик: Иркутский монстр",
        subtitle = "Врач скорой помощи с темной тайной",
        duration = "1ч 05мин",
        durationSeconds = 3900,
        releaseYear = "2023",
        category = "Расследования",
        ageRating = "18+",
        rating = 9.4,
        description = "История врача выездной бригады скорой помощи, который в середине 1980-х годов терроризировал жителей Иркутска. Сложные судебно-психиатрические экспертизы института Сербского.",
        caseDetails = "Место: Иркутск • Фигурант: Василий Кулик • Годы: 1984–1986",
        tags = listOf("MINDHUNTER", "Кулик", "Иркутск", "Медицина", "Психиатрия"),
        thumbnailUrl = "https://images.unsplash.com/photo-1473448912268-2022ce9509d8?w=800&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1448375240586-882707db888b?w=1200&auto=format&fit=crop&q=80",
        viewsCount = 980000L,
        baseLikesCount = 74000L
      ),
      VideoItem(
        id = "sav_10",
        youtubeId = "dQw4w9WgXcQ",
        title = "Американский психопат: Тед Банди",
        subtitle = "Харизма как инструмент манипуляции",
        duration = "1ч 15мин",
        durationSeconds = 4500,
        releaseYear = "2024",
        category = "Психология и кино",
        ageRating = "18+",
        rating = 9.5,
        description = "Анализ феномена Теда Банди. Человек с внешностью кинозвезды и юридическим образованием, изменивший всю криминалистику США. Рождение термина 'серийный убийца' в отделе поведенческого анализа ФБР.",
        caseDetails = "Место: США • Фигурант: Тед Банди • Годы: 1974–1978",
        tags = listOf("MINDHUNTER", "Тед Банди", "ФБР", "Профайлинг", "США"),
        thumbnailUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=800&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
        viewsCount = 1150000L,
        baseLikesCount = 98000L
      )
    )

    // Load custom user-added videos from storage if any
    val customJson = prefs.getString("custom_added_videos", null)
    val combined = defaultList.toMutableList()
    if (!customJson.isNullOrBlank()) {
      try {
        val array = JSONArray(customJson)
        for (i in 0 until array.length()) {
          val obj = array.getJSONObject(i)
          val item = VideoItem(
            id = obj.getString("id"),
            youtubeId = obj.getString("youtubeId"),
            title = obj.getString("title"),
            subtitle = obj.optString("subtitle", "Пользовательский выпуск MINDHUNTER"),
            duration = obj.optString("duration", "45мин"),
            durationSeconds = obj.optInt("durationSeconds", 2700),
            releaseYear = obj.optString("releaseYear", "2024"),
            category = obj.optString("category", "Новые выпуски"),
            ageRating = obj.optString("ageRating", "18+"),
            rating = obj.optDouble("rating", 9.5),
            description = obj.optString("description", "Выпуск добавлен в коллекцию Savelev TV."),
            caseDetails = obj.optString("caseDetails", "Авторский материал Кости Савельева"),
            tags = listOf("MINDHUNTER", "Savelev TV", "Костя Савельев"),
            thumbnailUrl = obj.optString("thumbnailUrl", ""),
            backdropUrl = if (obj.has("backdropUrl")) obj.getString("backdropUrl") else null,
            isCustomAdded = true,
            viewsCount = 100000L,
            baseLikesCount = 8900L
          )
          combined.add(0, item)
        }
      } catch (_: Exception) {}
    }

    _videos.value = combined
  }

  // --- Like functionality (In-app like, distinct from YouTube) ---
  fun toggleLike(videoId: String) {
    val current = _likedVideoIds.value.toMutableSet()
    if (current.contains(videoId)) {
      current.remove(videoId)
    } else {
      current.add(videoId)
    }
    _likedVideoIds.value = current
    prefs.edit().putStringSet("liked_video_ids", current).apply()
  }

  fun isLiked(videoId: String): Boolean {
    return _likedVideoIds.value.contains(videoId)
  }

  // --- Watch History ---
  fun updateWatchProgress(videoId: String, positionSeconds: Int, totalSeconds: Int) {
    val current = _watchHistory.value.toMutableMap()
    current[videoId] = WatchHistoryItem(
      videoId = videoId,
      positionSeconds = positionSeconds,
      totalDurationSeconds = totalSeconds,
      lastWatchedTimestamp = System.currentTimeMillis()
    )
    _watchHistory.value = current

    scope.launch {
      val array = JSONArray()
      current.values.forEach { item ->
        val obj = JSONObject()
        obj.put("videoId", item.videoId)
        obj.put("positionSeconds", item.positionSeconds)
        obj.put("totalDurationSeconds", item.totalDurationSeconds)
        obj.put("timestamp", item.lastWatchedTimestamp)
        array.put(obj)
      }
      prefs.edit().putString("watch_history_json", array.toString()).apply()
    }
  }

  fun clearWatchHistory() {
    _watchHistory.value = emptyMap()
    prefs.edit().remove("watch_history_json").apply()
  }

  // --- Offline Video Saving & Management ---
  fun startDownload(videoId: String) {
    val current = _offlineItems.value.toMutableMap()
    current[videoId] = OfflineItem(
      videoId = videoId,
      state = DownloadState.DOWNLOADING,
      progressPercent = 10,
      fileSizeMb = (380..520).random()
    )
    _offlineItems.value = current

    scope.launch {
      // Simulate realistic cinematic download progress
      for (p in listOf(25, 50, 75, 95, 100)) {
        delay(600)
        val inFlight = _offlineItems.value.toMutableMap()
        val item = inFlight[videoId]
        if (item != null && item.state == DownloadState.DOWNLOADING) {
          inFlight[videoId] = item.copy(
            progressPercent = p,
            state = if (p >= 100) DownloadState.COMPLETED else DownloadState.DOWNLOADING
          )
          _offlineItems.value = inFlight
        }
      }
      saveOfflineItemsToDisk()
    }
  }

  fun removeOfflineDownload(videoId: String) {
    val current = _offlineItems.value.toMutableMap()
    current.remove(videoId)
    _offlineItems.value = current
    saveOfflineItemsToDisk()
  }

  fun clearAllDownloads() {
    _offlineItems.value = emptyMap()
    saveOfflineItemsToDisk()
  }

  private fun saveOfflineItemsToDisk() {
    scope.launch {
      val array = JSONArray()
      _offlineItems.value.values.forEach { item ->
        val obj = JSONObject()
        obj.put("videoId", item.videoId)
        obj.put("state", item.state.name)
        obj.put("progress", item.progressPercent)
        obj.put("fileSizeMb", item.fileSizeMb)
        obj.put("timestamp", item.downloadedTimestamp)
        array.put(obj)
      }
      prefs.edit().putString("offline_items_json", array.toString()).apply()
    }
  }

  // --- Add new video automatically by link / title ---
  fun addVideoByUrl(
    urlOrId: String,
    title: String,
    category: String,
    description: String
  ): VideoItem {
    val cleanId = when {
      urlOrId.contains("v=") -> urlOrId.substringAfter("v=").substringBefore("&")
      urlOrId.contains("youtu.be/") -> urlOrId.substringAfter("youtu.be/").substringBefore("?")
      urlOrId.contains("embed/") -> urlOrId.substringAfter("embed/").substringBefore("?")
      else -> urlOrId.trim()
    }

    val newVideo = VideoItem(
      id = "custom_" + System.currentTimeMillis(),
      youtubeId = cleanId,
      title = title.ifBlank { "MINDHUNTER: Новый выпуск Кости Савельева" },
      subtitle = "Авторский проект Кости Савельева",
      duration = "1ч 15мин",
      durationSeconds = 4500,
      releaseYear = "2024",
      category = category.ifBlank { "Новые выпуски" },
      ageRating = "18+",
      rating = 9.8,
      description = description.ifBlank { "Новое криминальное расследование и психологический портрет от создателей MINDHUNTER." },
      caseDetails = "Проект Кости Савельева • Savelev TV (Создатели: Филипп и Арсений)",
      tags = listOf("MINDHUNTER", "Костя Савельев", "Savelev TV"),
      thumbnailUrl = "https://img.youtube.com/vi/$cleanId/maxresdefault.jpg",
      isCustomAdded = true,
      viewsCount = 150000L,
      baseLikesCount = 12000L
    )

    val updated = listOf(newVideo) + _videos.value
    _videos.value = updated

    scope.launch {
      val customItems = updated.filter { it.isCustomAdded }
      val array = JSONArray()
      customItems.forEach { v ->
        val obj = JSONObject()
        obj.put("id", v.id)
        obj.put("youtubeId", v.youtubeId)
        obj.put("title", v.title)
        obj.put("subtitle", v.subtitle)
        obj.put("duration", v.duration)
        obj.put("durationSeconds", v.durationSeconds)
        obj.put("releaseYear", v.releaseYear)
        obj.put("category", v.category)
        obj.put("ageRating", v.ageRating)
        obj.put("rating", v.rating)
        obj.put("description", v.description)
        obj.put("caseDetails", v.caseDetails)
        obj.put("thumbnailUrl", v.thumbnailUrl)
        array.put(obj)
      }
      prefs.edit().putString("custom_added_videos", array.toString()).apply()
    }

    return newVideo
  }

  // --- Theme mode preference ---
  fun setThemeMode(mode: AppThemeMode) {
    _themeMode.value = mode
    prefs.edit().putString("theme_mode", mode.name).apply()
  }
}
