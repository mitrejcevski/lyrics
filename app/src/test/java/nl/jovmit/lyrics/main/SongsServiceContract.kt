package nl.jovmit.lyrics.main

import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongBuilder.Companion.aSong
import nl.jovmit.lyrics.main.exceptions.SongsServiceException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

abstract class SongsServiceContract {

    @Test
    fun return_added_songs() = runBlocking {
        val firstSong = aSong().withId("songId1").build()
        val secondSong = aSong().withId("songId2").build()
        val songsList = listOf(firstSong, secondSong)

        val service = songsServiceWith(songsList)

        assertEquals(songsList, service.fetchAllSongs())
    }

    @Test
    fun return_song_with_matching_id() = runBlocking {
        val firstSong = aSong().withId("songId1").build()
        val secondSong = aSong().withId("songId2").build()
        val songsList = listOf(firstSong, secondSong)

        val service = songsServiceWith(songsList)

        assertEquals(firstSong, service.findSongById("songId1"))
    }

    @Test
    fun throw_service_exception_when_no_song_with_matching_id_exists() {
        val service = songsServiceWith(emptyList())

        assertThrows<SongsServiceException> {
            runBlocking { service.findSongById("songId") }
        }
    }

    @Test
    fun return_songs_containing_query_text() = runBlocking {
        val query = "abc"
        val songOne = aSong().withTitle("first abc").build()
        val songTwo = aSong().withPerformer("performer abc").build()
        val songThree = aSong().withLyrics("lyrics abc").build()
        val songFour = aSong().build()

        val service = songsServiceWith(listOf(songOne, songTwo, songThree, songFour))

        assertEquals(listOf(songOne, songTwo, songThree), service.search(query))
    }

    abstract fun songsServiceWith(songsList: List<Song>): SongsService
}