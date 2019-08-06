package nl.jovmit.lyrics.main.overview

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.common.AppCoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.main.SongsRepository
import nl.jovmit.lyrics.main.data.Song
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class SongsOverviewScreenSpecification {

    @Mock
    private lateinit var songsService: SongsService

    private val emptySongsList = emptyList<Song>()
    private val song = Song("Title", "Singer Name", "The lyrics of the song")
    private val songsList = listOf(song)

    private val songsOverviewModule = module {
        factory { SongsRepository(songsService) }
        single<CoroutineDispatchers> { AppCoroutineDispatchers() }
        viewModel { SongsOverviewViewModel(get(), get()) }
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        loadKoinModules(songsOverviewModule)
    }

    @Test
    fun shouldDisplayEmptyStateWhenNoSongsAdded() = runBlocking<Unit> {
        given(songsService.fetchAllSongs()).willReturn(emptySongsList)

        launchSongsOverview {
            //no operation
        } verify {
            songsEmptyStateIsDisplayed()
        }
    }

    @Test
    fun shouldNotDisplayEmptyStateWhenSongsAdded() = runBlocking<Unit> {
        given(songsService.fetchAllSongs()).willReturn(songsList)

        launchSongsOverview {
            //no operation
        } verify {
            songsEmptyStateIsGone()
        }
    }

    @After
    fun tearDown() {
        unloadKoinModules(songsOverviewModule)
    }
}