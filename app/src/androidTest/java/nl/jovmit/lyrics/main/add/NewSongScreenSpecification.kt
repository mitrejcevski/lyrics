package nl.jovmit.lyrics.main.add

import androidx.test.ext.junit.runners.AndroidJUnit4
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.stubs.SongsServiceUnableToAddSong
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class NewSongScreenSpecification {

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
    fun should_show_error_saving_song() {
        val replaceModule = module {
            factory<SongsService>() { SongsServiceUnableToAddSong() }
        }
        loadKoinModules(replaceModule)

        launchNewSongScreen {
            typeSongTitle("Song title")
            typeSongPerformer("Song performer")
            typeSongLyrics("Song lyrics")
        } submit {
            verifyErrorSavingSong()
        }

        unloadKoinModules(replaceModule)
    }

    @After
    fun tearDown() {
        val resetModule = module {
            factory<SongsService>() { InMemorySongsService(get()) }
        }
        loadKoinModules(resetModule)
    }
}

