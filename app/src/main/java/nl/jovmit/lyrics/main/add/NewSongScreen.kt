package nl.jovmit.lyrics.main.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.common.composables.ErrorBanner
import nl.jovmit.lyrics.common.composables.MultiLineTextInput
import nl.jovmit.lyrics.common.composables.SingleLineTextInput
import nl.jovmit.lyrics.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewSongScreen(
  newSongViewModel: NewSongViewModel = koinViewModel(),
  onNavigateUp: () -> Unit,
  onSongSaved: () -> Unit
) {
  val uiState by newSongViewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(uiState.songAdded) {
    if (uiState.songAdded) {
      onSongSaved()
    }
  }

  NewSongScreenContent(
    state = uiState,
    onNavigateUp = onNavigateUp,
    onSaveSong = newSongViewModel::saveSong,
    onUpdateSongTitle = newSongViewModel::updateSongTitle,
    onUpdatePerformer = newSongViewModel::updatePerformerName,
    onUpdateLyrics = newSongViewModel::updateLyrics,
    onErrorBannerDismiss = newSongViewModel::dismissAddSongError
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewSongScreenContent(
  state: NewSongScreenState,
  onNavigateUp: () -> Unit,
  onSaveSong: () -> Unit,
  onUpdateSongTitle: (newValue: String) -> Unit,
  onUpdatePerformer: (newValue: String) -> Unit,
  onUpdateLyrics: (newValue: String) -> Unit,
  onErrorBannerDismiss: () -> Unit
) {
  Scaffold(
    containerColor = AppTheme.colorScheme.background,
    topBar = {
      CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = AppTheme.colorScheme.colorPrimary,
          titleContentColor = AppTheme.colorScheme.colorOnPrimary,
          navigationIconContentColor = AppTheme.colorScheme.colorOnPrimary,
          actionIconContentColor = AppTheme.colorScheme.colorOnPrimary
        ),
        navigationIcon = {
          IconButton(
            modifier = Modifier.testTag("doneButton"),
            onClick = onNavigateUp
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = stringResource(R.string.cdNavigateUp)
            )
          }
        },
        title = {
          Text(
            text = stringResource(R.string.newSong),
            style = AppTheme.typography.title
          )
        },
        actions = {
          IconButton(onClick = onSaveSong) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = stringResource(R.string.cdActionDone)
            )
          }
        }
      )
    }
  ) { paddingValues ->
    Box(
      Modifier
        .fillMaxSize()
        .padding(paddingValues),
    ) {
      val scrollState = rememberScrollState()
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(scrollState)
          .padding(AppTheme.size.l),
        verticalArrangement = Arrangement.spacedBy(AppTheme.size.m)
      ) {
        val performerFocusRequester = FocusRequester()
        SingleLineTextInput(
          modifier = Modifier.fillMaxWidth(),
          value = state.songTitle,
          onValueChanged = onUpdateSongTitle,
          labelValue = stringResource(R.string.songTitle),
          errorLabel = if (state.isEmptySongTitleError) stringResource(R.string.errorEmptySongTitle) else null,
          keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
          ),
          keyboardActions = KeyboardActions(
            onNext = { performerFocusRequester.requestFocus() }
          )
        )
        val lyricsFocusRequester = FocusRequester()
        SingleLineTextInput(
          modifier = Modifier
            .fillMaxWidth()
            .focusRequester(performerFocusRequester),
          value = state.performerName,
          onValueChanged = onUpdatePerformer,
          labelValue = stringResource(R.string.singerName),
          errorLabel = if (state.isEmptyPerformerNameError) stringResource(R.string.errorEmptySongPerformer) else null,
          keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
          ),
          keyboardActions = KeyboardActions(
            onNext = { lyricsFocusRequester.requestFocus() }
          )
        )
        val focusManager = LocalFocusManager.current
        MultiLineTextInput(
          modifier = Modifier
            .fillMaxWidth()
            .focusRequester(lyricsFocusRequester),
          value = state.lyrics,
          onValueChanged = onUpdateLyrics,
          labelValue = stringResource(R.string.songLyric),
          errorLabel = if (state.isEmptyLyricsError) stringResource(R.string.errorEmptySongLyrics) else null,
          keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
          ),
          keyboardActions = KeyboardActions(
            onDone = {
              focusManager.clearFocus()
              onSaveSong()
            }
          )
        )
      }
      ErrorBanner(
        modifier = Modifier.fillMaxWidth(),
        visible = state.errorAddingSong,
        label = stringResource(R.string.errorSavingSong),
        onDismiss = { onErrorBannerDismiss() }
      )
    }
  }
}

@PreviewLightDark
@Composable
private fun PreviewNewSongScreenContent() {
  AppTheme {
    NewSongScreenContent(
      state = NewSongScreenState(songTitle = "8 Miles"),
      onNavigateUp = {},
      onSaveSong = {},
      onUpdateSongTitle = {},
      onUpdatePerformer = {},
      onUpdateLyrics = {},
      onErrorBannerDismiss = {}
    )
  }
}