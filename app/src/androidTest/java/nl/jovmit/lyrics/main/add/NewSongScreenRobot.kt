package nl.jovmit.lyrics.main.add

import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.action.ViewActions.*
import nl.jovmit.lyrics.*
import nl.jovmit.lyrics.main.MainActivity

@DslMarker
annotation class NewSongScreenRobot

fun launchNewSongScreen(
    block: NewSongRobot.() -> Unit
): NewSongRobot {
    launch(MainActivity::class.java)
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