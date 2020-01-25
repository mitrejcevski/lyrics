package nl.jovmit.lyrics.main.add

import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.data.result.NewSongResult
import nl.jovmit.lyrics.main.data.song.SongDataBuilder
import nl.jovmit.lyrics.main.exceptions.SongsServiceException
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class NewSongRepositoryShould {

    @Mock
    private lateinit var songsService: SongsService

    private val newSong = SongDataBuilder.aSongData().build()

    private lateinit var repository: NewSongRepository

    @BeforeEach
    fun set_up() {
        repository = NewSongRepository(songsService)
    }

    @Test
    fun return_successfully_added_new_song() = runBlocking {
        val result = repository.addNewSong(newSong)

        assertEquals(NewSongResult.SongAdded, result)
    }

    @Test
    fun return_failure_adding_new_song() = runBlocking {
        given(songsService.addNewSong(newSong)).willThrow(SongsServiceException())

        val result = repository.addNewSong(newSong)

        assertEquals(NewSongResult.ErrorAddingSong, result)
    }
}