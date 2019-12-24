package nl.jovmit.lyrics.main.data.song

import nl.jovmit.lyrics.main.data.result.NewSongResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SongShould {

    private val songTitle = SongTitle("::irrelevant song title::")
    private val emptySongTitle = SongTitle("")
    private val songPerformer = SongPerformer("::irrelevant performer name::")
    private val emptySongPerformer = SongPerformer("")
    private val songLyrics = SongLyrics("::irrelevant song lyrics::")
    private val emptySongLyrics = SongLyrics("")

    @Test
    fun report_invalid_song_title() {
        val song = Song(emptySongTitle, songPerformer, songLyrics)

        assertEquals(NewSongResult.EmptyTitle, song.validate())
    }

    @Test
    fun report_invalid_song_performer() {
        val song = Song(songTitle, emptySongPerformer, songLyrics)

        assertEquals(NewSongResult.EmptyPerformer, song.validate())
    }

    @Test
    fun report_invalid_song_lyrics() {
        val song = Song(songTitle, songPerformer, emptySongLyrics)

        assertEquals(NewSongResult.EmptyLyrics, song.validate())
    }

    @Test
    fun report_valid_for_proper_song_arguments() {
        val song = Song(songTitle, songPerformer, songLyrics)

        assertEquals(NewSongResult.Valid, song.validate())
    }
}