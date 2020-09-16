package nl.jovmit.lyrics.main.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.auth.AuthenticationService
import nl.jovmit.lyrics.main.auth.InMemoryAuthService
import nl.jovmit.lyrics.main.data.user.RegistrationData
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

    @After
    fun tearDown() {
        unloadKoinModules(loginModule)
        val resetModule = module {
            factory<PreferencesManager>(override = true) { InMemoryPreferencesManager() }
            factory<AuthenticationService>(override = true) { InMemoryAuthService(get()) }
        }
        loadKoinModules(resetModule)
    }
}
