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
import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongLyrics
import nl.jovmit.lyrics.main.data.song.SongPerformer
import nl.jovmit.lyrics.main.data.song.SongTitle
import nl.jovmit.lyrics.main.overview.SongsRepository
import nl.jovmit.lyrics.main.overview.SongsViewModel
import org.junit.*
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class SongDetailsScreenSpecification {

    private val songs = listOf(
        Song(
            SongTitle("::irrelevant song title::"),
            SongPerformer("::irrelevant song performer::"),
            SongLyrics("::irrelevant song lyrics::")
        )
    )

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java, true, false)

    private val songsOverviewModule = module {
        single<CoroutineDispatchers> { AppCoroutineDispatchers() }
        single<SongsService> { ReadOnlyServiceContaining(songs) }
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
    fun should_open_song_details_screen() {
        launchSongsOverviewScreen(rule) {
            tapOnSongWithTitle(songs.first().songTitle.value)
        } verify {
            songDetailsScreenIsOpened()
        }
    }

    @Test
    @Ignore("Not there yet")
    fun should_display_all_song_properties() {

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

        override suspend fun addNewSong(newSong: Song) {
            throw IllegalStateException("Irrelevant for this testcase")
        }
    }
}