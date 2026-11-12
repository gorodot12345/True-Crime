package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaRed

@Composable
fun CinemaTopBar(
  themeMode: AppThemeMode,
  onCycleTheme: () -> Unit,
  onOpenAddVideo: () -> Unit,
  onSearchClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    color = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Logo & Creator credits
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.testTag("app_branding_header")
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(
                Brush.linearGradient(
                  listOf(CinemaRed, Color(0xFF8A000A))
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Tv,
              contentDescription = "Savelev TV Logo",
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "SAVELEV",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.Black,
                  letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
              )
              Spacer(modifier = Modifier.width(4.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(CinemaRed)
                  .padding(horizontal = 4.dp, vertical = 1.dp)
              ) {
                Text(
                  text = "TV",
                  color = Color.White,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
            Text(
              text = "MINDHUNTER • Филипп и Арсений",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium
              ),
              color = CinemaGold
            )
          }
        }

        // Action Buttons
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          // Add video button
          IconButton(
            onClick = onOpenAddVideo,
            modifier = Modifier.testTag("add_video_button")
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "Добавить видео",
              tint = MaterialTheme.colorScheme.onBackground
            )
          }

          // Search button
          IconButton(
            onClick = onSearchClick,
            modifier = Modifier.testTag("search_icon_button")
          ) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Поиск",
              tint = MaterialTheme.colorScheme.onBackground
            )
          }

          // Theme switcher
          IconButton(
            onClick = onCycleTheme,
            modifier = Modifier.testTag("theme_switch_button")
          ) {
            Icon(
              imageVector = Icons.Default.Brightness4,
              contentDescription = "Сменить тему",
              tint = if (themeMode == AppThemeMode.LIGHT) CinemaRed else CinemaGold
            )
          }
        }
      }
    }
  }
}
