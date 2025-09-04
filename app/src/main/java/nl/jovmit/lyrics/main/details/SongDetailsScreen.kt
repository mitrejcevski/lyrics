package nl.jovmit.lyrics.main.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.common.composables.ErrorBanner
import nl.jovmit.lyrics.common.composables.OverflowMenu
import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongId
import nl.jovmit.lyrics.main.data.song.SongLyrics
import nl.jovmit.lyrics.main.data.song.SongPerformer
import nl.jovmit.lyrics.main.data.song.SongTitle
import nl.jovmit.lyrics.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SongDetailsScreen(
  viewModel: SongDetailsViewModel = koinViewModel(),
  onNavigateUp: () -> Unit,
  onEditSong: (songId: String) -> Unit,
  onSongDeleted: () -> Unit,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    viewModel.loadSong()
  }

  LaunchedEffect(uiState.isSongDeleted) {
    if (uiState.isSongDeleted) {
      onSongDeleted()
    }
  }

  SongDetailsScreenContent(
    state = uiState,
    onNavigateUp = onNavigateUp,
    onEditSong = onEditSong,
    onDismissSongNotFoundError = viewModel::dismissSongNotFoundError,
    onDismissSongDeletingError = viewModel::dismissSongDeletingError,
    onDeleteSong = viewModel::deleteSong
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongDetailsScreenContent(
  state: SongDetailsScreenState,
  onNavigateUp: () -> Unit,
  onEditSong: (songId: String) -> Unit,
  onDismissSongNotFoundError: () -> Unit,
  onDismissSongDeletingError: () -> Unit,
  onDeleteSong: () -> Unit
) {
  val scope = rememberCoroutineScope()
  val snackBarHostState = remember { SnackbarHostState() }
  Scaffold(
    containerColor = AppTheme.colorScheme.background,
    snackbarHost = {
      SnackbarHost(hostState = snackBarHostState) { data ->
        Snackbar(
          snackbarData = data,
          containerColor = AppTheme.colorScheme.colorSurface,
          actionColor = AppTheme.colorScheme.colorPrimary,
          contentColor = AppTheme.colorScheme.colorOnSurface
        )
      }
    },
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
            text = stringResource(R.string.songDetails),
            style = AppTheme.typography.title
          )
        },
        actions = {
          IconButton(
            onClick = { onEditSong(state.song?.songId?.value ?: "") },
            enabled = state.song != null
          ) {
            Icon(
              imageVector = Icons.Default.Edit,
              contentDescription = stringResource(R.string.edit)
            )
          }
          val deleteSongConfirmation = stringResource(R.string.deleteSongPrompt)
          val deleteAction = stringResource(R.string.delete)
          OverflowMenu(
            label = stringResource(R.string.delete),
            onAction = {
              scope.launch {
                val result = snackBarHostState.showSnackbar(
                  message = deleteSongConfirmation,
                  actionLabel = deleteAction,
                  duration = SnackbarDuration.Short
                )
                when (result) {
                  SnackbarResult.ActionPerformed -> {
                    onDeleteSong()
                  }

                  SnackbarResult.Dismissed -> {}
                }
              }
            }
          )
        }
      )
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      val scroll = rememberScrollState()
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(scroll)
          .padding(AppTheme.size.l),
        verticalArrangement = Arrangement.spacedBy(AppTheme.size.l)
      ) {
        Column(
          Modifier
            .fillMaxWidth()
            .clip(AppTheme.shape.rounded)
            .background(AppTheme.colorScheme.colorSurface.copy(.5f))
            .padding(AppTheme.size.m),
          verticalArrangement = Arrangement.spacedBy(AppTheme.size.m)
        ) {
          Text(
            text = state.song?.songTitle?.value ?: stringResource(R.string.songTitle),
            style = AppTheme.typography.labelLarge,
            color = AppTheme.colorScheme.colorOnSurface
          )
          Text(
            text = state.song?.songPerformer?.name ?: stringResource(R.string.singerName),
            style = AppTheme.typography.labelMedium,
            color = AppTheme.colorScheme.colorOnSurface.copy(alpha = .5f)
          )
        }
        Text(
          modifier = Modifier
            .fillMaxWidth()
            .clip(AppTheme.shape.rounded)
            .background(AppTheme.colorScheme.colorSurface.copy(.5f))
            .padding(AppTheme.size.m),
          text = state.song?.songLyric?.lyrics ?: stringResource(R.string.songLyric),
          style = AppTheme.typography.bodyDetails,
          color = AppTheme.colorScheme.colorOnSurface
        )
      }
      if (state.isLoading) {
        CircularProgressIndicator(
          modifier = Modifier
            .align(Alignment.TopCenter)
            .clip(CircleShape)
            .padding(top = 16.dp),
          color = AppTheme.colorScheme.colorPrimary,
        )
      }
      ErrorBanner(
        modifier = Modifier.fillMaxWidth(),
        visible = state.isSongNotFoundError,
        label = stringResource(R.string.errorLoadingSong),
        onDismiss = onDismissSongNotFoundError
      )
      ErrorBanner(
        modifier = Modifier.fillMaxWidth(),
        visible = state.isSongDeletingError,
        label = stringResource(R.string.errorDeletingSong),
        onDismiss = onDismissSongDeletingError
      )
    }
  }
}

@PreviewLightDark
@Composable
private fun PreviewSongDetailsScreen() {
  AppTheme {
    SongDetailsScreenContent(
      state = SongDetailsScreenState(
        song = Song(
          SongId("3"),
          SongTitle("I'm on my way"),
          SongPerformer("Alan Walker & Sabrina Carpenter"),
          SongLyrics(
            """
          I'm sorry but, don't wanna talk
          I need a moment before I go, it's nothing personal
          I draw the blinds, they don't need to see me cry
          'Cause even if they understand, they don't understand
          
          So then, when I'm finished
          I'm all 'bout my business and ready to save the world
          I'm taking my misery, make it my bitch
          Can't be everyone's favorite girl
          
          So take aim and fire away
          I've never been so wide awake
          No, nobody but me can keep me safe
          And I'm on my way
          
          The blood moon is on the rise
          The fire burning in my eyes
          No, nobody but me can keep me safe
          And I'm on my way""".trimIndent()
          )
        )
      ),
      onNavigateUp = {},
      onEditSong = {},
      onDismissSongDeletingError = {},
      onDismissSongNotFoundError = {},
      onDeleteSong = {}
    )
  }
}