package nl.jovmit.lyrics.main.edit

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
class SongEditingTest {

  private val song = aSong()
    .withId("1")
    .withTitle("title")
    .withPerformer("perf")
    .withLyrics("lyrics")
    .build()

  private val repository = SongsRepository(InMemorySongsService(IdGenerator(), listOf(song)))
  private val dispatchers = TestCoroutineDispatchers()
  private val stateHandle = SavedStateHandle().apply {
    set("songId", song.songId.value)
  }

  @Test
  fun defaultStateIsIdle() {
    val viewModel = UpdateSongViewModel(repository, dispatchers, stateHandle)
    assertThat(viewModel.uiState.value).isEqualTo(EditSongScreenState())
  }

  @Test
  fun songDataReflectsTheState() {
    val viewModel = UpdateSongViewModel(repository, dispatchers, stateHandle)

    viewModel.setSongData(song)

    assertThat(viewModel.uiState.value).isEqualTo(
      EditSongScreenState(
        songTitle = song.songTitle.value,
        songPerformer = song.songPerformer.name,
        songLyrics = song.songLyric.lyrics
      )
    )
  }

  @Test
  fun updateSongTitle() {
    val newTitle = ":irrelevant:"
    val viewModel = UpdateSongViewModel(repository, dispatchers, stateHandle)

    viewModel.updateSongTitle(newTitle)

    assertThat(viewModel.uiState.value).isEqualTo(EditSongScreenState(songTitle = newTitle))
  }

  @Test
  fun invalidSongTitle() {
    val viewModel = UpdateSongViewModel(repository, dispatchers, stateHandle)

    viewModel.updateSong()

    assertThat(viewModel.uiState.value).isEqualTo(EditSongScreenState(isEmptySongTitleError = true))
  }

  @Test
  fun clearInvalidSongTitleError() {
    val newTitle = ":a:"
    val viewModel = UpdateSongViewModel(repository, dispatchers, stateHandle).apply {
      updateSong()
    }

    viewModel.updateSongTitle(newTitle)

    assertThat(viewModel.uiState.value).isEqualTo(EditSongScreenState(songTitle = newTitle))
  }

  @Test
  fun updateSongPerformer() {
    val newPerformer = ":irrelevant:"
    val viewModel = UpdateSongViewModel(repository, dispatchers, stateHandle)

    viewModel.updateSongPerformer(newPerformer)

    assertThat(viewModel.uiState.value).isEqualTo(EditSongScreenState(songPerformer = newPerformer))
  }

  @Test
  fun invalidPerformerName() {
    val songTitle = ":irrelevant:"
    val viewModel = UpdateSongViewModel(repository, dispatchers, stateHandle).apply {
      updateSongTitle(songTitle)
    }

    viewModel.updateSong()

    assertThat(viewModel.uiState.value)
      .isEqualTo(EditSongScreenState(songTitle = songTitle, isEmptyPerformerNameError = true))
  }

  @Test
  fun clearInvalidPerformerNameError() {
    val songTitle = ":irrelevant:"
    val performer = ":irrelevant:"
    val viewModel = UpdateSongViewModel(repository, dispatchers, stateHandle).apply {
      updateSongTitle(songTitle)
      updateSong()
    }

    viewModel.updateSongPerformer(performer)

    assertThat(viewModel.uiState.value)
      .isEqualTo(EditSongScreenState(songTitle = songTitle, songPerformer = performer))
  }

  @Test
  fun updateLyrics() {
    val newLyrics = ":irrelevant:"
    val viewModel = UpdateSongViewModel(repository, dispatchers, stateHandle)

    viewModel.updateSongLyrics(newLyrics)

    assertThat(viewModel.uiState.value).isEqualTo(EditSongScreenState(songLyrics = newLyrics))
  }

