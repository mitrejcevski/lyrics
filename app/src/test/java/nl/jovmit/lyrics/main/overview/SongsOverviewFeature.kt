package nl.jovmit.lyrics.main.overview

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.inOrder
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.data.result.SongsResult
import nl.jovmit.lyrics.main.data.song.SongBuilder
import nl.jovmit.lyrics.main.data.song.SongBuilder.Companion.aSong
import nl.jovmit.lyrics.main.exceptions.SongsServiceException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class, InstantTaskExecutorExtension::class)
class SongsOverviewFeature {

    @Mock
    private lateinit var songsService: SongsService
    @Mock
    private lateinit var songsObserver: Observer<SongsResult>

    private val startLoading = SongsResult.Loading(true)
    private val fetchedSongs = SongsResult.Fetched(emptyList())
    private val fetchingError = SongsResult.FetchingError
    private val stopLoading = SongsResult.Loading(false)

    private lateinit var songsViewModel: SongsViewModel

    @BeforeEach
    fun set_up() {
        val dispatchers = TestCoroutineDispatchers()
        val songsRepository = SongsRepository(songsService)
        songsViewModel = SongsViewModel(songsRepository, dispatchers)
        songsViewModel.songsLiveData().observeForever(songsObserver)
    }

    @Test
    fun should_display_fetched_songs() = runBlocking {
        given(songsService.fetchAllSongs()).willReturn(emptyList())

        songsViewModel.fetchSongs()

        val inOrder = inOrder(songsObserver)
        inOrder.verify(songsObserver).onChanged(startLoading)
        inOrder.verify(songsObserver).onChanged(fetchedSongs)
        inOrder.verify(songsObserver).onChanged(stopLoading)
    }

    @Test
    fun should_display_error_when_fetching_fails() = runBlocking {
        given(songsService.fetchAllSongs()).willThrow(SongsServiceException())

        songsViewModel.fetchSongs()

        val inOrder = inOrder(songsObserver)
        inOrder.verify(songsObserver).onChanged(startLoading)
        inOrder.verify(songsObserver).onChanged(fetchingError)
        inOrder.verify(songsObserver).onChanged(stopLoading)
    }

    @Test
    fun should_display_songs_found() {
        val query = "song"
        val titleContainingQuery = "::irrelevant song title::"
        val filteredSongs = listOf(aSong().withTitle(titleContainingQuery).build())

        songsViewModel.search(query)

        val inOrder = inOrder(songsObserver)
        inOrder.verify(songsObserver).onChanged(startLoading)
        inOrder.verify(songsObserver).onChanged(SongsResult.Fetched(filteredSongs))
        inOrder.verify(songsObserver).onChanged(stopLoading)
    }
}