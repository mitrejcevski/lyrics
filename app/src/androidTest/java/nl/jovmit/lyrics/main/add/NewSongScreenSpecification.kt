package nl.jovmit.lyrics.main.add

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import nl.jovmit.lyrics.common.AppCoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.InfoViewModel
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.overview.SongsRepository
import nl.jovmit.lyrics.main.overview.SongsViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class NewSongScreenSpecification {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java, true, false)

    private val newSongModule = module {
        single<CoroutineDispatchers> { AppCoroutineDispatchers() }
        single<SongsService> { InMemorySongsService() }
        factory { NewSongRepository(get()) }
        factory { SongsRepository(get()) }
        viewModel { NewSongViewModel(get(), get()) }
        viewModel { InfoViewModel() }
        viewModel { SongsViewModel(get(), get()) }
    }

    @Before
    fun setUp() {
        loadKoinModules(newSongModule)
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
        unloadKoinModules(newSongModule)
    }
}