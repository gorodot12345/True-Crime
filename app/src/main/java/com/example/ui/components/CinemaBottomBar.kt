package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.DownloadDone
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ScreenTab
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaRed

@Composable
fun CinemaBottomBar(
  currentTab: ScreenTab,
  likedCount: Int,
  downloadedCount: Int,
  onTabSelected: (ScreenTab) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    containerColor = MaterialTheme.colorScheme.surface,
    contentColor = MaterialTheme.colorScheme.onSurface,
    tonalElevation = 8.dp,
    modifier = modifier.testTag("cinema_bottom_navigation")
  ) {
    // 1. Home
    NavigationBarItem(
      selected = currentTab == ScreenTab.HOME,
      onClick = { onTabSelected(ScreenTab.HOME) },
      icon = {
        Icon(
          imageVector = if (currentTab == ScreenTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
          contentDescription = "Главная",
          modifier = Modifier.size(24.dp)
        )
      },
      label = {
        Text(
          text = "Главная",
          fontSize = 11.sp,
          fontWeight = if (currentTab == ScreenTab.HOME) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color.White,
        selectedTextColor = CinemaRed,
        indicatorColor = CinemaRed,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
      ),
      modifier = Modifier.testTag("nav_tab_home")
    )

    // 2. Catalog
    NavigationBarItem(
      selected = currentTab == ScreenTab.CATALOG,
      onClick = { onTabSelected(ScreenTab.CATALOG) },
      icon = {
        Icon(
          imageVector = if (currentTab == ScreenTab.CATALOG) Icons.Filled.VideoLibrary else Icons.Outlined.VideoLibrary,
          contentDescription = "Каталог",
          modifier = Modifier.size(24.dp)
        )
      },
      label = {
        Text(
          text = "Каталог",
          fontSize = 11.sp,
          fontWeight = if (currentTab == ScreenTab.CATALOG) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color.White,
        selectedTextColor = CinemaRed,
        indicatorColor = CinemaRed,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
      ),
      modifier = Modifier.testTag("nav_tab_catalog")
    )

    // 3. Favorites / Likes
    NavigationBarItem(
      selected = currentTab == ScreenTab.FAVORITES,
      onClick = { onTabSelected(ScreenTab.FAVORITES) },
      icon = {
        if (likedCount > 0) {
          BadgedBox(
            badge = {
              Badge(
                containerColor = CinemaRed,
                contentColor = Color.White
              ) {
                Text(text = likedCount.toString(), fontSize = 10.sp)
              }
            }
          ) {
            Icon(
              imageVector = if (currentTab == ScreenTab.FAVORITES) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
              contentDescription = "Избранное",
              modifier = Modifier.size(24.dp)
            )
          }
        } else {
          Icon(
            imageVector = if (currentTab == ScreenTab.FAVORITES) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Избранное",
            modifier = Modifier.size(24.dp)
          )
        }
      },
      label = {
        Text(
          text = "Избранное",
          fontSize = 11.sp,
          fontWeight = if (currentTab == ScreenTab.FAVORITES) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color.White,
        selectedTextColor = CinemaRed,
        indicatorColor = CinemaRed,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
      ),
      modifier = Modifier.testTag("nav_tab_favorites")
    )

    // 4. Downloads / Offline
    NavigationBarItem(
      selected = currentTab == ScreenTab.DOWNLOADS,
      onClick = { onTabSelected(ScreenTab.DOWNLOADS) },
      icon = {
        if (downloadedCount > 0) {
          BadgedBox(
            badge = {
              Badge(
                containerColor = CinemaGold,
                contentColor = Color.Black
              ) {
                Text(text = downloadedCount.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold)
              }
            }
          ) {
            Icon(
              imageVector = if (currentTab == ScreenTab.DOWNLOADS) Icons.Filled.DownloadDone else Icons.Outlined.DownloadDone,
              contentDescription = "Офлайн",
              modifier = Modifier.size(24.dp)
            )
          }
        } else {
          Icon(
            imageVector = if (currentTab == ScreenTab.DOWNLOADS) Icons.Filled.DownloadDone else Icons.Outlined.DownloadDone,
            contentDescription = "Офлайн",
            modifier = Modifier.size(24.dp)
          )
        }
      },
      label = {
        Text(
          text = "Офлайн",
          fontSize = 11.sp,
          fontWeight = if (currentTab == ScreenTab.DOWNLOADS) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color.White,
        selectedTextColor = CinemaRed,
        indicatorColor = CinemaRed,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
      ),
      modifier = Modifier.testTag("nav_tab_downloads")
    )

    // 5. Profile
    NavigationBarItem(
      selected = currentTab == ScreenTab.PROFILE,
      onClick = { onTabSelected(ScreenTab.PROFILE) },
      icon = {
        Icon(
          imageVector = if (currentTab == ScreenTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
          contentDescription = "Профиль",
          modifier = Modifier.size(24.dp)
        )
      },
      label = {
        Text(
          text = "Профиль",
          fontSize = 11.sp,
          fontWeight = if (currentTab == ScreenTab.PROFILE) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color.White,
        selectedTextColor = CinemaRed,
        indicatorColor = CinemaRed,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
      ),
      modifier = Modifier.testTag("nav_tab_profile")
    )
  }
}
