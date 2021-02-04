package nl.jovmit.lyrics.main.details

import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu
import androidx.test.espresso.action.ViewActions.click
import nl.jovmit.lyrics.*
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.data.song.Song

@DslMarker
annotation class SongDetailsScreenRobot

fun launchSongsOverviewScreen(
    block: SongDetailsRobot.() -> Unit
): SongDetailsRobot {
    launch(MainActivity::class.java)
    return SongDetailsRobot().apply(block)
}

@SongDetailsScreenRobot
class SongDetailsRobot {

    fun tapOnSongWithTitle(songTitle: String) {
        text(songTitle) perform click()
    }

    fun tapOnDeleteAction() {
        openContextualActionModeOverflowMenu()
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
