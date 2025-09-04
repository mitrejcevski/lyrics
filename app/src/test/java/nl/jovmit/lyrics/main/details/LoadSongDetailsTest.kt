package nl.jovmit.lyrics.main.details

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.data.song.SongBuilder.Companion.aSong
import nl.jovmit.lyrics.main.overview.SongsRepository
import nl.jovmit.lyrics.utils.IdGenerator
import nl.jovmit.lyrics.utils.observeFlow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class LoadSongDetailsTest {

  private val theSong = aSong().withId("songId").withTitle("song title").build()

  private val songsService = InMemorySongsService(IdGenerator(), listOf(theSong))
  private val repository = SongsRepository(songsService)
  private val dispatchers = TestCoroutineDispatchers()
  private val savedStateHandle = SavedStateHandle().apply {
    set("songId", theSong.songId.value)
  }

  @Test
  fun defaultStateIdle() {
    val viewModel = SongDetailsViewModel(repository, dispatchers, savedStateHandle)
    assertThat(viewModel.uiState.value).isEqualTo(SongDetailsScreenState())
  }

  @Test
  fun songLoaded() {
    val viewModel = SongDetailsViewModel(repository, dispatchers, savedStateHandle)

    viewModel.loadSong()

    assertThat(viewModel.uiState.value).isEqualTo(SongDetailsScreenState(song = theSong))
  }

  @Test
  fun songNotFound() {
    savedStateHandle["songId"] = "anythingBut${theSong.songId.value}"
    val viewModel = SongDetailsViewModel(repository, dispatchers, savedStateHandle)

    viewModel.loadSong()

    assertThat(viewModel.uiState.value).isEqualTo(SongDetailsScreenState(isSongNotFoundError = true))
  }

  @Test
  fun dismissSongNotFoundError() {
    savedStateHandle["songId"] = "anythingBut${theSong.songId.value}"
    val viewModel = SongDetailsViewModel(repository, dispatchers, savedStateHandle).apply {
      loadSong()
    }

    viewModel.dismissSongNotFoundError()

    assertThat(viewModel.uiState.value).isEqualTo(SongDetailsScreenState())
  }


  @Test
  fun loadSongDetailsUiStateDeliveredInOrder() = runTest {
    val viewModel = SongDetailsViewModel(repository, dispatchers, savedStateHandle)

    val deliveredStates = observeFlow(viewModel.uiState) {
      viewModel.loadSong()
    }

    assertThat(deliveredStates).isEqualTo(
      listOf(
        SongDetailsScreenState(isLoading = true), SongDetailsScreenState(song = theSong)
      )
    )
  }
}