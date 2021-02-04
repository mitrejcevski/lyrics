package nl.jovmit.lyrics.main.details

import android.content.Context
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.*
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.main.data.song.Song
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@DslMarker
annotation class SongDetailsScreenRobot

fun launchSongsOverviewScreen(
    block: SongDetailsRobot.() -> Unit
): SongDetailsRobot {
    val scenario = launch(MainActivity::class.java)
    val activity = runBlocking {
        suspendCoroutine<MainActivity> { continuation ->
            scenario.onActivity {
                continuation.resume(it)
            }
        }
    }
    return SongDetailsRobot(activity).apply(block)
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