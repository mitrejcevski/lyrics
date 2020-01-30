package nl.jovmit.lyrics.main.details

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.inOrder
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.data.result.SongResult
import nl.jovmit.lyrics.main.data.song.SongBuilder.Companion.aSong
import nl.jovmit.lyrics.main.overview.SongsRepository
import nl.jovmit.lyrics.utils.IdGenerator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class, InstantTaskExecutorExtension::class)
class DeleteSongFeature {

    @Mock
    private lateinit var idGenerator: IdGenerator
    @Mock
    private lateinit var songDetailsLiveDataObserver: Observer<SongResult>

    private val songId = "::irrelevant song id::"
    private val songs = listOf(aSong().withId(songId).build())

    private val startLoading = SongResult.Loading(true)
    private val songDeleted = SongResult.Deleted
    private val stopLoading = SongResult.Loading(false)

    private lateinit var songDetailsViewModel: SongDetailsViewModel

    @BeforeEach
    fun set_up() {
        val songsService = InMemorySongsService(idGenerator, songs)
        val songsRepository = SongsRepository(songsService)
        val dispatchers = TestCoroutineDispatchers()
        songDetailsViewModel = SongDetailsViewModel(songsRepository, dispatchers)
    }

    @Test
    fun delete_song_by_id() {
        songDetailsViewModel.deleteSongById(songId)

        val inOrder = inOrder(songDetailsLiveDataObserver)
        inOrder.verify(songDetailsLiveDataObserver).onChanged(startLoading)
        inOrder.verify(songDetailsLiveDataObserver).onChanged(songDeleted)
        inOrder.verify(songDetailsLiveDataObserver).onChanged(stopLoading)
    }
}