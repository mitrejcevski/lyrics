package nl.jovmit.lyrics.main.data.song

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SongLyricsShould {

    @Test
    fun report_invalid_for_empty_song_lyrics() {
        assertEquals(false, SongLyrics("").isValid())
    }

    @Test
    fun report_invalid_for_blank_song_lyrics() {
        assertEquals(false, SongLyrics("  ").isValid())
    }

    @Test
    fun report_valid_for_proper_song_lyrics() {
        assertEquals(true, SongLyrics("This love has taken its toll on me").isValid())
    }
}