package nl.jovmit.lyrics.main.register

import android.content.Intent
import androidx.test.espresso.action.ViewActions.click
import androidx.test.rule.ActivityTestRule
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.check
import nl.jovmit.lyrics.isDisplayed
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.perform

@DslMarker
annotation class RegistrationScreenRobot

fun launchRegistration(
    rule: ActivityTestRule<MainActivity>,
    block: RegisterScreenRobot.() -> Unit
): RegisterScreenRobot {
    rule.launchActivity(Intent())
    return RegisterScreenRobot().apply(block)
}

@RegistrationScreenRobot
class RegisterScreenRobot {

    fun tapOnAlreadyHaveAnAccount() {
        R.id.registrationAlreadyRegisteredLabel perform click()
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
}

