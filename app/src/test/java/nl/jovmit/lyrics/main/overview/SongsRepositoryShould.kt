package nl.jovmit.lyrics.main.overview

import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.data.result.NewSongResult
import nl.jovmit.lyrics.main.data.result.SongResult
import nl.jovmit.lyrics.main.data.result.SongsResult
import nl.jovmit.lyrics.main.data.song.SongBuilder.Companion.aSong
import nl.jovmit.lyrics.main.data.song.SongDataBuilder.Companion.aSongData
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
    private val newSongData = aSongData().build()

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

    @Test
    fun return_songs_result_when_songs_search_is_successful() = runBlocking {
        val query = "song"
        given(songsService.search(query)).willReturn(songs)

        val result = songsRepository.searchSongs(query)

        assertEquals(songsFetched, result)
    }

    @Test
    fun return_error_result_when_songs_search_fails() = runBlocking {
        val query = "query"
        given(songsService.search(query)).willThrow(SongsServiceException())

        val result = songsRepository.searchSongs(query)

        assertEquals(SongsResult.SearchError, result)
    }

    @Test
    fun return_successfully_added_new_song() = runBlocking {
        val result = songsRepository.addNewSong(newSongData)

        assertEquals(NewSongResult.SongAdded, result)
    }

    @Test
    fun return_failure_adding_new_song() = runBlocking {
        given(songsService.addNewSong(newSongData)).willThrow(SongsServiceException())

        val result = songsRepository.addNewSong(newSongData)

        assertEquals(NewSongResult.ErrorAddingSong, result)
    }

    @Test
    fun return_success_when_song_updated_successfully() = runBlocking {
        val songId = "::songId::"
        val songData = aSongData().build()

        val result = songsRepository.updateSong(songId, songData)

        assertEquals(SongResult.Updated, result)
    }

    @Test
    fun return_failure_when_song_update_fails() = runBlocking {
        val songId = "::songId::"
        val songData = aSongData().build()
        given(songsService.updateSong(songId, songData)).willThrow(SongsServiceException())

        val result = songsRepository.updateSong(songId, songData)

        assertEquals(SongResult.ErrorUpdating, result)
    }

    @Test
    fun return_success_deleting_song() = runBlocking {
        val songId = "::song id::"

        val result = songsRepository.deleteSongById(songId)

        assertEquals(SongResult.Deleted, result)
    }

    @Test
    fun return_failure_when_song_deletion_fails() = runBlocking {
        val songId = "::song id::"
        given(songsService.deleteSongById(songId)).willThrow(SongsServiceException())

        val result = songsRepository.deleteSongById(songId)

        assertEquals(SongResult.ErrorDeleting, result)
    }
}
