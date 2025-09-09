package nl.jovmit.lyrics.main.overview

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.common.composables.ErrorBanner
import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongId
import nl.jovmit.lyrics.main.data.song.SongLyrics
import nl.jovmit.lyrics.main.data.song.SongPerformer
import nl.jovmit.lyrics.main.data.song.SongTitle
import nl.jovmit.lyrics.main.preferences.UserPreferencesViewModel
import nl.jovmit.lyrics.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SongsOverviewScreen(
  viewModel: SongsViewModel = koinViewModel(),
  userPreferencesViewModel: UserPreferencesViewModel = koinViewModel(),
  onAddNewSong: () -> Unit,
  onLoggedOut: () -> Unit,
  onOpenSongDetails: (song: Song) -> Unit
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    viewModel.fetchSongs()
  }

  SongsOverviewScreenContent(
    state = uiState,
    onAddNewSong = onAddNewSong,
    onRefresh = viewModel::fetchSongs,
    onLogout = {
      userPreferencesViewModel.clearLoggedInUser()
      onLoggedOut()
    },
    onSearchTermUpdated = viewModel::updateSearchQuery,
    onSearch = viewModel::performSearch,
    onSongClick = onOpenSongDetails,
    onDismissLoadingSongsError = viewModel::dismissSongsLoadingError,
    onDismissSearchingSongsError = viewModel::dismissSongsSearchingError
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongsOverviewScreenContent(
  state: SongsOverviewScreenState,
  onAddNewSong: () -> Unit,
  onRefresh: () -> Unit,
  onLogout: () -> Unit,
  onSearchTermUpdated: (newTerm: String) -> Unit,
  onSearch: () -> Unit,
  onSongClick: (song: Song) -> Unit,
  onDismissLoadingSongsError: () -> Unit,
  onDismissSearchingSongsError: () -> Unit
) {
  Scaffold(
    containerColor = AppTheme.colorScheme.background,
    topBar = {
      SongsSearchBar(
        state = state,
        onQueryUpdate = onSearchTermUpdated,
        onSearch = onSearch,
        onSongSelected = onSongClick,
        onLogout = onLogout,
        onDismissSearchingSongsError = onDismissSearchingSongsError
      )
    },
    floatingActionButton = {
      ExtendedFloatingActionButton(
        modifier = Modifier.testTag("newSong"),
        containerColor = AppTheme.colorScheme.colorPrimary,
        contentColor = AppTheme.colorScheme.colorOnPrimary,
        onClick = onAddNewSong,
        icon = { Icon(Icons.Default.Add, contentDescription = stringResource(R.string.newSong)) },
        text = {
          Text(
            text = stringResource(R.string.newSong),
            style = AppTheme.typography.buttonLabel
          )
        }
      )
    }
  ) { paddingValues ->
    val pullRefreshState = rememberPullToRefreshState()
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .pullToRefresh(
          state = pullRefreshState,
          isRefreshing = state.isLoading,
          onRefresh = onRefresh
        ),
    ) {
      SongsList(
        modifier = Modifier.fillMaxSize(),
        songs = state.songs,
        onSongSelected = onSongClick
      )
      ErrorBanner(
        modifier = Modifier.fillMaxWidth(),
        visible = state.isErrorLoadingSongs,
        label = stringResource(R.string.errorFetchingSongs),
        onDismiss = { onDismissLoadingSongsError() }
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongsSearchBar(
  state: SongsOverviewScreenState,
  onQueryUpdate: (newTerm: String) -> Unit,
  onSearch: () -> Unit,
  onSongSelected: (Song) -> Unit,
  onLogout: () -> Unit,
  onDismissSearchingSongsError: () -> Unit
) {
  var isSearchExpanded by remember { mutableStateOf(false) }
  val searchPadding by animateDpAsState(
    targetValue = if (isSearchExpanded) 0.dp else AppTheme.size.m,
    label = "searchPadding",
    animationSpec = spring()
  )
  SearchBar(
    modifier = Modifier
      .fillMaxWidth()
      .padding(searchPadding),
    shape = AppTheme.shape.circular,
    colors = SearchBarDefaults.colors(
      containerColor = AppTheme.colorScheme.colorSurface.copy(alpha = .5f),
      dividerColor = AppTheme.colorScheme.colorOnSurface.copy(alpha = .1f),
    ),
    inputField = {
      SearchBarDefaults.InputField(
        modifier = Modifier.testTag("searchText"),
        query = state.searchQuery,
        onQueryChange = onQueryUpdate,
        onSearch = { onSearch() },
        colors = TextFieldDefaults.colors(
          cursorColor = AppTheme.colorScheme.colorOnBackground,
          focusedTextColor = AppTheme.colorScheme.colorOnBackground,
          unfocusedTextColor = AppTheme.colorScheme.colorOnBackground.copy(.5f),
          focusedPlaceholderColor = AppTheme.colorScheme.colorOnBackground.copy(.5f),
          unfocusedPlaceholderColor = AppTheme.colorScheme.colorOnBackground.copy(.5f)
        ),
        leadingIcon = {
          IconButton(
            modifier = Modifier.testTag(
              if (isSearchExpanded) "navigateUp" else "search"
            ),
            onClick = {
              if (isSearchExpanded) {
                onQueryUpdate("")
                onSearch()
              }
              isSearchExpanded = !isSearchExpanded
            }
          ) {
            val icon = if (isSearchExpanded) {
              Icons.AutoMirrored.Filled.ArrowBack
            } else {
              Icons.Default.Search
            }
            val contentDescription = if (isSearchExpanded) {
              stringResource(R.string.cdNavigateUp)
            } else {
              stringResource(R.string.search)
            }
            Icon(
              imageVector = icon,
              contentDescription = contentDescription,
              tint = AppTheme.colorScheme.colorOnSurface
            )
          }
        },
        placeholder = {
          Text(
            text = stringResource(id = R.string.search),
            style = AppTheme.typography.buttonLabel,
          )
        },
        trailingIcon = {
          OverflowMenu(
            onLogout = onLogout
          )
        },
        expanded = isSearchExpanded,
        onExpandedChange = { isSearchExpanded = it },
      )
    },
    windowInsets = WindowInsets(0.dp),
    expanded = isSearchExpanded,
    onExpandedChange = { isSearchExpanded = it },
    content = {
      Box(modifier = Modifier.fillMaxSize()) {
        SongsList(
          modifier = Modifier.fillMaxSize(),
          songs = state.songs,
          onSongSelected = onSongSelected
        )
        ErrorBanner(
          modifier = Modifier.fillMaxWidth(),
          visible = state.isErrorSearching,
          label = stringResource(R.string.errorSearchingSong),
          onDismiss = { onDismissSearchingSongsError() }
        )
        if (state.isLoading) {
          LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.colorScheme.colorPrimary,
            trackColor = AppTheme.colorScheme.colorSurface
          )
        }
      }
    }
  )
}

@Composable
private fun BoxScope.SongsList(
  modifier: Modifier = Modifier,
  songs: List<Song>,
  onSongSelected: (song: Song) -> Unit
) {
  LazyColumn(
    modifier = modifier,
    contentPadding = PaddingValues(bottom = 96.dp)
  ) {
    items(items = songs, key = { it.songId.value }) { song ->
      SongListItem(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onSongSelected(song) },
        song = song
      )
    }
  }
  if (songs.isEmpty()) {
    Text(
      modifier = Modifier.align(Alignment.Center),
      text = stringResource(R.string.emptySongsList),
      style = AppTheme.typography.emptyStateLabel,
      textAlign = TextAlign.Center,
      color = AppTheme.colorScheme.colorOnBackground
    )
  }
}

@Composable
fun SongListItem(
  modifier: Modifier = Modifier,
  song: Song
) {
  ListItem(
    modifier = modifier,
    colors = ListItemDefaults.colors(
      containerColor = AppTheme.colorScheme.background,
      headlineColor = AppTheme.colorScheme.colorOnBackground,
      supportingColor = AppTheme.colorScheme.colorOnBackground.copy(.5f)
    ),
    headlineContent = {
      Text(text = song.songTitle.value, style = AppTheme.typography.title)
    },
    supportingContent = {
      Text(
        text = song.songPerformer.name,
        style = AppTheme.typography.buttonLabel.copy(fontWeight = FontWeight.Normal)
      )
    }
  )
}

@Composable
private fun OverflowMenu(
  onLogout: () -> Unit
) {
  var showMenu by remember { mutableStateOf(false) }
  IconButton(
    modifier = Modifier.testTag("actionOverflow"),
    onClick = { showMenu = !showMenu }
  ) {
    Icon(
      imageVector = Icons.Default.MoreVert,
      contentDescription = stringResource(R.string.cdActionMore),
      tint = AppTheme.colorScheme.colorOnBackground
    )
  }
  DropdownMenu(
    containerColor = AppTheme.colorScheme.background,
    expanded = showMenu,
    onDismissRequest = { showMenu = false }
  ) {
    DropdownMenuItem(
      onClick = {
        showMenu = false
        onLogout()
      },
      text = {
        Text(text = stringResource(R.string.logout))
      }
    )
  }
}

@Composable
@PreviewLightDark
private fun PreviewSongsOverviewScreen() {
  AppTheme {
    val songs = listOf(
      Song(
        SongId("1"),
        SongTitle("Tears"),
        SongPerformer("Sabrina Carpenter"),
        SongLyrics("Some Lyrics Here")
      ),
      Song(
        SongId("2"),
        SongTitle("Lose Yourself"),
        SongPerformer("Eminem"),
        SongLyrics("Some Lyrics Here")
      ),
      Song(
        SongId("3"),
        SongTitle("I'm on my way"),
        SongPerformer("Alan Walker"),
        SongLyrics("Some Lyrics Here")
      )
    )
    SongsOverviewScreenContent(
      SongsOverviewScreenState(songs = songs),
      onAddNewSong = {},
      onRefresh = {},
      onLogout = {},
      onSearchTermUpdated = {},
      onSearch = {},
      onSongClick = {},
      onDismissSearchingSongsError = {},
      onDismissLoadingSongsError = {}
    )
  }
}

@Composable
@PreviewLightDark
private fun PreviewEmptySongsOverviewScreen() {
  AppTheme {
    SongsOverviewScreenContent(
      SongsOverviewScreenState(),
      onAddNewSong = {},
      onRefresh = {},
      onLogout = {},
      onSearchTermUpdated = {},
      onSearch = {},
      onSongClick = {},
      onDismissSearchingSongsError = {},
      onDismissLoadingSongsError = {}
    )
  }
}