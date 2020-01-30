package nl.jovmit.lyrics.main.details

import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.rule.ActivityTestRule
import nl.jovmit.lyrics.*
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.data.song.Song

@DslMarker
annotation class SongDetailsScreenRobot

fun launchSongsOverviewScreen(
    rule: ActivityTestRule<MainActivity>,
    block: SongDetailsRobot.() -> Unit
): SongDetailsRobot {
    rule.launchActivity(Intent())
    return SongDetailsRobot(rule.activity).apply(block)
}

@SongDetailsScreenRobot
class SongDetailsRobot(private val context: Context) {

    fun tapOnSongWithTitle(songTitle: String) {
        text(songTitle) perform click()
    }

    fun tapOnDeleteAction() {
        openActionBarOverflowOrOptionsMenu(context)
        text(R.string.delete) perform click()
    }

    infix fun verify(
        block: SongDetailsVerificationRobot.() -> Unit
    ): SongDetailsVerificationRobot {
        return SongDetailsVerificationRobot().apply(block)
    }
}

@SongDetailsScreenRobot
class SongDetailsVerificationRobot {

    fun songTitleIsDisplayed(song: Song) {
        text(song.songTitle.value) check isDisplayed
    }

    fun songPerformerIsDisplayed(song: Song) {
        text(song.songPerformer.name) check isDisplayed
    }

    fun songLyricsIsDisplayed(song: Song) {
        text(song.songLyric.lyrics) check isDisplayed
    }

    fun deleteSongConfirmationIsShown() {
        text(R.string.deleteSongPrompt) check isDisplayed
    }
}