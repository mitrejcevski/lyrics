package nl.jovmit.lyrics.main.register

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.KoinTestRule
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.auth.AuthenticationRepository
import nl.jovmit.lyrics.main.auth.AuthenticationService
import nl.jovmit.lyrics.main.auth.InMemoryAuthService
import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.preferences.InMemoryPreferencesManager
import nl.jovmit.lyrics.main.preferences.PreferencesManager
import nl.jovmit.lyrics.utils.IdGenerator
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class RegisterScreenSpecification {

  private val username = "validUsername"
  private val password = "validPassword"
  private val about = "about"
  private val preferencesManager = InMemoryPreferencesManager()
  private val authService = InMemoryAuthService(IdGenerator())

  private val registrationModule = module {
    factory<PreferencesManager> { preferencesManager }
    factory<AuthenticationService> { authService }
    factory { AuthenticationRepository(get()) }
    factory<SongsService> { InMemorySongsService(IdGenerator()) }
  }

  @get:Rule(order = 1)
  val koinRule = KoinTestRule(listOf(registrationModule))

  @get:Rule(order = 2)
  val rule = createAndroidComposeRule<MainActivity>()

  @Test
  fun should_open_login() {
    launchRegistration(rule) {
      tapOnAlreadyHaveAnAccount()
    } verify {
      loginScreenIsDisplayed()
    }
  }

  @Test
  fun should_show_empty_username_error() {
    launchRegistration(rule) {
      tapOnCreateAccount()
    } verify {
      emptyUsernameErrorIsDisplayed()
    }
  }

  @Test
  fun should_show_empty_password_error() {
    launchRegistration(rule) {
      typeUsername(username)
      tapOnCreateAccount()
    } verify {
      emptyPasswordErrorIsDisplayed()
    }
  }

  @Test
  fun should_perform_registration() {
    launchRegistration(rule) {
      typeUsername(username)
      typePassword(password)
      tapOnCreateAccount()
    } verify {
      songsOverviewScreenIsDisplayed()
    }
  }

  @Test
  fun should_display_username_taken_error() = runBlocking<Unit> {
    authService.createUser(RegistrationData(username, password, about))
    launchRegistration(rule) {
      typeUsername(username)
      typePassword(password)
      tapOnCreateAccount()
    } verify {
      usernameTakenErrorIsDisplayed()
    }
  }

  @Test
  fun should_display_offline_error() {
    authService.setOffline()

    launchRegistration(rule) {
      typeUsername(username)
      typePassword(password)
      tapOnCreateAccount()
    } verify {
      offlineErrorIsDisplayed()
    }
  }
}
