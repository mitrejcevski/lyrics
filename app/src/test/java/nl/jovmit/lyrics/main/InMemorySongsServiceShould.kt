package nl.jovmit.lyrics.main

import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.data.song.SongBuilder.Companion.aSong
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test

class InMemorySongsServiceShould {

    private val livingOnAPrayer = aSong().withTitle("Living on a prayer").build()
    private val bedOfRoses = aSong().withTitle("Bed of roses").build()
    private val service: SongsService = InMemorySongsService()

    @Test
    fun return_added_songs() = runBlocking {
        val songs = listOf(livingOnAPrayer, bedOfRoses)

        service.addNewSong(livingOnAPrayer)
        service.addNewSong(bedOfRoses)

        assertEquals(songs, service.fetchAllSongs())
    }
}