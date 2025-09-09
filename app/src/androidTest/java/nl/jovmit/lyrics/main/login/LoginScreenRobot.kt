package nl.jovmit.lyrics.main.login

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.main.MainActivity

private typealias MainActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

@DslMarker
annotation class LoginRobot

@OptIn(ExperimentalTestApi::class)
fun launchLogin(
  rule: MainActivityRule,
  block: LoginScreenRobot.() -> Unit
): LoginScreenRobot {
  rule.waitUntilExactlyOneExists(hasTestTag("gotAccountLabel"))
  rule.onNodeWithTag("gotAccountLabel").performClick()
  return LoginScreenRobot(rule).apply(block)
}

@LoginRobot
class LoginScreenRobot(
  private val rule: MainActivityRule
) {

  fun typeUsername(username: String) {
    rule.onNodeWithTag("username")
      .performTextInput(username)
  }

  fun typePassword(password: String) {
    rule.onNodeWithTag("password")
      .performTextInput(password)
  }

  fun tapOnLogin() {
    rule.onNodeWithTag("loginButton")
      .performClick()
  }

  infix fun verify(
    function: LoginVerificationRobot.() -> Unit
  ): LoginVerificationRobot {
    return LoginVerificationRobot(rule).apply(function)
  }
}

@LoginRobot
class LoginVerificationRobot(
  private val rule: MainActivityRule
) {

  fun songsOverviewScreenIsDisplayed() {
    rule.onNodeWithTag("newSong")
      .assertIsDisplayed()
  }

  fun emptyUsernameErrorIsDisplayed() {
    val usernameRequired = rule.activity.getString(R.string.errorEmptyUsername)
    rule.onNodeWithText(usernameRequired).assertIsDisplayed()
  }

  fun emptyPasswordErrorIsDisplayed() {
    val passwordRequired = rule.activity.getString(R.string.errorEmptyPassword)
    rule.onNodeWithText(passwordRequired).assertIsDisplayed()
  }

  fun wrongCredentialsErrorIsDisplayed() {
    val loginError = rule.activity.getString(R.string.errorIncorrectCredentials)
    rule.onNodeWithText(loginError).assertIsDisplayed()
  }

  fun offlineErrorIsDisplayed() {
    val offlineError = rule.activity.getString(R.string.errorNoNetworkConnection)
    rule.onNodeWithText(offlineError).assertIsDisplayed()
  }
}
