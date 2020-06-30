package nl.jovmit.lyrics.main.register

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import nl.jovmit.lyrics.main.InfoViewModel
import nl.jovmit.lyrics.main.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class RegisterScreenSpecification {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java, true, false)

    private val registrationModule = module {
        viewModel { InfoViewModel() }
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

    @After
    fun tearDown() {
        unloadKoinModules(registrationModule)
    }
}