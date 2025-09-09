package nl.jovmit.lyrics.main.edit

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.rules.ActivityScenarioRule
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.data.song.Song

private typealias MainActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

@DslMarker
annotation class EditSongScreenRobot

fun launchMainScreenScreen(
  rule: MainActivityRule,
  block: EditSongRobot.() -> Unit
): EditSongRobot {
  return EditSongRobot(rule).apply(block)
}

@EditSongScreenRobot
class EditSongRobot(
  private val rule: MainActivityRule
) {

  fun tapOnSong(value: String) {
    rule.onNodeWithText(value)
      .performClick()
  }

  fun tapOnEditSongMenuItem() {
    rule.onNodeWithTag("editAction")
      .performClick()
  }

  fun replaceSongTitleWith(updatedSongTitle: String) {
    rule.onNodeWithTag("songTitle")
      .performTextReplacement(updatedSongTitle)
  }

  fun tapOnDoneMenuItem() {
    rule.onNodeWithTag("actionDone")
      .performClick()
  }

  infix fun verify(
    block: EditSongVerificationRobot.() -> Unit
  ): EditSongVerificationRobot {
    return EditSongVerificationRobot(rule).apply(block)
  }
}

@EditSongScreenRobot
class EditSongVerificationRobot(
  private val rule: MainActivityRule
) {

  fun editScreenIsGettingLaunchedFor(song: Song) {
    val editText = rule.activity.getString(R.string.edit)
    rule.onNodeWithText(editText).assertIsDisplayed()
    rule.onNodeWithText(song.songTitle.value).assertIsDisplayed()
    rule.onNodeWithText(song.songPerformer.name).assertIsDisplayed()
    rule.onNodeWithText(song.songLyric.lyrics).assertIsDisplayed()
  }

  @OptIn(ExperimentalTestApi::class)
  fun songDetailArePresentFor(updatedSong: Song) {
    val songDetailsText = rule.activity.getString(R.string.songDetails)
    rule.waitUntilExactlyOneExists(hasText(songDetailsText))
    rule.onNodeWithText(updatedSong.songTitle.value).assertIsDisplayed()
    rule.onNodeWithText(updatedSong.songPerformer.name).assertIsDisplayed()
    rule.onNodeWithText(updatedSong.songLyric.lyrics).assertIsDisplayed()
  }

  fun successUpdatingSongMessageShown() {
    val successLabel = rule.activity.getString(R.string.success)
    rule.onNodeWithText(successLabel).assertIsDisplayed()
  }

  fun unableSongEditingErrorShown() {
    val errorLabel = rule.activity.getString(R.string.errorUnableToEditSong)
    rule.onNodeWithText(errorLabel).assertIsDisplayed()
  }
}
