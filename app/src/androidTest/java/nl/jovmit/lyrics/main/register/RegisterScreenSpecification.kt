package nl.jovmit.lyrics.main.register

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.auth.AuthenticationService
import nl.jovmit.lyrics.main.auth.InMemoryAuthService
import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.data.user.User
import nl.jovmit.lyrics.main.preferences.InMemoryPreferencesManager
import nl.jovmit.lyrics.main.preferences.PreferencesManager
import nl.jovmit.lyrics.main.stubs.OfflineAuthService
import nl.jovmit.lyrics.utils.IdGenerator
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import java.util.*

@RunWith(AndroidJUnit4::class)
class RegisterScreenSpecification {

    private val userId = UUID.randomUUID().toString()
    private val username = "validUsername"
    private val password = "validPassword"
    private val about = "about"
    private val preferencesManager = InMemoryPreferencesManager()
    private val authService = InMemoryAuthService(IdGenerator())
    private val loggedInUser = User(userId, username, password, about)

    private val registrationModule = module {
        factory<PreferencesManager>() { preferencesManager }
        factory<AuthenticationService>() { authService }
    }

    @Before
    fun setUp() {
        loadKoinModules(registrationModule)
    }

    @Test
    fun should_open_login() {
        launchRegistration {
            tapOnAlreadyHaveAnAccount()
        } verify {
            loginScreenIsDisplayed()
        }
    }

    @Test
    fun should_show_empty_username_error() {
        launchRegistration {
            tapOnCreateAccount()
        } verify {
            emptyUsernameErrorIsDisplayed()
        }
    }

    @Test
    fun should_show_empty_password_error() {
        launchRegistration {
            typeUsername(username)
            tapOnCreateAccount()
        } verify {
            emptyPasswordErrorIsDisplayed()
        }
    }

    @Test
    fun should_perform_registration() {
        launchRegistration {
            typeUsername(username)
            typePassword(password)
            tapOnCreateAccount()
        } verify {
            songsOverviewScreenIsDisplayed()
        }
    }

    @Test
    fun should_skip_registration_when_user_was_logged_in() {
        preferencesManager.loggedInUser(loggedInUser)
        launchRegistration {
            //no operation
        } verify {
            songsOverviewScreenIsDisplayed()
        }
    }

    @Test
    fun should_display_username_taken_error() = runBlocking<Unit> {
        authService.createUser(RegistrationData(username, password, about))
        launchRegistration {
            typeUsername(username)
            typePassword(password)
            tapOnCreateAccount()
        } verify {
            usernameTakenErrorIsDisplayed()
        }
    }

    @Test
    fun should_display_offline_error() {
        val replaceModule = module {
            factory<AuthenticationService>() { OfflineAuthService() }
        }
        loadKoinModules(replaceModule)

        launchRegistration {
            typeUsername(username)
            typePassword(password)
            tapOnCreateAccount()
        } verify {
            offlineErrorIsDisplayed()
        }

        unloadKoinModules(replaceModule)
    }

    @After
    fun tearDown() {
        unloadKoinModules(registrationModule)
        val resetModule = module {
            factory<PreferencesManager>() { InMemoryPreferencesManager() }
            factory<AuthenticationService>() { InMemoryAuthService(get()) }
        }
        loadKoinModules(resetModule)
    }
}
