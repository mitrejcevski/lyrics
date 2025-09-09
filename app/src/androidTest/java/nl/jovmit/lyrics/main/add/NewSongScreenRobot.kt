package nl.jovmit.lyrics.main.add

import androidx.compose.ui.test.assertIsDisplayed
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
annotation class NewSongScreenRobot

fun launchNewSongScreen(
  rule: MainActivityRule,
  block: NewSongRobot.() -> Unit
): NewSongRobot {
  rule.onNodeWithTag("newSong").performClick()
  return NewSongRobot(rule).apply(block)
}

@NewSongScreenRobot
class NewSongRobot(
  private val rule: MainActivityRule
) {

  fun typeSongTitle(title: String) {
    rule.onNodeWithTag("newSongTitle")
      .performTextInput(title)
  }

  fun typeSongPerformer(performer: String) {
    rule.onNodeWithTag("newSongPerformer")
      .performTextInput(performer)
  }

  fun typeSongLyrics(lyrics: String) {
    rule.onNodeWithTag("newSongLyrics")
      .performTextInput(lyrics)
  }

  infix fun submit(
    block: NewSongVerificationRobot.() -> Unit
  ): NewSongVerificationRobot {
    rule.onNodeWithTag("actionDone").performClick()
    return NewSongVerificationRobot(rule).apply(block)
  }
}

@NewSongScreenRobot
class NewSongVerificationRobot(
  private val rule: MainActivityRule
) {

  fun emptySongTitleErrorIsDisplayed() {
    val emptyTitleError = rule.activity.getString(R.string.errorEmptySongTitle)
    rule.onNodeWithText(emptyTitleError)
      .assertIsDisplayed()
  }

  fun emptySongPerformerErrorIsDisplayed() {
    val emptyPerformerError = rule.activity.getString(R.string.errorEmptySongPerformer)
    rule.onNodeWithText(emptyPerformerError)
      .assertIsDisplayed()
  }

  fun emptySongLyricsErrorIsDisplayed() {
    val emptyLyricsError = rule.activity.getString(R.string.errorEmptySongLyrics)
    rule.onNodeWithText(emptyLyricsError)
      .assertIsDisplayed()
  }

  fun verifySongBeingSaved() {
    val successMessage = rule.activity.getString(R.string.success)
    rule.onNodeWithText(successMessage)
      .assertIsDisplayed()
  }

  fun verifyErrorSavingSong() {
    val errorMessage = rule.activity.getString(R.string.errorSavingSong)
    rule.onNodeWithText(errorMessage)
      .assertIsDisplayed()
  }
}
