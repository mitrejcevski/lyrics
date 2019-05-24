package nl.jovmit.lyrics.main.overview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.verifyBlocking
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.SongsRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SongsOverviewViewModelShould {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var songsRepository: SongsRepository

    private lateinit var songsViewModel: SongsOverviewViewModel

    @Before
    fun setUp() {
        val testDispatchers = TestCoroutineDispatchers()
        songsViewModel = SongsOverviewViewModel(songsRepository, testDispatchers)
    }

    @Test
    fun fetchSongsFromTheSongsRepository() {
        songsViewModel.fetchSongs()

        verifyBlocking(songsRepository) { fetchAllSongs() }
    }
}