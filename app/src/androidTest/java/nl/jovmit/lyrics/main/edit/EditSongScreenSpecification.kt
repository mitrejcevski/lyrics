package nl.jovmit.lyrics.main.edit

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
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

@RunWith(AndroidJUnit4::class)
class EditSongScreenSpecification {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java, true, false)

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
    private val songsList = listOf(song, anotherSong)
    private val songsOverviewModule by lazy {
        val service = InMemorySongsService(IdGenerator(), songsList)
        testModuleWithCustomSongsService(service)
    }

    @Before
    fun setUp() {
        loadKoinModules(songsOverviewModule)
    }

    @Test
    fun should_launch_song_editing() {
        launchMainScreenScreen(rule) {
            tapOnSong(song.songTitle.value)
            tapOnEditSongMenuItem()
        } verify {
            editScreenIsGettingLaunchedFor(song)
        }
    }

    @Test
    fun should_update_song_title() {
        val updatedSongTitle = "New Song Title"

        launchMainScreenScreen(rule) {
            tapOnSong(song.songTitle.value)
            tapOnEditSongMenuItem()
            replaceSongTitleWith(updatedSongTitle)
            tapOnDoneMenuItem()
        } verify {
            val updatedSong = song.copy(songTitle = SongTitle(updatedSongTitle))
            songDetailArePresentFor(updatedSong)
        }
    }

    @Test
    fun show_success_when_song_successfully_updated() {
        val updatedSongTitle = "New Song Title"

        launchMainScreenScreen(rule) {
            tapOnSong(song.songTitle.value)
            tapOnEditSongMenuItem()
            replaceSongTitleWith(updatedSongTitle)
            tapOnDoneMenuItem()
        } verify {
            successUpdatingSongMessageShown()
        }
    }

    @After
    fun tear_down() {
        unloadKoinModules(songsOverviewModule)
    }
}