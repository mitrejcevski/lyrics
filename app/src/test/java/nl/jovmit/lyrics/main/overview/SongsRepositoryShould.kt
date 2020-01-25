package nl.jovmit.lyrics.main.overview

import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.data.result.SongResult
import nl.jovmit.lyrics.main.data.result.SongsResult
import nl.jovmit.lyrics.main.data.song.SongBuilder.Companion.aSong
import nl.jovmit.lyrics.main.exceptions.SongsServiceException
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class SongsRepositoryShould {

    @Mock
    private lateinit var songsService: SongsService

    private val song = aSong()
        .withTitle("Title")
        .withPerformer("Singer Name")
        .withLyrics("The lyrics of the song")
        .build()
    private val songs = listOf(song)
    private val songsFetched = SongsResult.Fetched(songs)
    private val fetchingError = SongsResult.FetchingError

    private lateinit var songsRepository: SongsRepository

    @BeforeEach
    fun set_up() {
        songsRepository = SongsRepository(songsService)
    }

    @Test
    fun return_songs_fetched_result_when_songs_fetched_successfully_from_service() = runBlocking {
        given(songsService.fetchAllSongs()).willReturn(songs)

        val result = songsRepository.fetchAllSongs()

        assertEquals(songsFetched, result)
    }

    @Test
    fun return_error_result_when_error_fetching_songs_from_service() = runBlocking {
        given(songsService.fetchAllSongs()).willThrow(SongsServiceException())

        val result = songsRepository.fetchAllSongs()

        assertEquals(fetchingError, result)
    }

    @Test
    fun return_song_result_when_lookup_is_successful() = runBlocking {
        val songId = "songId"
        given(songsService.findSongById(songId)).willReturn(song)

        val result = songsRepository.findSongById(songId)

        assertEquals(SongResult.Fetched(song), result)
    }

    @Test
    fun return_error_result_when_lookup_fails() = runBlocking {
        val songId = "songId"
        given(songsService.findSongById(songId)).willThrow(SongsServiceException())

        val result = songsRepository.findSongById(songId)

        assertEquals(SongResult.NotFound, result)
    }
}