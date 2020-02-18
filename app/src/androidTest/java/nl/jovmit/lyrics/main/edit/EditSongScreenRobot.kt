package nl.jovmit.lyrics.main.edit

import android.content.Intent
import androidx.test.espresso.action.ViewActions.click
import androidx.test.rule.ActivityTestRule
import nl.jovmit.lyrics.*
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.data.song.Song

@DslMarker
annotation class EditSongScreenRobot

fun launchMainScreenScreen(
    rule: ActivityTestRule<MainActivity>,
    block: EditSongRobot.() -> Unit
): EditSongRobot {
    rule.launchActivity(Intent())
    return EditSongRobot().apply(block)
}

@EditSongScreenRobot
class EditSongRobot {

    fun tapOnSong(value: String) {
        text(value) perform click()
    }

    fun tapOnEditSongMenuItem() {
        R.id.actionEdit perform click()
    }

    infix fun verify(
        block: EditSongVerificationRobot.() -> Unit
    ): EditSongVerificationRobot {
        return EditSongVerificationRobot().apply(block)
    }
}

@EditSongScreenRobot
class EditSongVerificationRobot {

    fun editScreenIsGettingLaunchedFor(song: Song) {
        text(R.string.edit) check isDisplayed
        text(song.songTitle.value) check isDisplayed
        text(song.songPerformer.name) check isDisplayed
        text(song.songLyric.lyrics) check isDisplayed
    }
}
