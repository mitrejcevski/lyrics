package nl.jovmit.lyrics.main

import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.data.song.SongBuilder.Companion.aSong
import nl.jovmit.lyrics.main.data.song.SongDataBuilder.Companion.aSongData
import nl.jovmit.lyrics.utils.IdGenerator
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class InMemorySongsServiceShould {

    @Mock
    private lateinit var idGenerator: IdGenerator

    private val firstSongId = "::first song id::"
    private val firstSongTitle = "::first song title::"
    private val secondSongId = "::second song id::"
    private val secondSongTitle = "::second song data::"
    private val firstSongData = aSongData()
        .withTitle(firstSongTitle)
        .build()
    private val firstSong = aSong()
        .withId(firstSongId)
        .withTitle(firstSongTitle)
        .build()
    private val secondSongData = aSongData()
        .withTitle("::second song data::")
        .build()
    private val secondSong = aSong()
        .withId(secondSongId)
        .withTitle(secondSongTitle)
        .build()

    private lateinit var service: SongsService

    @BeforeEach
    fun set_up() {
        service = InMemorySongsService(idGenerator)
    }

    @Test
    fun return_added_songs() = runBlocking {
        given(idGenerator.next()).willReturn(firstSongId, secondSongId)

        service.addNewSong(firstSongData)
        service.addNewSong(secondSongData)

        assertEquals(listOf(firstSong, secondSong), service.fetchAllSongs())
    }
}