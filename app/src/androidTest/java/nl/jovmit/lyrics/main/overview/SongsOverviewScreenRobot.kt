package nl.jovmit.lyrics.main.overview

import androidx.fragment.app.testing.launchFragmentInContainer
import nl.jovmit.lyrics.*

@DslMarker
annotation class SongsOverviewScreenRobot

fun launchSongsOverview(block: SongsOverviewRobot.() -> Unit): SongsOverviewRobot {
    launchFragmentInContainer<SongsOverview>()
    return SongsOverviewRobot().apply(block)
}

@SongsOverviewScreenRobot
class SongsOverviewRobot {

    infix fun verify(function: SongsOverviewVerificationRobot.() -> Unit): SongsOverviewVerificationRobot {
        return SongsOverviewVerificationRobot().apply(function)
    }
}

@SongsOverviewScreenRobot
class SongsOverviewVerificationRobot {

    fun songsEmptyStateIsDisplayed() {
        R.id.songsOverviewEmptyStateLabel check isDisplayed
        R.id.songsOverviewEmptyStateLabel hasText R.string.empty_songs_list
    }

    fun songsEmptyStateIsGone() {
        R.id.songsOverviewEmptyStateLabel check isNotDisplayed
    }
}
