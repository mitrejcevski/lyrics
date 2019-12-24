package nl.jovmit.lyrics.main.overview

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.common.AppCoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.data.Song
import nl.jovmit.lyrics.main.exceptions.SongsServiceException
import org.junit.After
import org.junit.Before
import org.junit.Rule
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

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java, true, false)

    @Mock
    private lateinit var songsService: SongsService

    private val emptySongsList = emptyList<Song>()
    private val song = Song("Title", "Singer Name", "The lyrics of the song")
    private val songsList = listOf(song)

    private val songsOverviewModule = module {
        factory { SongsRepository(songsService) }
        single<CoroutineDispatchers> { AppCoroutineDispatchers() }
        viewModel { SongsViewModel(get(), get()) }
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        loadKoinModules(songsOverviewModule)
        runBlocking { whenever(songsService.fetchAllSongs()).thenReturn(songsList) }
    }

    @Test
    fun shouldDisplayEmptyStateWhenNoSongsAdded() = runBlocking<Unit> {
        given(songsService.fetchAllSongs()).willReturn(emptySongsList)

        launchSongsOverview(rule) { } verify {
            songsEmptyStateIsDisplayed()
        }
    }

    @Test
    fun shouldNotDisplayEmptyStateWhenSongsAdded() = runBlocking<Unit> {
        launchSongsOverview(rule) { } verify {
            songsEmptyStateIsGone()
        }
    }

    @Test
    fun shouldDisplayLoadedSongs() = runBlocking<Unit> {
        launchSongsOverview(rule) { } verify {
            songTitleAndSingerAreDisplayed(song)
        }
    }

    @Test
    fun shouldDisplayErrorIfLoadingSongsFails() = runBlocking<Unit> {
        given(songsService.fetchAllSongs()).willThrow(SongsServiceException())

        launchSongsOverview(rule) { } verify {
            loadingErrorIsDisplayed()
        }
    }

    @Test
    fun shouldOpenNewSongScreenUponClickOnNewSongButton() = runBlocking<Unit> {
        launchSongsOverview(rule) {
            clickOnNewSongButton()
        } verify {
            newSongScreenIsDisplayed()
        }
    }

    @After
    fun tearDown() {
        unloadKoinModules(songsOverviewModule)
    }
}