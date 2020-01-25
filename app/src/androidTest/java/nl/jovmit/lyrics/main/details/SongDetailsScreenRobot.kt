package nl.jovmit.lyrics.main.details

import android.content.Intent
import androidx.test.espresso.action.ViewActions.click
import androidx.test.rule.ActivityTestRule
import nl.jovmit.lyrics.check
import nl.jovmit.lyrics.isDisplayed
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.perform
import nl.jovmit.lyrics.text

@DslMarker
annotation class SongDetailsScreenRobot

fun launchSongsOverviewScreen(
    rule: ActivityTestRule<MainActivity>,
    block: SongDetailsRobot.() -> Unit
): SongDetailsRobot {
    rule.launchActivity(Intent())
    return SongDetailsRobot().apply(block)
}

@SongDetailsScreenRobot
class SongDetailsRobot {

    fun tapOnSongWithTitle(songTitle: String) {
        text(songTitle) perform click()
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
}