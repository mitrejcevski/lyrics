package nl.jovmit.lyrics.main.register

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.main.MainActivity

private typealias MainActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

@DslMarker
annotation class RegistrationScreenRobot

fun launchRegistration(
  rule: MainActivityRule,
  block: RegisterScreenRobot.() -> Unit
): RegisterScreenRobot {
  return RegisterScreenRobot(rule).apply(block)
}

@RegistrationScreenRobot
class RegisterScreenRobot(
  private val rule: MainActivityRule
) {

  @OptIn(ExperimentalTestApi::class)
  fun tapOnAlreadyHaveAnAccount() {
    val hasAccount = rule.activity.getString(R.string.alreadyRegistered)
    rule.onNodeWithText(hasAccount).performClick()
  }

  fun typeUsername(username: String) {
    rule.onNodeWithTag("username").performTextInput(username)
  }

  fun typePassword(password: String) {
    rule.onNodeWithTag("password").performTextInput(password)
  }

  fun tapOnCreateAccount() {
    rule.onNodeWithTag("registerAction").performClick()
  }

  infix fun verify(block: RegisterVerificationRobot.() -> Unit): RegisterVerificationRobot {
    return RegisterVerificationRobot(rule).apply(block)
  }
}

@RegistrationScreenRobot
class RegisterVerificationRobot(
  private val rule: MainActivityRule
) {

  fun loginScreenIsDisplayed() {
    val login = rule.activity.getString(R.string.login)
    rule.onAllNodesWithText(login)[0].assertIsDisplayed()
  }

  fun emptyUsernameErrorIsDisplayed() {
    val usernameRequired = rule.activity.getString(R.string.errorEmptyUsername)
    rule.onNodeWithText(usernameRequired).assertIsDisplayed()
  }

  fun emptyPasswordErrorIsDisplayed() {
    val passwordRequired = rule.activity.getString(R.string.errorEmptyPassword)
    rule.onNodeWithText(passwordRequired).assertIsDisplayed()
  }

  @OptIn(ExperimentalTestApi::class)
  fun songsOverviewScreenIsDisplayed() {
    val search = rule.activity.getString(R.string.search)
    rule.waitUntilExactlyOneExists(hasText(search))
    rule.onNodeWithTag("newSong")
      .assertIsDisplayed()
  }

  fun usernameTakenErrorIsDisplayed() {
    val registrationError = rule.activity.getString(R.string.errorUsernameTaken)
    rule.onNodeWithText(registrationError).assertIsDisplayed()
  }

  fun offlineErrorIsDisplayed() {
    val offlineError = rule.activity.getString(R.string.errorNoNetworkConnection)
    rule.onNodeWithText(offlineError).assertIsDisplayed()
  }
}

