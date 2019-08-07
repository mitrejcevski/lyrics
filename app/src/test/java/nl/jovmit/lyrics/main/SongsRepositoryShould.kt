@file:Suppress("IncorrectScope")

package nl.jovmit.lyrics.main

import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.data.Song
import nl.jovmit.lyrics.main.data.result.SongsResult
import nl.jovmit.lyrics.main.exceptions.SongsServiceException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SongsRepositoryShould {

    @Mock
    private lateinit var songsService: SongsService

    private val song = Song("Title", "Singer Name", "The lyrics of the song")
    private val songs = listOf(song)
    private val songsFetched = SongsResult.Fetched(songs)
    private val fetchingError = SongsResult.FetchingError

    private lateinit var songsRepository: SongsRepository

    @Before
    fun setUp() {
        songsRepository = SongsRepository(songsService)
    }

    @Test
    fun returnSongsFetchedResultWhenSongsFetchedSuccessfullyFromService() = runBlocking {
        given(songsService.fetchAllSongs()).willReturn(songs)

        val result = songsRepository.fetchAllSongs()

        assertEquals(songsFetched, result)
    }

    @Test
    fun returnErrorResultWhenErrorFetchingSongsFromService() = runBlocking {
        given(songsService.fetchAllSongs()).willThrow(SongsServiceException())

        val result = songsRepository.fetchAllSongs()

        assertEquals(fetchingError, result)
    }
}