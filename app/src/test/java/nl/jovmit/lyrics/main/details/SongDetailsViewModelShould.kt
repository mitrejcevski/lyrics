package nl.jovmit.lyrics.main.details

import com.nhaarman.mockitokotlin2.verifyBlocking
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.overview.SongsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class, InstantTaskExecutorExtension::class)
class SongDetailsViewModelShould {

    @Mock
    private lateinit var songsRepository: SongsRepository

    private lateinit var songDetailsViewModel: SongDetailsViewModel

    @BeforeEach
    fun set_up() {
        val dispatchers: CoroutineDispatchers = TestCoroutineDispatchers()
        songDetailsViewModel = SongDetailsViewModel(songsRepository, dispatchers)
    }

    @Test
    fun fetch_song_by_id_from_songs_repository() {
        val songId = "::songId::"

        songDetailsViewModel.fetchSongById(songId)

        verifyBlocking(songsRepository) { findSongById(songId) }
    }

    @Test
    fun delete_song_by_id_in_songs_repository() {
        val songId = "::songId::"

        songDetailsViewModel.deleteSongById(songId)

        verifyBlocking(songsRepository) { deleteSongById(songId) }
    }
}