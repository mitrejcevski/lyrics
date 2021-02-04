package nl.jovmit.lyrics.main.details

import androidx.test.ext.junit.runners.AndroidJUnit4
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.data.song.*
import nl.jovmit.lyrics.main.testModuleWithCustomSongsService
import nl.jovmit.lyrics.utils.IdGenerator
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

@RunWith(AndroidJUnit4::class)
class SongDetailsScreenSpecification {

    private val song = Song(
        SongId("::irrelevant song id::"),
        SongTitle("::irrelevant song title::"),
        SongPerformer("::irrelevant song performer::"),
        SongLyrics("::irrelevant song lyrics::")
    )

    private val songsOverviewModule =
        testModuleWithCustomSongsService(
            InMemorySongsService(IdGenerator(), listOf(song))
        )

    @Before
    fun setUp() {
        loadKoinModules(songsOverviewModule)
    }

    @Test
    fun should_display_all_song_properties() {
        launchSongsOverviewScreen {
            tapOnSongWithTitle(song.songTitle.value)
        } verify {
            songTitleIsDisplayed(song)
            songPerformerIsDisplayed(song)
            songLyricsIsDisplayed(song)
        }
    }

    @Test
    fun should_prompt_before_deleting_song() {
        launchSongsOverviewScreen {
            tapOnSongWithTitle(song.songTitle.value)
            tapOnDeleteAction()
        } verify {
            deleteSongConfirmationIsShown()
        }
    }

    @After
    fun tearDown() {
        unloadKoinModules(songsOverviewModule)
    }
}