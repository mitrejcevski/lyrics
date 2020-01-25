package nl.jovmit.lyrics.main.overview

import android.content.Intent
import androidx.test.espresso.action.ViewActions.click
import androidx.test.rule.ActivityTestRule
import nl.jovmit.lyrics.*
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.data.song.Song

@DslMarker
annotation class SongsOverviewScreenRobot

fun launchSongsOverview(
    rule: ActivityTestRule<MainActivity>,
    block: SongsOverviewRobot.() -> Unit
): SongsOverviewRobot {
    rule.launchActivity(Intent())
    return SongsOverviewRobot().apply(block)
}

@SongsOverviewScreenRobot
class SongsOverviewRobot {

    infix fun verify(function: SongsOverviewVerificationRobot.() -> Unit): SongsOverviewVerificationRobot {
        return SongsOverviewVerificationRobot().apply(function)
    }

    fun clickOnNewSongButton() {
        R.id.songsOverviewNewSongButton perform click()
    }
}

@SongsOverviewScreenRobot
class SongsOverviewVerificationRobot {

    fun songsEmptyStateIsDisplayed() {
        R.id.songsOverviewEmptyStateLabel check isDisplayed
        R.id.songsOverviewEmptyStateLabel hasText R.string.emptySongsList
    }

    fun songsEmptyStateIsGone() {
        R.id.songsOverviewEmptyStateLabel check isNotDisplayed
    }

    fun songTitleAndSingerAreDisplayed(song: Song) {
        text(song.songTitle.value) check isDisplayed
        text(song.songPerformer.name) check isDisplayed
    }

    fun loadingErrorIsDisplayed() {
        text(R.string.errorFetchingSongs) check isDisplayed
    }

    fun newSongScreenIsDisplayed() {
        text(R.string.newSong) check isDisplayed
    }
}
