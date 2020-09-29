package nl.jovmit.lyrics.main.register

import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.action.ViewActions.*
import nl.jovmit.lyrics.*
import nl.jovmit.lyrics.main.MainActivity

@DslMarker
annotation class RegistrationScreenRobot

fun launchRegistration(
    block: RegisterScreenRobot.() -> Unit
): RegisterScreenRobot {
    launch(MainActivity::class.java)
    return RegisterScreenRobot().apply(block)
}

@RegistrationScreenRobot
class RegisterScreenRobot {

    fun tapOnAlreadyHaveAnAccount() {
        R.id.registrationAlreadyRegisteredLabel perform click()
    }

    fun typeUsername(username: String) {
        R.id.registrationUsernameEditText.perform(typeText(username), closeSoftKeyboard())
    }

    fun typePassword(password: String) {
        R.id.registrationPasswordEditText.perform(typeText(password), closeSoftKeyboard())
    }

    fun tapOnCreateAccount() {
        R.id.registrationRegisterButton perform click()
    }

    infix fun verify(block: RegisterVerificationRobot.() -> Unit): RegisterVerificationRobot {
        return RegisterVerificationRobot().apply(block)
    }
}

@RegistrationScreenRobot
class RegisterVerificationRobot {

    fun loginScreenIsDisplayed() {
        R.id.loginUsernameEditText check isDisplayed
        R.id.loginPasswordEditText check isDisplayed
    }

    fun emptyUsernameErrorIsDisplayed() {
        text(R.string.errorEmptyUsername) check isDisplayed
    }

    fun emptyPasswordErrorIsDisplayed() {
        text(R.string.errorEmptyPassword) check isDisplayed
    }

    fun songsOverviewScreenIsDisplayed() {
        R.id.songsOverviewRecycler check isDisplayed
    }

    fun usernameTakenErrorIsDisplayed() {
        text(R.string.errorUsernameTaken) check isDisplayed
    }

    fun offlineErrorIsDisplayed() {
        text(R.string.errorNoNetworkConnection) check isDisplayed
    }
}

