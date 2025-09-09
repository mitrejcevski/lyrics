package nl.jovmit.lyrics.main.login

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.KoinTestRule
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.auth.AuthenticationService
import nl.jovmit.lyrics.main.auth.InMemoryAuthService
import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.preferences.InMemoryPreferencesManager
import nl.jovmit.lyrics.main.preferences.PreferencesManager
import nl.jovmit.lyrics.main.stubs.OfflineAuthService
import nl.jovmit.lyrics.utils.IdGenerator
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class LoginScreenSpecification {

  private val username = "validUsername"
  private val password = "validPassword"
  private val registrationData = RegistrationData(username, password, "::about::")
  private val authService = InMemoryAuthService(IdGenerator())

  private val loginModule = module {
    factory<PreferencesManager> { InMemoryPreferencesManager() }
    factory<AuthenticationService> { authService }
  }

  @get:Rule(order = 1)
  val koinRule = KoinTestRule(listOf(loginModule))

  @get:Rule(order = 2)
  val rule = createAndroidComposeRule<MainActivity>()

  @Test
  fun should_perform_login() {
    runBlocking { authService.createUser(registrationData) }

    launchLogin(rule) {
      typeUsername(username)
      typePassword(password)
      tapOnLogin()
    } verify {
      songsOverviewScreenIsDisplayed()
    }
  }

  @Test
  fun should_show_empty_username_error() {
    launchLogin(rule) {
      tapOnLogin()
    } verify {
      emptyUsernameErrorIsDisplayed()
    }
  }

  @Test
  fun should_show_empty_password_error() {
    launchLogin(rule) {
      typeUsername(username)
      tapOnLogin()
    } verify {
      emptyPasswordErrorIsDisplayed()
    }
  }

  @Test
  fun should_display_incorrect_credentials_error() = runBlocking<Unit> {
    val registrationData = RegistrationData(username.reversed(), password, "::about::")
    authService.createUser(registrationData)
    launchLogin(rule) {
      typeUsername(username)
      typePassword(password)
      tapOnLogin()
    } verify {
      wrongCredentialsErrorIsDisplayed()
    }
  }

  @Test
  fun should_display_offline_error() {
    val replaceModule = module {
      factory<AuthenticationService>() { OfflineAuthService() }
    }
    loadKoinModules(replaceModule)

    launchLogin(rule) {
      typeUsername(username)
      typePassword(password)
      tapOnLogin()
    } verify {
      offlineErrorIsDisplayed()
    }

    unloadKoinModules(replaceModule)
  }

  @After
  fun tearDown() {
    unloadKoinModules(loginModule)
    val resetModule = module {
      factory<PreferencesManager> { InMemoryPreferencesManager() }
      factory<AuthenticationService> { InMemoryAuthService(get()) }
    }
    loadKoinModules(resetModule)
  }
}