  @Test
  fun invalidSongLyrics() {
    val songTitle = ":irrelevant:"
    val performer = ":irrelevant:"
    val viewModel = UpdateSongViewModel(repository, dispatchers, stateHandle).apply {
      updateSongTitle(songTitle)
      updateSongPerformer(performer)
    }

    viewModel.updateSong()

    assertThat(viewModel.uiState.value).isEqualTo(
      EditSongScreenState(
        songTitle = songTitle,
        songPerformer = performer,
        isEmptyLyricsError = true
      )
    )
  }

  @Test
  fun clearInvalidLyricsError() {
    val songTitle = ":irrelevant:"
    val performer = ":irrelevant:"
    val lyrics = ":irrelevant:"
    val viewModel = UpdateSongViewModel(repository, dispatchers, stateHandle).apply {
      updateSongTitle(songTitle)
      updateSongPerformer(performer)
      updateSong()
    }

    viewModel.updateSongLyrics(lyrics)

    assertThat(viewModel.uiState.value).isEqualTo(
      EditSongScreenState(
        songTitle = songTitle,
        songPerformer = performer,
        songLyrics = lyrics
      )
    )
  }

  @Test
  fun errorUpdatingSong() {
    val songTitle = ":irrelevant:"
    val performer = ":irrelevant:"
    val lyrics = ":irrelevant:"
    stateHandle["songId"] = "anythingBut${song.songId}"
    val viewModel = UpdateSongViewModel(repository, dispatchers, stateHandle).apply {
      updateSongTitle(songTitle)
      updateSongPerformer(performer)
      updateSongLyrics(lyrics)
    }

    viewModel.updateSong()

    assertThat(viewModel.uiState.value).isEqualTo(
      EditSongScreenState(
        songTitle = songTitle,
        songPerformer = performer,
        songLyrics = lyrics,
        isErrorSavingSong = true
      )
    )
  }

  @Test
  fun clearSongUpdatingError() {
    val songTitle = ":irrelevant:"
    val performer = ":irrelevant:"
    val lyrics = ":irrelevant:"
    stateHandle["songId"] = "anythingBut${song.songId}"
    val viewModel = UpdateSongViewModel(repository, dispatchers, stateHandle).apply {
      updateSongTitle(songTitle)
      updateSongPerformer(performer)
      updateSongLyrics(lyrics)
      updateSong()
    }

    viewModel.dismissErrorBanner()

    assertThat(viewModel.uiState.value).isEqualTo(
      EditSongScreenState(
        songTitle = songTitle,
        songPerformer = performer,
        songLyrics = lyrics,
      )
    )
  }

  @Test
  fun songUpdatedSuccessfully() {
    val songTitle = ":irrelevant:"
    val performer = ":irrelevant:"
    val lyrics = ":irrelevant:"
    val viewModel = UpdateSongViewModel(repository, dispatchers, stateHandle).apply {
      updateSongTitle(songTitle)
      updateSongPerformer(performer)
      updateSongLyrics(lyrics)
      updateSong()
    }

    viewModel.dismissErrorBanner()

    assertThat(viewModel.uiState.value).isEqualTo(
      EditSongScreenState(
        songTitle = songTitle,
        songPerformer = performer,
        songLyrics = lyrics,
        songUpdated = true
      )
    )
  }

  @Test
  fun editSongDeliveredUiStatesInOrder() = runTest {
    val songTitle = ":irrelevant:"
    val performer = ":irrelevant:"
    val lyrics = ":irrelevant:"
    val viewModel = UpdateSongViewModel(repository, dispatchers, stateHandle).apply {
      updateSongTitle(songTitle)
      updateSongPerformer(performer)
      updateSongLyrics(lyrics)
    }

    val deliveredStates = observeFlow(viewModel.uiState) {
      viewModel.updateSong()
    }

    assertThat(deliveredStates).isEqualTo(listOf(
      EditSongScreenState(
        songTitle = songTitle,
        songPerformer = performer,
        songLyrics = lyrics,
        isLoading = true
      ),
      EditSongScreenState(
        songTitle = songTitle,
        songPerformer = performer,
        songLyrics = lyrics,
        songUpdated = true
      )
    ))
  }

}