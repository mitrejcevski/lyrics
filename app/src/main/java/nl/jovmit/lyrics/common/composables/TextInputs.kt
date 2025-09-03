package nl.jovmit.lyrics.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import nl.jovmit.lyrics.theme.AppTheme

@Composable
fun SingleLineTextInput(
  modifier: Modifier = Modifier,
  value: String,
  onValueChanged: (newValue: String) -> Unit,
  labelValue: String = "",
  errorLabel: String? = null,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default
) {
  CoreTextInput(
    modifier = modifier,
    value = value,
    onValueChanged = onValueChanged,
    labelValue = labelValue,
    isSingleLine = true,
    errorLabel = errorLabel,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
  )
}

@Composable
fun MultiLineTextInput(
  modifier: Modifier = Modifier,
  value: String,
  onValueChanged: (newValue: String) -> Unit,
  labelValue: String = "",
  errorLabel: String? = null,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default
) {
  CoreTextInput(
    modifier = modifier,
    value = value,
    onValueChanged = onValueChanged,
    labelValue = labelValue,
    errorLabel = errorLabel,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
  )
}

@Composable
private fun CoreTextInput(
  modifier: Modifier = Modifier,
  value: String,
  onValueChanged: (newValue: String) -> Unit,
  labelValue: String = "",
  isSingleLine: Boolean = false,
  errorLabel: String?,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default
) {
  OutlinedTextField(
    modifier = modifier,
    value = value,
    onValueChange = onValueChanged,
    singleLine = isSingleLine,
    isError = errorLabel?.isNotBlank() == true,
    label = {
      if (value.isBlank()) {
        Text(
          text = labelValue,
          style = AppTheme.typography.body
        )
      }
    },
    supportingText = {
      errorLabel?.let {
        Text(
          text = it,
          style = AppTheme.typography.description
        )
      }
    },
    colors = OutlinedTextFieldDefaults.colors(
      unfocusedBorderColor = Color.Transparent,
      focusedBorderColor = Color.Transparent,
      unfocusedContainerColor = AppTheme.colorScheme.colorSurface.copy(alpha = .5f),
      focusedContainerColor = AppTheme.colorScheme.colorSurface,
      unfocusedLabelColor = AppTheme.colorScheme.colorOnSurface.copy(alpha = .5f),
      focusedLabelColor = AppTheme.colorScheme.colorOnSurface.copy(alpha = .5f),
      unfocusedTextColor = AppTheme.colorScheme.colorOnSurface,
      focusedTextColor = AppTheme.colorScheme.colorOnSurface,
      errorTextColor = AppTheme.colorScheme.colorOnSurface,
      errorContainerColor = AppTheme.colorScheme.colorSurface.copy(alpha = .5f)
    ),
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions
  )
}

@Composable
@PreviewLightDark
private fun PreviewSingleLineTextInput() {
  AppTheme {
    Column(
      modifier = Modifier
        .background(AppTheme.colorScheme.background)
        .padding(AppTheme.size.l),
      verticalArrangement = Arrangement.spacedBy(AppTheme.size.s)
    ) {
      SingleLineTextInput(
        modifier = Modifier.fillMaxWidth(),
        labelValue = "Song title",
        value = "",
        onValueChanged = {},
        errorLabel = "Please enter song title"
      )
      SingleLineTextInput(
        modifier = Modifier.fillMaxWidth(),
        labelValue = "Singer",
        value = "Michael Jackson",
        onValueChanged = {},
      )
      MultiLineTextInput(
        modifier = Modifier.fillMaxWidth(),
        value = "I'm unstoppable,\n" +
          "I'm a Porsche with no brakes,\n" +
          "I'm invincible,\n" +
          "Yeah, I win every single game!",
        onValueChanged = {}
      )
    }
  }
}