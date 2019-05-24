package nl.jovmit.lyrics.main.overview

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.SongsRepository
import nl.jovmit.lyrics.main.data.Song
import nl.jovmit.lyrics.main.data.result.SongsResult
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class SongsOverviewScreenSpecification {

    @Mock
    private lateinit var songsRepository: SongsRepository

    private val emptySongsList = SongsResult.Fetched(emptyList())
    private val song = Song("Title", "Singer Name", "The lyrics of the song")
    private val songsList = SongsResult.Fetched(listOf(song))

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun shouldDisplayEmptyStateWhenNoSongsAdded() = runBlocking<Unit> {
        given(songsRepository.fetchAllSongs()).willReturn(emptySongsList)

        launchSongsOverview {
            //no operation
        } verify {
            songsEmptyStateIsDisplayed()
        }
    }

    @Test
    fun shouldNotDisplayEmptyStateWhenSongsAdded() = runBlocking<Unit> {
        given(songsRepository.fetchAllSongs()).willReturn(songsList)

        launchSongsOverview {
            //no operation
        } verify {
            songsEmptyStateIsGone()
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}