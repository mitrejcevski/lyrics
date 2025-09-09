package nl.jovmit.lyrics.main

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import nl.jovmit.lyrics.BuildConfig
import nl.jovmit.lyrics.common.AppCoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.main.add.NewSongViewModel
import nl.jovmit.lyrics.main.auth.AuthenticationRepository
import nl.jovmit.lyrics.main.auth.AuthenticationService
import nl.jovmit.lyrics.main.auth.CredentialsValidator
import nl.jovmit.lyrics.main.auth.InMemoryAuthService
import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongId
import nl.jovmit.lyrics.main.data.song.SongLyrics
import nl.jovmit.lyrics.main.data.song.SongPerformer
import nl.jovmit.lyrics.main.data.song.SongTitle
import nl.jovmit.lyrics.main.details.SongDetailsViewModel
import nl.jovmit.lyrics.main.edit.UpdateSongViewModel
import nl.jovmit.lyrics.main.login.LoginViewModel
import nl.jovmit.lyrics.main.overview.SongsRepository
import nl.jovmit.lyrics.main.overview.SongsViewModel
import nl.jovmit.lyrics.main.preferences.PreferencesManager
import nl.jovmit.lyrics.main.preferences.SharedPreferencesManager
import nl.jovmit.lyrics.main.preferences.UserPreferencesViewModel
import nl.jovmit.lyrics.main.register.RegisterViewModel
import nl.jovmit.lyrics.utils.IdGenerator
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
  single<SharedPreferences> {
    get<Context>().getSharedPreferences("lyrics", Context.MODE_PRIVATE)
  }
  single { FirebaseFirestore.getInstance() }
  single<CoroutineDispatchers> { AppCoroutineDispatchers() }
  single { IdGenerator() }
  single<SongsService> {
    if (BuildConfig.DEBUG) {
      InMemorySongsService(get(), generateSongs())
    } else {
      FirebaseSongsService(get())
    }
  }
  factory { SongsRepository(get()) }
  factory { CredentialsValidator() }
  single<AuthenticationService> { InMemoryAuthService(get()) }
  factory { AuthenticationRepository(get()) }
  single<PreferencesManager> { SharedPreferencesManager(get()) }

  viewModel { SongsViewModel(get(), get()) }
  viewModel { NewSongViewModel(get(), get()) }
  viewModel { SongDetailsViewModel(get(), get(), get()) }
  viewModel { UpdateSongViewModel(get(), get(), get()) }
  viewModel { RegisterViewModel(get(), get(), get(), get()) }
  viewModel { UserPreferencesViewModel(get()) }
  viewModel { LoginViewModel(get(), get(), get(), get()) }
}

fun generateSongs(): List<Song> {
  return listOf(
    Song(
      SongId("1"),
      SongTitle("Tears"),
      SongPerformer("Sabrina Carpenter"),
      SongLyrics("Some Lyrics Here")
    ),
    Song(
      SongId("2"),
      SongTitle("Lose Yourself"),
      SongPerformer("Eminem"),
      SongLyrics("Some Lyrics Here")
    ),
    Song(
      SongId("3"),
      SongTitle("I'm on my way"),
      SongPerformer("Alan Walker"),
      SongLyrics(
        """I'm sorry but, don't wanna talk
I need a moment before I go, it's nothing personal
I draw the blinds, they don't need to see me cry
'Cause even if they understand, they don't understand

So then, when I'm finished
I'm all 'bout my business and ready to save the world
I'm taking my misery, make it my bitch
Can't be everyone's favorite girl

So take aim and fire away
I've never been so wide awake
No, nobody but me can keep me safe
And I'm on my way
The blood moon is on the rise
The fire burning in my eyes
No, nobody but me can keep me safe
And I'm on my way""".trim()
      )
    ),
    Song(
      SongId("4"),
      SongTitle("Unstoppable"),
      SongPerformer("Sia"),
      SongLyrics("Some Lyrics Here")
    ),
    Song(
      SongId("5"),
      SongTitle("Flames"),
      SongPerformer("Sia"),
      SongLyrics("Some Lyrics Here")
    ),
    Song(
      SongId("6"),
      SongTitle("Mocking Bird"),
      SongPerformer("Eminem"),
      SongLyrics("Some Lyrics Here")
    ),
    Song(
      SongId("7"),
      SongTitle("Changes"),
      SongPerformer("2Pac"),
      SongLyrics("Some Lyrics Here")
    ),
    Song(
      SongId("8"),
      SongTitle("Blinding Lights"),
      SongPerformer("The Weeknd"),
      SongLyrics("Some Lyrics Here")
    ),
    Song(
      SongId("9"),
      SongTitle("Shape of You"),
      SongPerformer("Ed Sheeran"),
      SongLyrics("Some Lyrics Here")
    ),
    Song(
      SongId("10"),
      SongTitle("Hotel California"),
      SongPerformer("Eagles"),
      SongLyrics("Some Lyrics Here")
    ),
    Song(
      SongId("11"),
      SongTitle("Smells Like Teen Spirit"),
      SongPerformer("Nirvana"),
      SongLyrics("Some Lyrics Here")
    ),
    Song(
      SongId("12"),
      SongTitle("Rolling in the Deep"),
      SongPerformer("Adele"),
      SongLyrics("Some Lyrics Here")
    ),
    Song(
      SongId("13"),
      SongTitle("Bohemian Rhapsody"),
      SongPerformer("Queen"),
      SongLyrics("Some Lyrics Here")
    ),
    Song(
      SongId("14"),
      SongTitle("Billie Jean"),
      SongPerformer("Michael Jackson"),
      SongLyrics("Some Lyrics Here")
    ),
  )
}
