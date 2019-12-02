package nl.jovmit.lyrics.main.add

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.action.ViewActions.click
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
}