package nl.jovmit.lyrics.main.overview

import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.data.result.SongsResult
import nl.jovmit.lyrics.main.data.song.SongData
import nl.jovmit.lyrics.main.data.song.SongLyrics
import nl.jovmit.lyrics.main.data.song.SongPerformer
import nl.jovmit.lyrics.main.data.song.SongTitle
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

    private val song = SongData(
        SongTitle("Title"),
        SongPerformer("Singer Name"),
        SongLyrics("The lyrics of the song")
    )
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
}