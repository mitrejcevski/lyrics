package nl.jovmit.lyrics.main.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.auth.AuthenticationService
import nl.jovmit.lyrics.main.auth.InMemoryAuthService
import nl.jovmit.lyrics.main.data.user.LoginData
import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.exceptions.NetworkUnavailableException
import nl.jovmit.lyrics.main.preferences.InMemoryPreferencesManager
import nl.jovmit.lyrics.main.preferences.PreferencesManager
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
class LoginScreenSpecification {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java, true, false)

    private val username = "validUsername"
    private val password = "validPassword"
    private val registrationData = RegistrationData(username, password, "::about::")
    private val preferencesManager = InMemoryPreferencesManager()
    private val authService = InMemoryAuthService(IdGenerator())

    private val loginModule = module {
        factory<PreferencesManager>(override = true) { preferencesManager }
        factory<AuthenticationService>(override = true) { authService }
    }

    @Before
    fun setUp() {
        loadKoinModules(loginModule)
    }

    @Test
    fun should_perform_login() = runBlocking<Unit> {
        authService.createUser(registrationData)
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
        val registrationData1 = RegistrationData(username.reversed(), password, "::about::")
        authService.createUser(registrationData1)
        launchLogin(rule) {
            typeUsername(username)
            typePassword(password)
            tapOnLogin()
        } verify {
            wrongCredentialsErrorIsDisplayed()
        }
    }

    @Test
    fun should_display_offline_error() = runBlocking {
        val replaceModule = moduleWithOfflineAuthService()
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
            factory<PreferencesManager>(override = true) { InMemoryPreferencesManager() }
            factory<AuthenticationService>(override = true) { InMemoryAuthService(get()) }
        }
        loadKoinModules(resetModule)
    }

    private suspend fun moduleWithOfflineAuthService(): Module {
        val offlineService = mock<AuthenticationService>()
        given(offlineService.login(LoginData(username, password)))
            .willThrow(NetworkUnavailableException())
        return module { factory(override = true) { offlineService } }
    }
}
