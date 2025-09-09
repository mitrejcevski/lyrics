package nl.jovmit.lyrics.main.details

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongId
import nl.jovmit.lyrics.main.data.song.SongLyrics
import nl.jovmit.lyrics.main.data.song.SongPerformer
import nl.jovmit.lyrics.main.data.song.SongTitle
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
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

  @get:Rule
  val rule = createAndroidComposeRule<MainActivity>()

  private val songsOverviewModule = module {
    factory<SongsService> { InMemorySongsService(get(), listOf(song)) }
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

  @Test
  fun should_prompt_before_deleting_song() {
    launchSongsOverviewScreen(rule) {
      tapOnSongWithTitle(song.songTitle.value)
      tapOnDeleteAction()
    } verify {
      deleteSongConfirmationIsShown()
    }
  }

  @After
  fun tearDown() {
    unloadKoinModules(songsOverviewModule)
    val resetModule = module {
      factory<SongsService> { InMemorySongsService(get()) }
    }
    loadKoinModules(resetModule)
  }
}
