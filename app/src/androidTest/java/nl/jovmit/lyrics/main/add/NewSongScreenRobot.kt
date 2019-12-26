package nl.jovmit.lyrics.main.add

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.action.ViewActions.*
import nl.jovmit.lyrics.*

@DslMarker
annotation class NewSongScreenRobot

fun launchNewSongScreen(
    block: NewSongRobot.() -> Unit
): NewSongRobot {
    launchFragmentInContainer<NewSong>(themeResId = R.style.AppTheme)
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
        R.id.newSongDoneButton perform click()
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
}