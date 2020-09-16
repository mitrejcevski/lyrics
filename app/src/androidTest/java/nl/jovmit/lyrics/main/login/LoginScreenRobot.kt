package nl.jovmit.lyrics.main.login

import android.content.Intent
import androidx.test.espresso.action.ViewActions.*
import androidx.test.rule.ActivityTestRule
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.check
import nl.jovmit.lyrics.isDisplayed
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.perform

@DslMarker
annotation class LoginRobot

fun launchLogin(
    rule: ActivityTestRule<MainActivity>,
    block: LoginScreenRobot.() -> Unit
): LoginScreenRobot {
    rule.launchActivity(Intent())
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
}
