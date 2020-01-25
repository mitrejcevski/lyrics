package nl.jovmit.lyrics.main.overview

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.common.AppCoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.main.InfoViewModel
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.add.NewSongRepository
import nl.jovmit.lyrics.main.add.NewSongViewModel
import nl.jovmit.lyrics.main.data.song.*
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
    private val song = Song(
        SongId("SongId"),
        SongTitle("Title"),
        SongPerformer("Singer Name"),
        SongLyrics("The lyrics of the song")
    )
    private val songsList = listOf(song)

    private val songsOverviewModule = module {
        single<CoroutineDispatchers> { AppCoroutineDispatchers() }
        factory { SongsRepository(songsService) }
        factory { NewSongRepository(songsService) }
        viewModel { SongsViewModel(get(), get()) }
        viewModel { NewSongViewModel(get(), get()) }
        viewModel { InfoViewModel() }
    }

    @Before
    fun set_up() {
        MockitoAnnotations.initMocks(this)
        loadKoinModules(songsOverviewModule)
        runBlocking { whenever(songsService.fetchAllSongs()).thenReturn(songsList) }
    }

    @Test
    fun should_display_empty_state_when_no_songs_added() = runBlocking<Unit> {
        given(songsService.fetchAllSongs()).willReturn(emptySongsList)

        launchSongsOverview(rule) { } verify {
            songsEmptyStateIsDisplayed()
        }
    }

    @Test
    fun should_not_display_empty_state_when_songs_added() = runBlocking<Unit> {
        launchSongsOverview(rule) { } verify {
            songsEmptyStateIsGone()
        }
    }

    @Test
    fun should_display_loaded_songs() = runBlocking<Unit> {
        launchSongsOverview(rule) { } verify {
            songTitleAndSingerAreDisplayed(song)
        }
    }

    @Test
    fun should_display_error_if_loading_songs_fails() = runBlocking<Unit> {
        given(songsService.fetchAllSongs()).willThrow(SongsServiceException())

        launchSongsOverview(rule) { } verify {
            loadingErrorIsDisplayed()
        }
    }

    @Test
    fun should_open_new_song_screen_upon_click_on_new_song_button() = runBlocking<Unit> {
        launchSongsOverview(rule) {
            clickOnNewSongButton()
        } verify {
            newSongScreenIsDisplayed()
        }
    }

    @Test
    fun should_open_song_details_screen() {
        launchSongsOverview(rule) {
            tapOnSongWithTitle(song.songTitle.value)
        } verify {
            songDetailsScreenIsOpened()
        }
    }

    @After
    fun tear_down() {
        unloadKoinModules(songsOverviewModule)
    }
}