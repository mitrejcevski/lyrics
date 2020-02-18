package nl.jovmit.lyrics.main.edit

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.inOrder
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.data.result.SongResult
import nl.jovmit.lyrics.main.data.song.SongBuilder.Companion.aSong
import nl.jovmit.lyrics.main.data.song.SongLyrics
import nl.jovmit.lyrics.main.data.song.SongPerformer
import nl.jovmit.lyrics.main.data.song.SongTitle
import nl.jovmit.lyrics.main.overview.SongsRepository
import nl.jovmit.lyrics.utils.IdGenerator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class, InstantTaskExecutorExtension::class)
class EditSongFeature {

    @Mock
    private lateinit var updateSongLiveDataObserver: Observer<SongResult>

    private val songId = "::songId::"
    private val title = "::title::"
    private val performer = "::performer::"
    private val lyrics = "::lyrics::"
    private val songs = listOf(aSong().withId(songId).build())
    private val songTitle = SongTitle(title)
    private val songPerformer = SongPerformer(performer)
    private val songLyrics = SongLyrics(lyrics)

    private val startLoading = SongResult.Loading(true)
    private val songEdited = SongResult.Updated
    private val stopLoading = SongResult.Loading(false)

    private lateinit var updateSongViewModel: UpdateSongViewModel

    @BeforeEach
    fun setUp() {
        val songsRepository = SongsRepository(InMemorySongsService(IdGenerator(), songs))
        val dispatchers = TestCoroutineDispatchers()
        updateSongViewModel = UpdateSongViewModel(songsRepository, dispatchers)
        updateSongViewModel.updateSongLiveData().observeForever(updateSongLiveDataObserver)
    }

    @Test
    fun edit_song() {
        updateSongViewModel.updateSong(songId, songTitle, songPerformer, songLyrics)

        val inOrder = inOrder(updateSongLiveDataObserver)
        inOrder.verify(updateSongLiveDataObserver).onChanged(startLoading)
        inOrder.verify(updateSongLiveDataObserver).onChanged(songEdited)
        inOrder.verify(updateSongLiveDataObserver).onChanged(stopLoading)
    }
}