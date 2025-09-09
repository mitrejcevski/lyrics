package nl.jovmit.lyrics.common.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.theme.AppTheme

@Composable
fun OverflowMenu(
  label: String,
  onAction: () -> Unit,
  iconColor: Color = AppTheme.colorScheme.background,
  actionBackgroundColor: Color = AppTheme.colorScheme.background,
  actionTextColor: Color = AppTheme.colorScheme.colorOnBackground
) {
  var showMenu by remember { mutableStateOf(false) }
  IconButton(
    modifier = Modifier.testTag("overflowAction"),
    onClick = { showMenu = !showMenu }
  ) {
    Icon(
      imageVector = Icons.Default.MoreVert,
      contentDescription = stringResource(R.string.cdActionMore),
      tint = iconColor
    )
  }
  DropdownMenu(
    containerColor = actionBackgroundColor,
    expanded = showMenu,
    onDismissRequest = { showMenu = false }
  ) {
    DropdownMenuItem(
      onClick = {
        showMenu = false
        onAction()
      },
      text = {
        Text(
          text = label,
          color = actionTextColor
        )
      }
    )
  }
}