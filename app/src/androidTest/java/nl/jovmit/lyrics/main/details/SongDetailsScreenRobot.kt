package nl.jovmit.lyrics.main.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.data.song.Song

private typealias MainActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

@DslMarker
annotation class SongDetailsScreenRobot

fun launchSongsOverviewScreen(
  rule: MainActivityRule,
  block: SongDetailsRobot.() -> Unit
): SongDetailsRobot {
  return SongDetailsRobot(rule).apply(block)
}

@SongDetailsScreenRobot
class SongDetailsRobot(
  private val rule: MainActivityRule
) {

  fun tapOnSongWithTitle(songTitle: String) {
    rule.onNodeWithText(songTitle).performClick()
  }

  fun tapOnDeleteAction() {
    val deleteText = rule.activity.getString(R.string.delete)
    rule.onNodeWithTag("overflowAction").performClick()
    rule.onNodeWithText(deleteText).performClick()
  }

  infix fun verify(
    block: SongDetailsVerificationRobot.() -> Unit
  ): SongDetailsVerificationRobot {
    return SongDetailsVerificationRobot(rule).apply(block)
  }
}

@SongDetailsScreenRobot
class SongDetailsVerificationRobot(
  private val rule: MainActivityRule
) {

  fun songTitleIsDisplayed(song: Song) {
    rule.onNodeWithText(song.songTitle.value)
      .assertIsDisplayed()
  }

  fun songPerformerIsDisplayed(song: Song) {
    rule.onNodeWithText(song.songPerformer.name)
      .assertIsDisplayed()
  }

  fun songLyricsIsDisplayed(song: Song) {
    rule.onNodeWithText(song.songLyric.lyrics)
      .assertIsDisplayed()
  }

  fun deleteSongConfirmationIsShown() {
    val confirmText = rule.activity.getString(R.string.deleteSongPrompt)
    rule.onNodeWithText(confirmText)
      .assertIsDisplayed()
  }
}
