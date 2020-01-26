package nl.jovmit.lyrics.main.add

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.inMemoryTestModule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

@RunWith(AndroidJUnit4::class)
class NewSongScreenSpecification {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java, true, false)

    @Before
    fun setUp() {
        loadKoinModules(inMemoryTestModule)
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

    @After
    fun tearDown() {
        unloadKoinModules(inMemoryTestModule)
    }
}