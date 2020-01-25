package nl.jovmit.lyrics.main.details

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import nl.jovmit.lyrics.common.AppCoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.main.InfoViewModel
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.add.NewSongRepository
import nl.jovmit.lyrics.main.add.NewSongViewModel
import nl.jovmit.lyrics.main.data.song.*
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
class SongDetailsScreenSpecification {

    private val song = Song(
        SongId("::irrelevant song id::"),
        SongTitle("::irrelevant song title::"),
        SongPerformer("::irrelevant song performer::"),
        SongLyrics("::irrelevant song lyrics::")
    )

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java, true, false)

    private val songsOverviewModule = module {
        single<CoroutineDispatchers> { AppCoroutineDispatchers() }
        single<SongsService> { ReadOnlyServiceContaining(listOf(song)) }
        factory { SongsRepository(get()) }
        factory { NewSongRepository(get()) }
        viewModel { SongsViewModel(get(), get()) }
        viewModel { NewSongViewModel(get(), get()) }
        viewModel { InfoViewModel() }
    }

    @Before
    fun setUp() {
        loadKoinModules(songsOverviewModule)
    }

    @Test
    fun should_display_all_song_properties() {
        launchSongsOverviewScreen(rule) {
            tapOnSongWithTitle(song.songTitle.value)
        } verify {
            songTitleIsDisplayed(song)
            songPerformerIsDisplayed(song)
            songLyricsIsDisplayed(song)
        }
    }

    @After
    fun tearDown() {
        unloadKoinModules(songsOverviewModule)
    }

    inner class ReadOnlyServiceContaining(
        private val songs: List<Song>
    ) : SongsService {

        override suspend fun fetchAllSongs(): List<Song> {
            return songs
        }

        override suspend fun addNewSong(newSongData: SongData) {
            throw IllegalStateException("Irrelevant for this testcase")
        }
    }
}