package nl.jovmit.lyrics.main.login

import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.action.ViewActions.*
import nl.jovmit.lyrics.*
import nl.jovmit.lyrics.main.MainActivity

@DslMarker
annotation class LoginRobot

fun launchLogin(
    block: LoginScreenRobot.() -> Unit
): LoginScreenRobot {
    launch(MainActivity::class.java)
    R.id.registrationAlreadyRegisteredLabel perform click()
    return LoginScreenRobot().apply(block)
}

@LoginRobot
class LoginScreenRobot {

    fun typeUsername(username: String) {
        R.id.loginUsernameEditText.perform(typeText(username), closeSoftKeyboard())
    }

    fun typePassword(password: String) {
        R.id.loginPasswordEditText.perform(typeText(password), closeSoftKeyboard())
    }

    fun tapOnLogin() {
        R.id.loginActionButton perform click()
    }

    infix fun verify(
        function: LoginVerificationRobot.() -> Unit
    ): LoginVerificationRobot {
        return LoginVerificationRobot().apply(function)
    }
}

@LoginRobot
class LoginVerificationRobot {

    fun songsOverviewScreenIsDisplayed() {
        R.id.songsOverviewRecycler check isDisplayed
    }

    fun emptyUsernameErrorIsDisplayed() {
        text(R.string.errorEmptyUsername) check isDisplayed
    }

    fun emptyPasswordErrorIsDisplayed() {
        text(R.string.errorEmptyPassword) check isDisplayed
    }

    fun wrongCredentialsErrorIsDisplayed() {
        text(R.string.errorIncorrectCredentials) check isDisplayed
    }

    fun offlineErrorIsDisplayed() {
        text(R.string.errorNoNetworkConnection) check isDisplayed
    }
}
