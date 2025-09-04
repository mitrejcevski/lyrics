package nl.jovmit.lyrics.main.overview

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.data.song.SongBuilder.Companion.aSong
import nl.jovmit.lyrics.utils.IdGenerator
import nl.jovmit.lyrics.utils.observeFlow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID

@ExtendWith(InstantTaskExecutorExtension::class)
class LoadSongsTest {

  private val song1 = aSong().withId(UUID.randomUUID().toString()).withTitle("one").build()
  private val song2 = aSong().withId(UUID.randomUUID().toString()).withTitle("two").build()
  private val song3 = aSong().withId(UUID.randomUUID().toString()).withTitle("three").build()
  private val song4 = aSong().withId(UUID.randomUUID().toString()).withTitle("four").build()
  private val allSongs = listOf(song1, song2, song3, song4)

  private val repository = SongsRepository(InMemorySongsService(IdGenerator()))
  private val dispatchers = TestCoroutineDispatchers()

  @Test
  fun defaultStateIsIdle() {
    val viewModel = SongsViewModel(repository, dispatchers)

    assertThat(viewModel.uiState.value).isEqualTo(SongsOverviewScreenState())
  }

  @Test
  fun updateSearchQuery() {
    val query = ":irrelevant:"
    val viewModel = SongsViewModel(repository, dispatchers)

    viewModel.updateSearchQuery(query)

    assertThat(viewModel.uiState.value).isEqualTo(SongsOverviewScreenState(searchQuery = query))
  }

  @Test
  fun emptySongsResult() {
    val viewModel = SongsViewModel(repository, dispatchers)

    viewModel.fetchSongs()

    assertThat(viewModel.uiState.value).isEqualTo(SongsOverviewScreenState(songs = emptyList()))
  }

  @Test
  fun songsLoaded() {
    val repository = SongsRepository(InMemorySongsService(IdGenerator(), allSongs))
    val viewModel = SongsViewModel(repository, dispatchers)

    viewModel.fetchSongs()

    assertThat(viewModel.uiState.value).isEqualTo(SongsOverviewScreenState(songs = allSongs))
  }

  @Test
  fun errorLoadingSongs() {
    val repository = SongsRepository(InMemorySongsService(IdGenerator()).apply { setUnavailable() })
    val viewModel = SongsViewModel(repository, dispatchers)

    viewModel.fetchSongs()

    assertThat(viewModel.uiState.value)
      .isEqualTo(SongsOverviewScreenState(isErrorLoadingSongs = true))
  }

  @Test
  fun noSongsFound() {
    val query = "unknown"
    val repository = SongsRepository(InMemorySongsService(IdGenerator(), allSongs))
    val viewModel = SongsViewModel(repository, dispatchers).apply {
      updateSearchQuery(query)
    }

    viewModel.performSearch()

    assertThat(viewModel.uiState.value)
      .isEqualTo(SongsOverviewScreenState(searchQuery = query, songs = emptyList()))
  }

  @Test
  fun songFound() {
    val query = song1.songTitle.value
    val repository = SongsRepository(InMemorySongsService(IdGenerator(), allSongs))
    val viewModel = SongsViewModel(repository, dispatchers).apply {
      updateSearchQuery(query)
    }

    viewModel.performSearch()

    assertThat(viewModel.uiState.value)
      .isEqualTo(SongsOverviewScreenState(searchQuery = query, songs = listOf(song1)))
  }

  @Test
  fun errorSearchingSongs() {
    val query = song1.songTitle.value
    val repository = SongsRepository(InMemorySongsService(IdGenerator()).apply { setUnavailable() })
    val viewModel = SongsViewModel(repository, dispatchers).apply {
      updateSearchQuery(query)
    }

    viewModel.performSearch()

    assertThat(viewModel.uiState.value)
      .isEqualTo(SongsOverviewScreenState(searchQuery = query, isErrorSearching = true))
  }

  @Test
  fun loadingSongsUiStateDeliveryInOrder() = runTest {
    val repository = SongsRepository(InMemorySongsService(IdGenerator(), allSongs))
    val viewModel = SongsViewModel(repository, dispatchers)

    val deliveredStates = observeFlow(viewModel.uiState) {
      viewModel.fetchSongs()
    }

    assertThat(deliveredStates).isEqualTo(
      listOf(
        SongsOverviewScreenState(isLoading = true),
        SongsOverviewScreenState(songs = allSongs)
      )
    )
  }

  @Test
  fun searchingSongsUiStateDeliveryInOrder() = runTest {
    val repository = SongsRepository(InMemorySongsService(IdGenerator(), allSongs))
    val viewModel = SongsViewModel(repository, dispatchers)

    val deliveredStates = observeFlow(viewModel.uiState) {
      viewModel.updateSearchQuery(song2.songTitle.value)
      viewModel.performSearch()
    }

    assertThat(deliveredStates).isEqualTo(
      listOf(
        SongsOverviewScreenState(searchQuery = song2.songTitle.value),
        SongsOverviewScreenState(searchQuery = song2.songTitle.value, isLoading = true),
        SongsOverviewScreenState(searchQuery = song2.songTitle.value, songs = listOf(song2))
      )
    )
  }
}