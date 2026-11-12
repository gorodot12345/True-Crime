package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.DownloadState
import com.example.data.repository.VideoRepository
import com.example.ui.theme.AppThemeMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app name string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Savelev TV", appName)
  }

  @Test
  fun `video repository loads initial mindhunter episodes`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = VideoRepository(context)

    val videos = repo.videos.value
    assertTrue("Videos catalog should not be empty", videos.isNotEmpty())

    val pichushkin = videos.find { it.title.contains("Пичушкин") }
    assertNotNull("Pichushkin episode should exist", pichushkin)
    assertEquals("18+", pichushkin?.ageRating)
  }

  @Test
  fun `like and unlike updates state in repository`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = VideoRepository(context)

    val videoId = "sav_01"
    assertFalse(repo.likedVideoIds.value.contains(videoId))

    repo.toggleLike(videoId)
    assertTrue(repo.likedVideoIds.value.contains(videoId))

    repo.toggleLike(videoId)
    assertFalse(repo.likedVideoIds.value.contains(videoId))
  }

  @Test
  fun `theme mode switching works properly`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = VideoRepository(context)

    repo.setThemeMode(AppThemeMode.AMOLED)
    assertEquals(AppThemeMode.AMOLED, repo.themeMode.value)

    repo.setThemeMode(AppThemeMode.LIGHT)
    assertEquals(AppThemeMode.LIGHT, repo.themeMode.value)

    repo.setThemeMode(AppThemeMode.DARK)
    assertEquals(AppThemeMode.DARK, repo.themeMode.value)
  }
}
