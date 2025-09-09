package nl.jovmit.lyrics.main.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.common.composables.ErrorBanner
import nl.jovmit.lyrics.common.composables.InfoBanner
import nl.jovmit.lyrics.common.composables.MultiLineTextInput
import nl.jovmit.lyrics.common.composables.SingleLineTextInput
import nl.jovmit.lyrics.main.details.SongDetailsScreenState
import nl.jovmit.lyrics.main.details.SongDetailsViewModel
import nl.jovmit.lyrics.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditSongScreen(
  updateSongViewModel: UpdateSongViewModel = koinViewModel(),
  songDetailsViewModel: SongDetailsViewModel = koinViewModel(),
  onSongUpdated: () -> Unit,
  onNavigateUp: () -> Unit
) {
  val songDetailsUiState by songDetailsViewModel.uiState.collectAsStateWithLifecycle()
  val editSongScreenState by updateSongViewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    songDetailsViewModel.loadSong()
  }

  LaunchedEffect(songDetailsUiState) {
    songDetailsUiState.song?.let {
      updateSongViewModel.setSongData(it)
    }
  }

  EditSongScreenContent(
    songDetailsState = songDetailsUiState,
    editSongState = editSongScreenState,
    onUpdateSongTitle = updateSongViewModel::updateSongTitle,
    onUpdateSongPerformer = updateSongViewModel::updateSongPerformer,
    onUpdateSongLyrics = updateSongViewModel::updateSongLyrics,
    onSaveSongEdits = updateSongViewModel::updateSong,
    onErrorBannerDismiss = updateSongViewModel::dismissErrorBanner,
    onSongUpdated = onSongUpdated,
    onNavigateUp = onNavigateUp,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditSongScreenContent(
  songDetailsState: SongDetailsScreenState,
  editSongState: EditSongScreenState,
  onUpdateSongTitle: (String) -> Unit,
  onUpdateSongPerformer: (String) -> Unit,
  onUpdateSongLyrics: (String) -> Unit,
  onSaveSongEdits: () -> Unit,
  onErrorBannerDismiss: () -> Unit,
  onSongUpdated: () -> Unit,
  onNavigateUp: () -> Unit,
) {
  Scaffold(
    containerColor = AppTheme.colorScheme.background,
    topBar = {
      CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
          containerColor = AppTheme.colorScheme.colorPrimary,
          titleContentColor = AppTheme.colorScheme.colorOnPrimary,
          navigationIconContentColor = AppTheme.colorScheme.colorOnPrimary,
          actionIconContentColor = AppTheme.colorScheme.colorOnPrimary
        ),
        navigationIcon = {
          IconButton(
            modifier = Modifier.testTag("navigateUp"),
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
            text = stringResource(R.string.edit),
            style = AppTheme.typography.title
          )
        },
        actions = {
          if (songDetailsState.isLoading || editSongState.isLoading) {
            IconButton(onClick = {}, enabled = false) {
              CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                trackColor = AppTheme.colorScheme.colorSurface.copy(.5f),
                color = AppTheme.colorScheme.background
              )
            }
          } else {
            IconButton(
              modifier = Modifier.testTag("actionDone"),
              onClick = onSaveSongEdits
            ) {
              Icon(
                imageVector = Icons.Default.Done,
                contentDescription = stringResource(R.string.edit)
              )
            }
          }
        }
      )
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      val scrollState = rememberScrollState()
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(scrollState)
          .padding(AppTheme.size.l),
        verticalArrangement = Arrangement.spacedBy(AppTheme.size.s)
      ) {
        val performerFocusRequester = FocusRequester()
        SingleLineTextInput(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("songTitle"),
          value = editSongState.songTitle,
          onValueChanged = onUpdateSongTitle,
          labelValue = stringResource(R.string.songTitle),
          errorLabel = if (editSongState.isEmptySongTitleError) stringResource(R.string.errorEmptySongTitle) else null,
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
            .focusRequester(performerFocusRequester)
            .testTag("songPerformer"),
          value = editSongState.songPerformer,
          onValueChanged = onUpdateSongPerformer,
          labelValue = stringResource(R.string.singerName),
          errorLabel = if (editSongState.isEmptyPerformerNameError) stringResource(R.string.errorEmptySongPerformer) else null,
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
            .focusRequester(lyricsFocusRequester)
            .testTag("songLyrics"),
          value = editSongState.songLyrics,
          onValueChanged = onUpdateSongLyrics,
          labelValue = stringResource(R.string.songLyric),
          errorLabel = if (editSongState.isEmptyLyricsError) stringResource(R.string.errorEmptySongLyrics) else null,
          keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
          ),
          keyboardActions = KeyboardActions(
            onDone = {
              focusManager.clearFocus()
              onSaveSongEdits()
            }
          )
        )
      }
      ErrorBanner(
        modifier = Modifier.fillMaxWidth(),
        visible = editSongState.isErrorSavingSong,
        label = stringResource(R.string.errorUnableToEditSong),
        onDismiss = onErrorBannerDismiss
      )
      InfoBanner(
        modifier = Modifier.fillMaxWidth(),
        visible = editSongState.songUpdated,
        label = stringResource(R.string.success),
        onDismiss = onSongUpdated
      )
    }
  }
}

@PreviewLightDark
@Composable
private fun PreviewEditSongScreen() {
  AppTheme {
    EditSongScreenContent(
      songDetailsState = SongDetailsScreenState(isLoading = true),
      editSongState = EditSongScreenState(),
      onUpdateSongTitle = {},
      onUpdateSongPerformer = {},
      onUpdateSongLyrics = {},
      onSaveSongEdits = {},
      onErrorBannerDismiss = {},
      onSongUpdated = {},
      onNavigateUp = {}
    )
  }
}