package nl.jovmit.lyrics.main.add

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.overview.SongsRepository
import nl.jovmit.lyrics.utils.IdGenerator
import nl.jovmit.lyrics.utils.observeFlow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class AddNewSongTest {

  private val songTitle = ":irrelevant:"
  private val songPerformer = ":irrelevant:"
  private val songLyrics = ":irrelevant:"

  private val dispatcher = TestCoroutineDispatchers()
  private val repository = SongsRepository(InMemorySongsService(IdGenerator()))

  @Test
  fun defaultStateIsIdle() {
    val viewModel = NewSongViewModel(repository, dispatcher)

    assertThat(viewModel.uiState.value).isEqualTo(NewSongScreenState())
  }

  @Test
  fun updateSongTitle() {
    val viewModel = NewSongViewModel(repository, dispatcher)

    viewModel.updateSongTitle(songTitle)

    assertThat(viewModel.uiState.value)
      .isEqualTo(NewSongScreenState(songTitle = songTitle))
  }

  @Test
  fun emptySongTitleProducesError() {
    val viewModel = NewSongViewModel(repository, dispatcher)

    viewModel.saveSong()

    assertThat(viewModel.uiState.value)
      .isEqualTo(NewSongScreenState(isEmptySongTitleError = true))
  }

  @Test
  fun resetEmptySongTitleError() {
    val viewModel = NewSongViewModel(repository, dispatcher).apply {
      saveSong()
    }

    viewModel.updateSongTitle(this.songTitle)

    assertThat(viewModel.uiState.value)
      .isEqualTo(NewSongScreenState(songTitle = this.songTitle, isEmptySongTitleError = false))
  }

  @Test
  fun updateSongPerformer() {
    val viewModel = NewSongViewModel(repository, dispatcher)

    viewModel.updatePerformerName(songPerformer)

    assertThat(viewModel.uiState.value)
      .isEqualTo(NewSongScreenState(performerName = songPerformer))
  }

  @Test
  fun emptySongPerformerProducesError() {
    val viewModel = NewSongViewModel(repository, dispatcher).apply {
      updateSongTitle(this@AddNewSongTest.songTitle)
    }

    viewModel.saveSong()

    assertThat(viewModel.uiState.value)
      .isEqualTo(NewSongScreenState(songTitle = this.songTitle, isEmptyPerformerNameError = true))
  }

  @Test
  fun resetEmptySongPerformerError() {
    val viewModel = NewSongViewModel(repository, dispatcher).apply {
      updateSongTitle(songTitle)
      saveSong()
    }

    viewModel.updatePerformerName(songPerformer)

    assertThat(viewModel.uiState.value).isEqualTo(
      NewSongScreenState(
        songTitle = songTitle,
        performerName = songPerformer
      )
    )
  }

  @Test
  fun updateSongLyrics() {
    val viewModel = NewSongViewModel(repository, dispatcher)

    viewModel.updateLyrics(songLyrics)

    assertThat(viewModel.uiState.value)
      .isEqualTo(NewSongScreenState(lyrics = songLyrics))
  }

  @Test
  fun emptySongLyricsProducesError() {
    val viewModel = NewSongViewModel(repository, dispatcher).apply {
      updateSongTitle(songTitle)
      updatePerformerName(songPerformer)
    }

    viewModel.saveSong()

    assertThat(viewModel.uiState.value).isEqualTo(
      NewSongScreenState(
        songTitle = songTitle,
        performerName = songPerformer,
        isEmptyLyricsError = true
      )
    )
  }

  @Test
  fun resetEmptySongLyricsError() {
    val viewModel = NewSongViewModel(repository, dispatcher).apply {
      updateSongTitle(songTitle)
      updatePerformerName(songPerformer)
      saveSong()
    }

    viewModel.updateLyrics(songLyrics)

    assertThat(viewModel.uiState.value).isEqualTo(
      NewSongScreenState(
        songTitle = songTitle,
        performerName = songPerformer,
        lyrics = songLyrics,
        isEmptyLyricsError = false
      )
    )
  }

  @Test
  fun successfulSongSaving() {
    val viewModel = NewSongViewModel(repository, dispatcher).apply {
      updateSongTitle(songTitle)
      updatePerformerName(songPerformer)
      updateLyrics(songLyrics)
    }

    viewModel.saveSong()

    assertThat(viewModel.uiState.value).isEqualTo(
      NewSongScreenState(
        songTitle = songTitle,
        performerName = songPerformer,
        lyrics = songLyrics,
        songAdded = true
      )
    )
  }

  @Test
  fun failureSavingSong() {
    val repository = SongsRepository(InMemorySongsService(IdGenerator()).apply { setUnavailable() })
    val viewModel = NewSongViewModel(repository, dispatcher).apply {
      updateSongTitle(songTitle)
      updatePerformerName(songPerformer)
      updateLyrics(songLyrics)
    }

    viewModel.saveSong()

    assertThat(viewModel.uiState.value).isEqualTo(
      NewSongScreenState(
        songTitle = songTitle,
        performerName = songPerformer,
        lyrics = songLyrics,
        errorAddingSong = true
      )
    )
  }

  @Test
  fun createSongStatesDeliveredInOrder() = runTest {
    val viewModel = NewSongViewModel(repository, dispatcher).apply {
      updateSongTitle(songTitle)
      updatePerformerName(songPerformer)
      updateLyrics(songLyrics)
    }
    val state = viewModel.uiState.value

    val collectedStates = observeFlow(viewModel.uiState) {
      viewModel.saveSong()
    }

    assertThat(collectedStates).isEqualTo(
      listOf(
        state.copy(isLoading = true),
        state.copy(isLoading = false, songAdded = true)
      )
    )
  }
}