package nl.jovmit.lyrics.common.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay
import nl.jovmit.lyrics.theme.AppTheme

@Composable
fun ErrorBanner(
  modifier: Modifier = Modifier,
  visible: Boolean,
  label: String,
  onDismiss: () -> Unit,
) {
  AnimatedVisibility(
    modifier = modifier,
    visible = visible,
    enter = slideInVertically(
      animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    ),
  ) {
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .background(AppTheme.colorScheme.colorError)
        .padding(AppTheme.size.l),
      text = label,
      textAlign = TextAlign.Center,
      color = AppTheme.colorScheme.colorOnError,
      style = AppTheme.typography.body
    )
  }
  LaunchedEffect(visible) {
    if (visible) {
      delay(1000)
      onDismiss()
    }
  }
}