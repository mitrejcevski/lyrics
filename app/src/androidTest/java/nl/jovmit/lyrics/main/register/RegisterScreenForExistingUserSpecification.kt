package nl.jovmit.lyrics.main.register

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import nl.jovmit.lyrics.KoinTestRule
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.auth.AuthenticationRepository
import nl.jovmit.lyrics.main.auth.AuthenticationService
import nl.jovmit.lyrics.main.auth.InMemoryAuthService
import nl.jovmit.lyrics.main.data.user.User
import nl.jovmit.lyrics.main.preferences.InMemoryPreferencesManager
import nl.jovmit.lyrics.main.preferences.PreferencesManager
import nl.jovmit.lyrics.utils.IdGenerator
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class RegisterScreenForExistingUserSpecification {

  private val userId = UUID.randomUUID().toString()
  private val username = "validUsername"
  private val password = "validPassword"
  private val about = "about"
  private val loggedInUser = User(userId, username, password, about)

  private val authService = InMemoryAuthService(IdGenerator())
  private val preferencesManager = InMemoryPreferencesManager().apply {
    loggedInUser(loggedInUser)
  }

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
  fun should_skip_registration_when_user_was_logged_in() {
    launchRegistration(rule) {
      //no operation
    } verify {
      songsOverviewScreenIsDisplayed()
    }
  }
}