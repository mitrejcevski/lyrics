package nl.jovmit.lyrics.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import nl.jovmit.lyrics.theme.AppTheme

@Composable
fun PrimaryButton(
  modifier: Modifier = Modifier,
  labelValue: String,
  onClick: () -> Unit,
  enabled: Boolean = true,
  showLoading: Boolean = false
) {
  Button(
    modifier = modifier
      .heightIn(56.dp),
    onClick = onClick,
    enabled = enabled && !showLoading,
    colors = ButtonDefaults.buttonColors(
      containerColor = AppTheme.colorScheme.colorPrimary,
      disabledContainerColor = AppTheme.colorScheme.colorPrimary.copy(alpha = .3f),
      disabledContentColor = Color.White.copy(alpha = .6f)
    )
  ) {
    if (showLoading) {
      CircularProgressIndicator(
        color = Color.White
      )
    } else {
      Text(
        text = labelValue,
        style = AppTheme.typography.buttonLabel
      )
    }
  }
}

@Composable
@PreviewLightDark
private fun PreviewPrimaryButton() {
  AppTheme {
    Column(
      verticalArrangement = Arrangement.spacedBy(AppTheme.size.s)
    ) {
      PrimaryButton(
        modifier = Modifier.fillMaxWidth(),
        labelValue = "Create new user",
        onClick = {}
      )
      PrimaryButton(
        modifier = Modifier.fillMaxWidth(),
        labelValue = "Create new user",
        onClick = {},
        showLoading = true
      )
    }
  }
}