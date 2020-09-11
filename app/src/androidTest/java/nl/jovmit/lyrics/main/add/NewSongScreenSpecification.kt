package nl.jovmit.lyrics.main.add

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.UnavailableSongService
import nl.jovmit.lyrics.main.testModuleWithCustomSongsService
import nl.jovmit.lyrics.utils.IdGenerator
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import kotlin.LazyThreadSafetyMode.NONE

@RunWith(AndroidJUnit4::class)
class NewSongScreenSpecification {

    private val module by lazy(NONE) {
        val songsService = InMemorySongsService(IdGenerator(), emptyList())
        testModuleWithCustomSongsService(songsService)
    }

    @Before
    fun setUp() {
        loadKoinModules(module)
    }

    @Test
    fun should_show_empty_song_title_error() {
        launchNewSongScreen {} submit {
            emptySongTitleErrorIsDisplayed()
        }
    }

    @Test
    fun should_show_empty_song_performer_error() {
        launchNewSongScreen {
            typeSongTitle("Song title")
        } submit {
            emptySongPerformerErrorIsDisplayed()
        }
    }

    @Test
    fun should_show_empty_song_lyrics_error() {
        launchNewSongScreen {
            typeSongTitle("Usher")
            typeSongPerformer("Yeah")
        } submit {
            emptySongLyricsErrorIsDisplayed()
        }
    }

    @Test
    fun should_store_new_song() {
        launchNewSongScreen {
            typeSongTitle("New song title")
            typeSongPerformer("Song performer")
            typeSongLyrics("Song lyrics goes here...")
        } submit {
            verifySongBeingSaved()
        }
    }

    @Test
    fun should_show_error_saving_song() = runBlocking<Unit> {
        unloadKoinModules(module)
        val songService = UnavailableSongService()
        module.factory<SongsService>(override = true) { songService }
        loadKoinModules(module)

        launchNewSongScreen {
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
        val resetModule = module {
            factory<SongsService>(override = true) { InMemorySongsService(get()) }
        }
        loadKoinModules(resetModule)
    }
}
