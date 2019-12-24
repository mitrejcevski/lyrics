package nl.jovmit.lyrics.main.data.song

import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SongTitleShould {

    @Test
    fun report_invalid_for_empty_title() {
        assertEquals(false, SongTitle("").isValid())
    }

    @Test
    fun report_invalid_for_blank_title() {
        Assertions.assertEquals(false, SongTitle(" ").isValid())
    }

    @Test
    fun report_valid_for_proper_title() {
        Assertions.assertEquals(true, SongTitle("song title").isValid())
    }
}