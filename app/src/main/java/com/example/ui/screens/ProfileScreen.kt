package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Hd
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaRed

@Composable
fun ProfileScreen(
  themeMode: AppThemeMode,
  onThemeChange: (AppThemeMode) -> Unit,
  onClearHistory: () -> Unit,
  onClearDownloads: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var showClearHistoryDialog by remember { mutableStateOf(false) }
  var showClearDownloadsDialog by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp)
      .testTag("profile_screen")
  ) {
    // App Branding & Creators Card
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      ),
      modifier = Modifier.fillMaxWidth()
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.linearGradient(
              listOf(Color(0xFF220005), Color(0xFF14141E))
            )
          )
          .padding(20.dp)
      ) {
        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                  Brush.linearGradient(listOf(CinemaRed, Color(0xFF88000C)))
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Tv,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(34.dp)
              )
            }

            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "Savelev TV",
                  style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                  color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(CinemaRed)
                    .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = "PRO",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }

              Text(
                text = "Онлайн-кинотеатр MINDHUNTER",
                style = MaterialTheme.typography.bodySmall,
                color = CinemaGold,
                fontWeight = FontWeight.Medium
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Creators recognition block
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0x55000000))
              .padding(12.dp)
          ) {
            Column {
              Text(
                text = "СОЗДАТЕЛИ ПРИЛОЖЕНИЯ",
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Bold,
                color = CinemaGold
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Филипп и Арсений",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
              Text(
                text = "Специальный кинематографический проект для всех ценителей расследований Кости Савельева.",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = Color.White.copy(alpha = 0.8f)
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Theme Customization Section
    Text(
      text = "ОФОРМЛЕНИЕ И ТЕМА",
      style = MaterialTheme.typography.labelMedium.copy(
        letterSpacing = 1.sp,
        fontWeight = FontWeight.Bold
      ),
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(8.dp))

    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      ),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        ThemeOptionRow(
          title = "Темная тема (Cinema Dark)",
          subtitle = "Фирменный нуарный кино-стиль с глубокими тенями",
          icon = Icons.Default.DarkMode,
          selected = themeMode == AppThemeMode.DARK,
          onClick = { onThemeChange(AppThemeMode.DARK) }
        )

        Divider(
          modifier = Modifier.padding(vertical = 4.dp),
          color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )

        ThemeOptionRow(
          title = "AMOLED Black (Абсолютный чёрный)",
          subtitle = "Идеально для OLED-экранов, экономит заряд батареи",
          icon = Icons.Default.Brightness4,
          selected = themeMode == AppThemeMode.AMOLED,
          onClick = { onThemeChange(AppThemeMode.AMOLED) }
        )

        Divider(
          modifier = Modifier.padding(vertical = 4.dp),
          color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )

        ThemeOptionRow(
          title = "Светлая тема",
          subtitle = "Классический светлый интерфейс",
          icon = Icons.Default.LightMode,
          selected = themeMode == AppThemeMode.LIGHT,
          onClick = { onThemeChange(AppThemeMode.LIGHT) }
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // About Kostya Savelev and Mindhunter
    Text(
      text = "О ПРОЕКТЕ MINDHUNTER",
      style = MaterialTheme.typography.labelMedium.copy(
        letterSpacing = 1.sp,
        fontWeight = FontWeight.Bold
      ),
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(8.dp))

    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      ),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Константин Савельев (SAVELEV)",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
          color = CinemaGold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "MINDHUNTER — документальный проект о серийных преступниках, психологии и кинематографе. Каждый выпуск создан как полноценный художественный фильм с уникальной атмосферой и архивными материалами.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 20.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Data Management
    Text(
      text = "УПРАВЛЕНИЕ ДАННЫМИ",
      style = MaterialTheme.typography.labelMedium.copy(
        letterSpacing = 1.sp,
        fontWeight = FontWeight.Bold
      ),
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(8.dp))

    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      ),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { showClearHistoryDialog = true }
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            tint = CinemaRed,
            modifier = Modifier.size(22.dp)
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Очистить историю просмотров",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Сбросить прогресс всех просмотренных серий",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Divider(
          modifier = Modifier.padding(vertical = 4.dp),
          color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { showClearDownloadsDialog = true }
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = CinemaRed,
            modifier = Modifier.size(22.dp)
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Удалить все офлайн-загрузки",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Освободить память устройства от сохранённых видео",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(90.dp))
  }

  // Clear History Dialog
  if (showClearHistoryDialog) {
    AlertDialog(
      onDismissRequest = { showClearHistoryDialog = false },
      title = { Text("Очистить историю?") },
      text = { Text("Все отметки о продолжении просмотра будут удалены.") },
      confirmButton = {
        Button(
          onClick = {
            onClearHistory()
            showClearHistoryDialog = false
            Toast.makeText(context, "История просмотров очищена", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = CinemaRed)
        ) {
          Text("Очистить")
        }
      },
      dismissButton = {
        TextButton(onClick = { showClearHistoryDialog = false }) {
          Text("Отмена")
        }
      }
    )
  }

  // Clear Downloads Dialog
  if (showClearDownloadsDialog) {
    AlertDialog(
      onDismissRequest = { showClearDownloadsDialog = false },
      title = { Text("Удалить все загрузки?") },
      text = { Text("Все сохранённые для офлайн-просмотра видео будут удалены из памяти.") },
      confirmButton = {
        Button(
          onClick = {
            onClearDownloads()
            showClearDownloadsDialog = false
            Toast.makeText(context, "Офлайн-хранилище очищено", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = CinemaRed)
        ) {
          Text("Удалить")
        }
      },
      dismissButton = {
        TextButton(onClick = { showClearDownloadsDialog = false }) {
          Text("Отмена")
        }
      }
    )
  }
}

@Composable
private fun ThemeOptionRow(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  selected: Boolean,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .clickable { onClick() }
      .padding(8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = if (selected) CinemaRed else MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.size(24.dp)
    )
    Spacer(modifier = Modifier.width(12.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
    RadioButton(
      selected = selected,
      onClick = onClick,
      colors = RadioButtonDefaults.colors(
        selectedColor = CinemaRed
      )
    )
  }
}
