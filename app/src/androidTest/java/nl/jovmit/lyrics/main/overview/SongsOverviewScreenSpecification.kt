package nl.jovmit.lyrics.main.overview

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import nl.jovmit.lyrics.KoinTestRule
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.auth.AuthenticationRepository
import nl.jovmit.lyrics.main.auth.InMemoryAuthService
import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongId
import nl.jovmit.lyrics.main.data.song.SongLyrics
import nl.jovmit.lyrics.main.data.song.SongPerformer
import nl.jovmit.lyrics.main.data.song.SongTitle
import nl.jovmit.lyrics.main.data.user.User
import nl.jovmit.lyrics.main.preferences.InMemoryPreferencesManager
import nl.jovmit.lyrics.main.preferences.PreferencesManager
import nl.jovmit.lyrics.main.stubs.SongsServiceUnableToFetchSongs
import nl.jovmit.lyrics.main.stubs.SongsServiceUnableToSearchSongs
import nl.jovmit.lyrics.utils.IdGenerator
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class SongsOverviewScreenSpecification {

  private val song = Song(
    SongId("SongId"),
    SongTitle("Song One"),
    SongPerformer("First Singer"),
    SongLyrics("The lyrics of the song")
  )
  private val anotherSong = Song(
    SongId("AnotherSongId"),
    SongTitle("Song Two"),
    SongPerformer("Second Singer"),
    SongLyrics("The text of the song")
  )
  private val emptySongsList = emptyList<Song>()
  private val songsList = listOf(song, anotherSong)

  private val songsOverviewModule = module {
    factory<SongsService> { InMemorySongsService(IdGenerator(), songsList) }
    factory<PreferencesManager> {
      InMemoryPreferencesManager().also {
        val loggedInUser = User("userId", "username", "password", "about")
        it.loggedInUser(loggedInUser)
      }
    }
    factory { AuthenticationRepository(InMemoryAuthService(IdGenerator())) }
  }

  @get:Rule(order = 1)
  val koinRule = KoinTestRule(listOf(songsOverviewModule))

  @get:Rule(order = 2)
  val rule = createAndroidComposeRule<MainActivity>()

  @Before
  fun set_up() {
    loadKoinModules(songsOverviewModule)
  }

  @Test
  fun should_display_empty_state_when_no_songs_added() {
    val replaceModule = setupModule(InMemorySongsService(IdGenerator(), emptySongsList))

    launchSongsOverview(rule) { } verify {
      songsEmptyStateIsDisplayed()
    }

    unloadKoinModules(replaceModule)
  }

  @Test
  fun should_not_display_empty_state_when_songs_added() {
    launchSongsOverview(rule) { } verify {
      songsEmptyStateIsGone()
    }
  }

  @Test
  fun should_display_loaded_songs() {
    launchSongsOverview(rule) { } verify {
      songTitleAndSingerAreDisplayed(song)
      songTitleAndSingerAreDisplayed(anotherSong)
    }
  }

  @Test
  fun should_display_error_if_loading_songs_fails() {
    val songsService = SongsServiceUnableToFetchSongs()
    val replaceModule = setupModule(songsService)

    launchSongsOverview(rule) { } verify {
      loadingErrorIsDisplayed()
    }

    unloadKoinModules(replaceModule)
  }

  @Test
  fun should_open_new_song_screen_upon_click_on_new_song_button() {
    launchSongsOverview(rule) {
      clickOnNewSongButton()
    } verify {
      newSongScreenIsDisplayed()
    }
  }

  @Test
  fun should_open_song_details_screen() {
    launchSongsOverview(rule) {
      tapOnSongWithTitle(song.songTitle.value)
    } verify {
      songDetailsScreenIsOpened()
    }
  }

  @Test
  fun should_perform_search() {
    val queryMatchingFirstSong = song.songTitle.value
    launchSongsOverview(rule) {
      typeSearchQuery(queryMatchingFirstSong)
    } verify {
      songTitleAndSingerAreDisplayed(song)
      songTitleAndSingerNotDisplayed(anotherSong)
    }
  }

  @Test
  fun should_display_all_songs_once_search_is_closed() {
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
  fun should_display_search_error() {
    val query = "query"
    setupModule(SongsServiceUnableToSearchSongs())

    launchSongsOverview(rule) {
      typeSearchQuery(query)
    } verify {
      searchErrorIsDisplayed()
    }
  }

  @Test
  fun should_log_out_user() {
    launchSongsOverview(rule) {
      tapOnLogout()
    } verify {
      registrationScreenIsDisplayed()
    }
  }

  private fun setupModule(songsService: SongsService): Module {
    val module = module {
      factory<SongsService> { songsService }
    }
    loadKoinModules(module)
    return module
  }

  @After
  fun tear_down() {
    unloadKoinModules(songsOverviewModule)
    val resetModule = module {
      factory<SongsService> { InMemorySongsService(get()) }
      factory<PreferencesManager> { InMemoryPreferencesManager() }
    }
    loadKoinModules(resetModule)
  }
}
