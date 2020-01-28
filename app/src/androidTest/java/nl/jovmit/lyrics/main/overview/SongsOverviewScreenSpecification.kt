package nl.jovmit.lyrics.main.overview

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.data.song.*
import nl.jovmit.lyrics.main.exceptions.SongsServiceException
import nl.jovmit.lyrics.main.testModuleWithCustomSongsService
import nl.jovmit.lyrics.utils.IdGenerator
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class SongsOverviewScreenSpecification {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java, true, false)

    @Mock
    private lateinit var songsService: SongsService
    private lateinit var module: Module

    private val song = Song(
        SongId("SongId"),
        SongTitle("Title"),
        SongPerformer("Singer Name"),
        SongLyrics("The lyrics of the song")
    )
    private val anotherSong = Song(
        SongId("AnotherSongId"),
        SongTitle("Another Title"),
        SongPerformer("Another Singer Name"),
        SongLyrics("The text of the song")
    )
    private val emptySongsList = emptyList<Song>()
    private val songsList = listOf(song, anotherSong)
    private val songsOverviewModule by lazy {
        val service = InMemorySongsService(IdGenerator(), songsList)
        testModuleWithCustomSongsService(service)
    }

    @Before
    fun set_up() {
        MockitoAnnotations.initMocks(this)
        loadKoinModules(songsOverviewModule)
    }

    @Test
    fun should_display_empty_state_when_no_songs_added() = runBlocking {
        setupModule(InMemorySongsService(IdGenerator(), emptySongsList))

        launchSongsOverview(rule) { } verify {
            songsEmptyStateIsDisplayed()
        }

        unloadModule()
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
            songTitleAndSingerAreDisplayed(anotherSong)
        }
    }

    @Test
    fun should_display_error_if_loading_songs_fails() = runBlocking {
        given(songsService.fetchAllSongs()).willThrow(SongsServiceException())
        setupModule(songsService)

        launchSongsOverview(rule) { } verify {
            loadingErrorIsDisplayed()
        }

        unloadModule()
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
    fun should_open_song_details_screen() = runBlocking<Unit> {
        launchSongsOverview(rule) {
            tapOnSongWithTitle(song.songTitle.value)
        } verify {
            songDetailsScreenIsOpened()
        }
    }

    @Test
    fun should_perform_search() = runBlocking<Unit> {
        val queryMatchingFirstSong = "lyrics"
        launchSongsOverview(rule) {
            typeSearchQuery(queryMatchingFirstSong)
        } verify {
            songTitleAndSingerAreDisplayed(song)
            songTitleAndSingerNotDisplayed(anotherSong)
        }
    }

    @Test
    fun display_all_songs_once_search_is_closed() {
        val queryMatchingFirstSong = "lyrics"
        launchSongsOverview(rule) {
            typeSearchQuery(queryMatchingFirstSong)
            closeSearch()
        } verify {
            songTitleAndSingerAreDisplayed(song)
            songTitleAndSingerAreDisplayed(anotherSong)
        }
    }

    @Test
    fun display_search_error() = runBlocking {
        val query = "query"
        given(songsService.fetchAllSongs()).willReturn(songsList)
        given(songsService.search(query)).willThrow(SongsServiceException())
        setupModule(songsService)

        launchSongsOverview(rule) {
            typeSearchQuery(query)
        } verify {
            searchErrorIsDisplayed()
        }

        unloadModule()
    }

    private fun setupModule(songsService: SongsService) {
        unloadKoinModules(songsOverviewModule)
        module = testModuleWithCustomSongsService(songsService)
        loadKoinModules(module)
    }

    private fun unloadModule() {
        unloadKoinModules(module)
    }

    @After
    fun tear_down() {
        unloadKoinModules(songsOverviewModule)
    }
}