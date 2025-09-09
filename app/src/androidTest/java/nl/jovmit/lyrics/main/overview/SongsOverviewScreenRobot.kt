package nl.jovmit.lyrics.main.overview

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.requestFocus
import androidx.test.ext.junit.rules.ActivityScenarioRule
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.data.song.Song

private typealias MainActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

@DslMarker
annotation class SongsOverviewScreenRobot

fun launchSongsOverview(
  rule: MainActivityRule,
  block: SongsOverviewRobot.() -> Unit
): SongsOverviewRobot {
  return SongsOverviewRobot(rule).apply(block)
}

@SongsOverviewScreenRobot
class SongsOverviewRobot(
  private val rule: MainActivityRule
) {

  fun clickOnNewSongButton() {
    rule.onNodeWithTag("newSong").performClick()
  }

  fun tapOnSongWithTitle(songTitle: String) {
    rule.onNodeWithText(songTitle).performClick()
  }

  @OptIn(ExperimentalTestApi::class)
  fun typeSearchQuery(query: String) {
    val searchText = rule.activity.getString(R.string.search)
    rule.waitUntilExactlyOneExists(hasText(searchText))
    rule.onNodeWithTag("searchText")
      .requestFocus()
      .performTextInput(query)
    rule.onNodeWithTag("searchText").performImeAction()
  }

  fun closeSearch() {
    rule.onNodeWithTag("navigateUp").performClick()
  }

  fun tapOnLogout() {
    val logoutLabel = rule.activity.getString(R.string.logout)
    rule.onNodeWithTag("actionOverflow").performClick()
    rule.onNodeWithText(logoutLabel).performClick()
  }

  infix fun verify(
    function: SongsOverviewVerificationRobot.() -> Unit
  ): SongsOverviewVerificationRobot {
    return SongsOverviewVerificationRobot(rule).apply(function)
  }
}

@SongsOverviewScreenRobot
class SongsOverviewVerificationRobot(
  private val rule: MainActivityRule
) {

  fun songsEmptyStateIsDisplayed() {
    val emptyList = rule.activity.getString(R.string.emptySongsList)
    rule.onNodeWithText(emptyList).assertIsDisplayed()
  }

  fun songsEmptyStateIsGone() {
    val emptyList = rule.activity.getString(R.string.emptySongsList)
    rule.onNodeWithText(emptyList).assertIsNotDisplayed()
  }

  fun songTitleAndSingerAreDisplayed(song: Song) {
    rule.onAllNodesWithText(song.songTitle.value).assertAll(hasText(song.songTitle.value))
    rule.onAllNodesWithText(song.songPerformer.name).assertAll(hasText(song.songTitle.value))
  }

  fun songTitleAndSingerNotDisplayed(song: Song) {
    rule.onNodeWithText(song.songTitle.value).assertDoesNotExist()
    rule.onNodeWithText(song.songPerformer.name).assertDoesNotExist()
  }

  @OptIn(ExperimentalTestApi::class)
  fun loadingErrorIsDisplayed() {
    val cantLoadMessage = rule.activity.getString(R.string.errorFetchingSongs)
    rule.waitUntilExactlyOneExists(hasText(cantLoadMessage))
    rule.onNodeWithText(cantLoadMessage)
      .assertIsDisplayed()
  }

  fun newSongScreenIsDisplayed() {
    val newSong = rule.activity.getString(R.string.newSong)
    rule.onNodeWithText(newSong)
      .assertIsDisplayed()
  }

  fun songDetailsScreenIsOpened() {
    val errorMessage = rule.activity.getString(R.string.songDetails)
    rule.onNodeWithText(errorMessage)
      .assertIsDisplayed()
  }

  fun searchErrorIsDisplayed() {
    val errorMessage = rule.activity.getString(R.string.errorSearchingSong)
    rule.onNodeWithText(errorMessage)
      .assertIsDisplayed()
  }

  fun registrationScreenIsDisplayed() {
    val registrationLabel = rule.activity.getString(R.string.createUser)
    rule.onAllNodesWithText(registrationLabel).assertAll(hasText(registrationLabel))
  }
}
