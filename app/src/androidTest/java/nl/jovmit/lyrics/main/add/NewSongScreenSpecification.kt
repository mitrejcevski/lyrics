package nl.jovmit.lyrics.main.add

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewSongScreenSpecification {

    @Test
    fun should_show_empty_song_title_error() {
        launchNewSongScreen {} submit {
            emptySongTitleErrorIsDisplayed()
        }
    }
}