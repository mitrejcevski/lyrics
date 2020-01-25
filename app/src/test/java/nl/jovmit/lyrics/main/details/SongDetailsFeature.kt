package nl.jovmit.lyrics.main.details

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.inOrder
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.data.result.SongResult
import nl.jovmit.lyrics.main.data.song.SongBuilder.Companion.aSong
import nl.jovmit.lyrics.main.data.song.SongDataBuilder.Companion.aSongData
import nl.jovmit.lyrics.main.overview.SongsRepository
import nl.jovmit.lyrics.utils.IdGenerator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class, InstantTaskExecutorExtension::class)
class SongDetailsFeature {

    @Mock
    private lateinit var songDetailsLiveDataObserver: Observer<SongResult>
    @Mock
    private lateinit var idGenerator: IdGenerator

    private val songId = UUID.randomUUID().toString()
    private val song = aSong().withId(songId).build()
    private val songData = aSongData().build()

    private val startLoading = SongResult.Loading(true)
    private val songFetched = SongResult.Fetched(song)
    private val stopLoading = SongResult.Loading(false)

    private lateinit var songsService: SongsService
    private lateinit var songDetailsViewModel: SongDetailsViewModel

    @BeforeEach
    fun set_up() {
        songsService = InMemorySongsService(idGenerator)
        val songsRepository = SongsRepository(songsService)
        val dispatchers = TestCoroutineDispatchers()
        songDetailsViewModel = SongDetailsViewModel(songsRepository, dispatchers)
        songDetailsViewModel.songDetailsLiveData().observeForever(songDetailsLiveDataObserver)
    }

    @Test
    fun fetch_song_by_id() = runBlocking {
        given(idGenerator.next()).willReturn(songId)
        songsService.addNewSong(songData)

        songDetailsViewModel.fetchSongById(songId)

        val inOrder = inOrder(songDetailsLiveDataObserver)
        inOrder.verify(songDetailsLiveDataObserver).onChanged(startLoading)
        inOrder.verify(songDetailsLiveDataObserver).onChanged(songFetched)
        inOrder.verify(songDetailsLiveDataObserver).onChanged(stopLoading)
    }
}