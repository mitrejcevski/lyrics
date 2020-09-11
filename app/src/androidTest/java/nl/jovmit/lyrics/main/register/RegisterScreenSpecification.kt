package nl.jovmit.lyrics.main.register

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.data.user.User
import nl.jovmit.lyrics.main.preferences.InMemoryPreferencesManager
import nl.jovmit.lyrics.main.preferences.PreferencesManager
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import java.util.*

@RunWith(AndroidJUnit4::class)
class RegisterScreenSpecification {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java, true, false)

    private val userId = UUID.randomUUID().toString()
    private val username = "validUsername"
    private val password = "validPassword"
    private val about = "about"
    private val preferencesManager = InMemoryPreferencesManager()
    private val loggedInUser = User(userId, username, about)

    private val registrationModule = module {
        factory<PreferencesManager>(override = true) { preferencesManager }
    }

    @Before
    fun setUp() {
        loadKoinModules(registrationModule)
    }

    @Test
    fun should_open_login() {
        launchRegistration(rule) {
            tapOnAlreadyHaveAnAccount()
        } verify {
            loginScreenIsDisplayed()
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
    fun should_skip_registration_when_user_was_logged_in() {
        preferencesManager.loggedInUser(loggedInUser)
        launchRegistration(rule) {
            //no operation
        } verify {
            songsOverviewScreenIsDisplayed()
        }
    }

    @After
    fun tearDown() {
        unloadKoinModules(registrationModule)
        val resetModule = module {
            factory<PreferencesManager>(override = true) { InMemoryPreferencesManager() }
        }
        loadKoinModules(resetModule)
    }
}
