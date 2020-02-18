package nl.jovmit.lyrics.main.edit

import android.content.Intent
import androidx.test.espresso.action.ViewActions.*
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

    fun replaceSongTitleWith(updatedSongTitle: String) {
        R.id.editSongTitleEditText perform clearText()
        R.id.editSongTitleEditText perform typeText(updatedSongTitle)
    }

    fun tapOnDoneMenuItem() {
        R.id.actionDone perform click()
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

    fun songDetailArePresentFor(updatedSong: Song) {
        text(R.string.songDetails) check isDisplayed
        text(updatedSong.songTitle.value) check isDisplayed
        text(updatedSong.songPerformer.name) check isDisplayed
        text(updatedSong.songLyric.lyrics) check isDisplayed
    }

    fun successUpdatingSongMessageShown() {
        text(R.string.success) check isDisplayed
    }

    fun unableSongEditingErrorShown() {
        text(R.string.errorUnableToEditSong) check isDisplayed
    }
}
