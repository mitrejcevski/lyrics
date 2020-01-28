package nl.jovmit.lyrics.main.overview

import com.nhaarman.mockitokotlin2.verifyBlocking
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class, InstantTaskExecutorExtension::class)
class SongsViewModelShould {

    @Mock
    private lateinit var songsRepository: SongsRepository

    private lateinit var songsViewModel: SongsViewModel

    @BeforeEach
    fun set_up() {
        val testDispatchers = TestCoroutineDispatchers()
        songsViewModel = SongsViewModel(songsRepository, testDispatchers)
    }

    @Test
    fun fetch_songs_from_the_songs_repository() {
        songsViewModel.fetchSongs()

        verifyBlocking(songsRepository) { fetchAllSongs() }
    }

    @Test
    fun search_songs_using_the_songs_repository() {
        val query = "song"

        songsViewModel.search(query)

        verifyBlocking(songsRepository) { searchSongs(query) }
    }
}