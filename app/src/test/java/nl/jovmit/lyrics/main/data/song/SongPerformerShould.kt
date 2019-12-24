package nl.jovmit.lyrics.main.data.song

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SongPerformerShould {

    @Test
    fun report_invalid_for_empty_performer_name() {
        assertEquals(false, SongPerformer("").isValid())
    }

    @Test
    fun report_invalid_for_blank_performer_name() {
        assertEquals(false, SongPerformer(" ").isValid())
    }

    @Test
    fun report_valid_for_proper_performer_name() {
        assertEquals(true, SongPerformer("Tom Driver").isValid())
    }
}