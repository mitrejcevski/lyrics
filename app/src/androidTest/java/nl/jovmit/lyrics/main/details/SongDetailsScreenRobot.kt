package nl.jovmit.lyrics.main.details

import android.content.Intent
import androidx.test.espresso.action.ViewActions.click
import androidx.test.rule.ActivityTestRule
import nl.jovmit.lyrics.*
import nl.jovmit.lyrics.main.MainActivity

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

    fun songDetailsScreenIsOpened() {
        text(R.string.songDetails) check isDisplayed
    }
}