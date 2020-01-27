package nl.jovmit.lyrics.main.add

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.exceptions.SongsServiceException
import nl.jovmit.lyrics.main.testModuleWithCustomSongsService
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.LazyThreadSafetyMode.NONE

@RunWith(AndroidJUnit4::class)
class NewSongScreenSpecification {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java, true, false)

    @Mock
    private lateinit var songsService: SongsService

    private val module by lazy(NONE) {
        testModuleWithCustomSongsService(songsService)
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        loadKoinModules(module)
        runBlocking { whenever(songsService.fetchAllSongs()).thenReturn(emptyList()) }
    }

    @Test
    fun should_show_empty_song_title_error() {
        launchNewSongScreen(rule) {} submit {
            emptySongTitleErrorIsDisplayed()
        }
    }

    @Test
    fun should_show_empty_song_performer_error() {
        launchNewSongScreen(rule) {
            typeSongTitle("Song title")
        } submit {
            emptySongPerformerErrorIsDisplayed()
        }
    }

    @Test
    fun should_show_empty_song_lyrics_error() {
        launchNewSongScreen(rule) {
            typeSongTitle("Usher")
            typeSongPerformer("Yeah")
        } submit {
            emptySongLyricsErrorIsDisplayed()
        }
    }

    @Test
    fun should_store_new_song() {
        launchNewSongScreen(rule) {
            typeSongTitle("New song title")
            typeSongPerformer("Song performer")
            typeSongLyrics("Song lyrics goes here...")
        } submit {
            verifySongBeingSaved()
        }
    }

    @Test
    fun should_show_error_saving_song() = runBlocking<Unit> {
        given(songsService.addNewSong(any())).willThrow(SongsServiceException())
        launchNewSongScreen(rule) {
            typeSongTitle("Song title")
            typeSongPerformer("Song performer")
            typeSongLyrics("Song lyrics")
        } submit {
            verifyErrorSavingSong()
        }
    }

    @After
    fun tearDown() {
        unloadKoinModules(module)
    }
}