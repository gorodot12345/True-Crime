package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.VideoItem
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaRed

@Composable
fun HeroBillboard(
  featuredVideo: VideoItem,
  isLiked: Boolean,
  onPlayClick: () -> Unit,
  onDetailsClick: () -> Unit,
  onLikeClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(340.dp)
      .testTag("hero_billboard")
  ) {
    // Backdrop Image
    AsyncImage(
      model = ImageRequest.Builder(context)
        .data(R.drawable.savelev_hero_banner_1794503657421)
        .crossfade(true)
        .build(),
      contentDescription = "Главный баннер Savelev TV",
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize()
    )

    // Deep gradient overlays for cinematic mood
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color.Black.copy(alpha = 0.3f),
              Color.Transparent,
              MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
              MaterialTheme.colorScheme.background
            ),
            startY = 0f,
            endY = 900f
          )
        )
    )

    // Content container aligned at the bottom
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp, vertical = 14.dp),
      verticalArrangement = Arrangement.Bottom
    ) {
      // Tags Row
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(CinemaRed)
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = "MINDHUNTER ORIGINAL",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Black,
              letterSpacing = 1.sp,
              fontSize = 10.sp
            ),
            color = Color.White
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color.Black.copy(alpha = 0.6f))
            .padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
          Text(
            text = "4K HDR",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp
            ),
            color = CinemaGold
          )
        }

        Text(
          text = "★ ${featuredVideo.rating}",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = CinemaGold
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Title
      Text(
        text = featuredVideo.title.uppercase(),
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Black,
          fontSize = 20.sp,
          lineHeight = 24.sp
        ),
        color = Color.White,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = featuredVideo.subtitle,
        style = MaterialTheme.typography.bodySmall,
        color = Color.White.copy(alpha = 0.85f),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Action Buttons Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Play Button
        Button(
          onClick = onPlayClick,
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .testTag("hero_play_button"),
          colors = ButtonDefaults.buttonColors(
            containerColor = CinemaRed,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(10.dp)
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Смотреть",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
          )
        }

        // Details Button
        FilledTonalButton(
          onClick = onDetailsClick,
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .testTag("hero_details_button"),
          shape = RoundedCornerShape(10.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "О выпуске",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
          )
        }

        // Like Button
        IconButton(
          onClick = onLikeClick,
          modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
            .testTag("hero_like_button")
        ) {
          Icon(
            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (isLiked) "Лайк поставлен" else "Поставить лайк",
            tint = if (isLiked) CinemaRed else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}
