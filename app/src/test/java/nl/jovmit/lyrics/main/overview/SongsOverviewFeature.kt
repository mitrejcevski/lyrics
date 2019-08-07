package nl.jovmit.lyrics.main.overview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.inOrder
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.SongsRepository
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.data.result.SongsResult
import nl.jovmit.lyrics.main.exceptions.SongsServiceException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SongsOverviewFeature {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var songsService: SongsService
    @Mock
    private lateinit var songsObserver: Observer<SongsResult>

    private val startLoading = SongsResult.Loading(true)
    private val fetchedSongs = SongsResult.Fetched(emptyList())
    private val fetchingError = SongsResult.FetchingError
    private val stopLoading = SongsResult.Loading(false)

    private lateinit var songsOverviewViewModel: SongsOverviewViewModel

    @Before
    fun setUp() {
        val dispatchers = TestCoroutineDispatchers()
        val songsRepository = SongsRepository(songsService)
        songsOverviewViewModel = SongsOverviewViewModel(songsRepository, dispatchers)
        songsOverviewViewModel.songsLiveData().observeForever(songsObserver)
    }

    @Test
    fun shouldDisplayFetchedSongs() = runBlocking {
        given(songsService.fetchAllSongs()).willReturn(emptyList())

        songsOverviewViewModel.fetchSongs()

        val inOrder = inOrder(songsObserver)
        inOrder.verify(songsObserver).onChanged(startLoading)
        inOrder.verify(songsObserver).onChanged(fetchedSongs)
        inOrder.verify(songsObserver).onChanged(stopLoading)
    }

    @Test
    fun shouldDisplayErrorWhenFetchingFails() = runBlocking {
        given(songsService.fetchAllSongs()).willThrow(SongsServiceException())

        songsOverviewViewModel.fetchSongs()

        val inOrder = inOrder(songsObserver)
        inOrder.verify(songsObserver).onChanged(startLoading)
        inOrder.verify(songsObserver).onChanged(fetchingError)
        inOrder.verify(songsObserver).onChanged(stopLoading)
    }
}