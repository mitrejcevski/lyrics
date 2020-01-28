package nl.jovmit.lyrics.main.overview

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.inOrder
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.data.result.SongsResult
import nl.jovmit.lyrics.main.data.song.SongBuilder.Companion.aSong
import nl.jovmit.lyrics.utils.IdGenerator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class, InstantTaskExecutorExtension::class)
class SongsOverviewFeature {

    @Mock
    private lateinit var idGenerator: IdGenerator
    @Mock
    private lateinit var songsObserver: Observer<SongsResult>

    private val song = aSong().withTitle("::first song title::").build()
    private val anotherSong = aSong().withTitle("::another song title::").build()
    private val songsList = listOf(song, anotherSong)

    private val startLoading = SongsResult.Loading(true)
    private val fetchedSongs = SongsResult.Fetched(songsList)
    private val stopLoading = SongsResult.Loading(false)

    private lateinit var songsViewModel: SongsViewModel

    @BeforeEach
    fun set_up() {
        val dispatchers = TestCoroutineDispatchers()
        val songsService = InMemorySongsService(idGenerator, songsList)
        val songsRepository = SongsRepository(songsService)
        songsViewModel = SongsViewModel(songsRepository, dispatchers)
        songsViewModel.songsLiveData().observeForever(songsObserver)
    }

    @Test
    fun should_display_fetched_songs() = runBlocking {
        songsViewModel.fetchSongs()

        val inOrder = inOrder(songsObserver)
        inOrder.verify(songsObserver).onChanged(startLoading)
        inOrder.verify(songsObserver).onChanged(fetchedSongs)
        inOrder.verify(songsObserver).onChanged(stopLoading)
    }

    @Test
    fun should_display_songs_found() = runBlocking {
        val query = "first"
        val expectedResult = SongsResult.Fetched(listOf(song))

        songsViewModel.search(query)

        val inOrder = inOrder(songsObserver)
        inOrder.verify(songsObserver).onChanged(startLoading)
        inOrder.verify(songsObserver).onChanged(expectedResult)
        inOrder.verify(songsObserver).onChanged(stopLoading)
    }
}