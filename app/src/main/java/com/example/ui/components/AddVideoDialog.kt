package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVideoDialog(
  onDismiss: () -> Unit,
  onAddVideo: (url: String, title: String, category: String, description: String) -> Unit
) {
  var urlInput by remember { mutableStateOf("") }
  var titleInput by remember { mutableStateOf("") }
  var descriptionInput by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("Новые выпуски") }

  val categories = listOf("Новые выпуски", "Маньяки", "Расследования", "Психология и кино")

  val extractedId by remember(urlInput) {
    derivedStateOf {
      val trimmed = urlInput.trim()
      when {
        trimmed.contains("v=") -> trimmed.substringAfter("v=").substringBefore("&")
        trimmed.contains("youtu.be/") -> trimmed.substringAfter("youtu.be/").substringBefore("?")
        trimmed.contains("embed/") -> trimmed.substringAfter("embed/").substringBefore("?")
        trimmed.length in 8..15 && !trimmed.contains("/") -> trimmed
        else -> ""
      }
    }
  }

  val previewThumbUrl = if (extractedId.isNotBlank()) {
    "https://img.youtube.com/vi/$extractedId/hqdefault.jpg"
  } else null

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("add_video_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Movie,
              contentDescription = null,
              tint = CinemaRed,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Добавить видео",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Закрыть",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Text(
          text = "Вставьте ссылку на ролик Кости Савельева. Обложка и превью добавятся автоматически!",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        // URL Input
        OutlinedTextField(
          value = urlInput,
          onValueChange = { urlInput = it },
          label = { Text("Ссылка на YouTube (или ID видео)") },
          placeholder = { Text("https://www.youtube.com/watch?v=...") },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Link,
              contentDescription = null,
              tint = CinemaRed
            )
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("add_video_url_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Thumbnail Preview
        if (previewThumbUrl != null) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(16f / 9f)
              .clip(RoundedCornerShape(10.dp))
              .background(Color.Black)
          ) {
            AsyncImage(
              model = previewThumbUrl,
              contentDescription = "Превью обложки",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxWidth()
            )
            Box(
              modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(6.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xCC000000))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "Обложка загружена",
                color = CinemaGold,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }
          Spacer(modifier = Modifier.height(10.dp))
        }

        // Title Input
        OutlinedTextField(
          value = titleInput,
          onValueChange = { titleInput = it },
          label = { Text("Название выпуска") },
          placeholder = { Text("Например: MINDHUNTER — Новое расследование") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("add_video_title_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Category Chips
        Text(
          text = "Категория:",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          categories.forEach { cat ->
            FilterChip(
              selected = selectedCategory == cat,
              onClick = { selectedCategory = cat },
              label = { Text(cat, fontSize = 11.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = CinemaRed,
                selectedLabelColor = Color.White
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Description
        OutlinedTextField(
          value = descriptionInput,
          onValueChange = { descriptionInput = it },
          label = { Text("Описание и детали дела") },
          placeholder = { Text("Краткий синопсис выпуска...") },
          maxLines = 3,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("add_video_desc_input")
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End,
          verticalAlignment = Alignment.CenterVertically
        ) {
          TextButton(onClick = onDismiss) {
            Text("Отмена", color = MaterialTheme.colorScheme.onSurfaceVariant)
          }

          Spacer(modifier = Modifier.width(8.dp))

          Button(
            onClick = {
              if (urlInput.isNotBlank()) {
                val effectiveTitle = titleInput.ifBlank { "MINDHUNTER выпуск Кости Савельева" }
                onAddVideo(urlInput, effectiveTitle, selectedCategory, descriptionInput)
              }
            },
            enabled = urlInput.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
              containerColor = CinemaRed,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("submit_add_video_button")
          ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Добавить в кинотеатр")
          }
        }
      }
    }
  }
}
