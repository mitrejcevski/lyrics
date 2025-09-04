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
class DeleteSongTest {

  private val theSong = aSong().withId("songId").withTitle("song title").build()

  private val songsService = InMemorySongsService(IdGenerator(), listOf(theSong))
  private val repository = SongsRepository(songsService)
  private val dispatchers = TestCoroutineDispatchers()
  private val savedStateHandle = SavedStateHandle().apply {
    set("songId", theSong.songId.value)
  }

  @Test
  fun songDeleted() {
    val viewModel = SongDetailsViewModel(repository, dispatchers, savedStateHandle)

    viewModel.deleteSong()

    assertThat(viewModel.uiState.value).isEqualTo(SongDetailsScreenState(isSongDeleted = true))
  }

  @Test
  fun errorDeletingSong() {
    songsService.setUnavailable()
    val viewModel = SongDetailsViewModel(repository, dispatchers, savedStateHandle)

    viewModel.deleteSong()

    assertThat(viewModel.uiState.value).isEqualTo(SongDetailsScreenState(isSongDeletingError = true))
  }

  @Test
  fun dismissSongDeletingError() {
    songsService.setUnavailable()
    val viewModel = SongDetailsViewModel(repository, dispatchers, savedStateHandle).apply {
      deleteSong()
    }

    viewModel.dismissSongDeletingError()

    assertThat(viewModel.uiState.value).isEqualTo(SongDetailsScreenState())
  }


  @Test
  fun deleteSongUiStateDeliveredInOrder() = runTest {
    val viewModel = SongDetailsViewModel(repository, dispatchers, savedStateHandle)

    val deliveredStates = observeFlow(viewModel.uiState) {
      viewModel.deleteSong()
    }

    assertThat(deliveredStates).isEqualTo(
      listOf(
        SongDetailsScreenState(isLoading = true),
        SongDetailsScreenState(isSongDeleted = true)
      )
    )
  }
}