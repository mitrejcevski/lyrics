package nl.jovmit.lyrics.main.overview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.willReturn
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.SongsRepository
import nl.jovmit.lyrics.main.data.result.SongsResult
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
    private lateinit var songsRepository: SongsRepository
    @Mock
    private lateinit var songsObserver: Observer<SongsResult>

    private val startLoading = SongsResult.Loading(true)
    private val fetchedSongs = SongsResult.Fetched(emptyList())
    private val stopLoading = SongsResult.Loading(false)

    private lateinit var songsOverviewViewModel: SongsOverviewViewModel

    @Before
    fun setUp() {
        val dispatchers = TestCoroutineDispatchers()
        songsOverviewViewModel = SongsOverviewViewModel(songsRepository, dispatchers)
        songsOverviewViewModel.songsLiveData().observeForever(songsObserver)
    }

    @Test
    fun shouldDisplayFetchedSongs() = runBlocking {
        given(songsRepository.fetchAllSongs()).willReturn { fetchedSongs }

        songsOverviewViewModel.fetchSongs()

        val inOrder = inOrder(songsObserver)
        inOrder.verify(songsObserver).onChanged(startLoading)
        inOrder.verify(songsObserver).onChanged(fetchedSongs)
        inOrder.verify(songsObserver).onChanged(stopLoading)
    }
}