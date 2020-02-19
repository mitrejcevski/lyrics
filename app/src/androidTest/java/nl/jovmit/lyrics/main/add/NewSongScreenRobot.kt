package nl.jovmit.lyrics.main.add

import android.content.Intent
import androidx.test.espresso.action.ViewActions.*
import androidx.test.rule.ActivityTestRule
import nl.jovmit.lyrics.*
import nl.jovmit.lyrics.main.MainActivity

@DslMarker
annotation class NewSongScreenRobot

fun launchNewSongScreen(
    rule: ActivityTestRule<MainActivity>,
    block: NewSongRobot.() -> Unit
): NewSongRobot {
    rule.launchActivity(Intent())
    R.id.songsOverviewNewSongButton perform click()
    return NewSongRobot().apply(block)
}

@NewSongScreenRobot
class NewSongRobot {

    fun typeSongTitle(title: String) {
        R.id.newSongTitleEditText.perform(typeText(title), closeSoftKeyboard())
    }

    fun typeSongPerformer(performer: String) {
        R.id.newSongPerformerEditText.perform(typeText(performer), closeSoftKeyboard())
    }

    fun typeSongLyrics(lyrics: String) {
        R.id.newSongLyricEditText.perform(typeText(lyrics), closeSoftKeyboard())
    }

    infix fun submit(
        block: NewSongVerificationRobot.() -> Unit
    ): NewSongVerificationRobot {
        R.id.actionDone perform click()
        return NewSongVerificationRobot().apply(block)
    }
}

@NewSongScreenRobot
class NewSongVerificationRobot {

    fun emptySongTitleErrorIsDisplayed() {
        text(R.string.errorEmptySongTitle) check isDisplayed
    }

    fun emptySongPerformerErrorIsDisplayed() {
        text(R.string.errorEmptySongPerformer) check isDisplayed
    }

    fun emptySongLyricsErrorIsDisplayed() {
        text(R.string.errorEmptySongLyrics) check isDisplayed
    }

    fun verifySongBeingSaved() {
        text(R.string.success) check isDisplayed
    }

    fun verifyErrorSavingSong() {
        text(R.string.errorSavingSong) check isDisplayed
    }
}