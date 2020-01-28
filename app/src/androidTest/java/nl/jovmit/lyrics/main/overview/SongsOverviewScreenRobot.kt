package nl.jovmit.lyrics.main.overview

import android.content.Intent
import androidx.appcompat.widget.SearchView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBackUnconditionally
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
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

    fun clickOnNewSongButton() {
        R.id.songsOverviewNewSongButton perform click()
    }

    fun tapOnSongWithTitle(songTitle: String) {
        text(songTitle) perform click()
    }

    fun typeSearchQuery(query: String) {
        R.id.actionSearch perform click()
        onView(isAssignableFrom(SearchView::class.java)).perform(submitText(query))
    }

    fun closeSearch() {
        pressBackUnconditionally()
        pressBackUnconditionally()
    }

    infix fun verify(
        function: SongsOverviewVerificationRobot.() -> Unit
    ): SongsOverviewVerificationRobot {
        return SongsOverviewVerificationRobot().apply(function)
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

    fun songTitleAndSingerNotDisplayed(song: Song) {
        text(song.songTitle.value) check doesNotExist
        text(song.songPerformer.name) check doesNotExist
    }

    fun loadingErrorIsDisplayed() {
        text(R.string.errorFetchingSongs) check isDisplayed
    }

    fun newSongScreenIsDisplayed() {
        text(R.string.newSong) check isDisplayed
    }

    fun songDetailsScreenIsOpened() {
        text(R.string.songDetails) check isDisplayed
    }
}
