package nl.jovmit.lyrics.app

import nl.jovmit.lyrics.common.AppCoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.InfoViewModel
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.add.NewSongRepository
import nl.jovmit.lyrics.main.add.NewSongViewModel
import nl.jovmit.lyrics.main.auth.AuthenticationRepository
import nl.jovmit.lyrics.main.auth.AuthenticationService
import nl.jovmit.lyrics.main.auth.CredentialsValidator
import nl.jovmit.lyrics.main.auth.InMemoryAuthService
import nl.jovmit.lyrics.main.data.user.User
import nl.jovmit.lyrics.main.details.SongDetailsViewModel
import nl.jovmit.lyrics.main.edit.UpdateSongViewModel
import nl.jovmit.lyrics.main.overview.SongsRepository
import nl.jovmit.lyrics.main.overview.SongsViewModel
import nl.jovmit.lyrics.main.preferences.InMemoryPreferencesManager
import nl.jovmit.lyrics.main.preferences.PreferencesManager
import nl.jovmit.lyrics.main.preferences.UserPreferencesViewModel
import nl.jovmit.lyrics.main.register.RegisterViewModel
import nl.jovmit.lyrics.utils.IdGenerator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val testAppModule = module {

    factory<CoroutineDispatchers> { AppCoroutineDispatchers() }
    factory<SongsService> { InMemorySongsService(get()) }
    factory<AuthenticationService> { InMemoryAuthService(get()) }
    factory<PreferencesManager> {
        InMemoryPreferencesManager().also {
            val loggedInUser = User("userId", "username", "about")
            it.loggedInUser(loggedInUser)
        }
    }
    factory { IdGenerator() }
    factory { NewSongRepository(get()) }
    factory { SongsRepository(get()) }
    factory { AuthenticationRepository(get()) }
    factory { CredentialsValidator() }

    viewModel { NewSongViewModel(get(), get()) }
    viewModel { InfoViewModel() }
    viewModel { SongsViewModel(get(), get()) }
    viewModel { SongDetailsViewModel(get(), get()) }
    viewModel { UpdateSongViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get(), get()) }
    viewModel { UserPreferencesViewModel(get()) }
}
