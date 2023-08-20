package nl.jovmit.lyrics.main.add

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.data.song.SongData
import nl.jovmit.lyrics.main.data.song.SongLyrics
import nl.jovmit.lyrics.main.data.song.SongPerformer
import nl.jovmit.lyrics.main.data.song.SongTitle
import nl.jovmit.lyrics.main.overview.SongsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class, InstantTaskExecutorExtension::class)
class NewSongViewModelShould {

    @Mock
    private lateinit var songsRepository: SongsRepository

    private val title = "title"
    private val performer = "performer"
    private val lyrics = "song lyrics"
    private val newSong = SongData(
        SongTitle(title),
        SongPerformer(performer),
        SongLyrics(lyrics)
    )

    private lateinit var viewModel: NewSongViewModel

    @BeforeEach
    fun set_up() {
        val dispatchers = TestCoroutineDispatchers()
        viewModel = NewSongViewModel(songsRepository, dispatchers)
    }

    @Test
    fun add_new_song_to_song_repository() = runBlocking<Unit> {
        viewModel.addNewSong(title, performer, lyrics)

        verify(songsRepository).addNewSong(newSong)
    }

    @Test
    fun not_add_song_to_repository_when_song_is_not_valid() {
        viewModel.addNewSong("", performer, lyrics)
        viewModel.addNewSong(title, "", lyrics)
        viewModel.addNewSong(title, performer, "")

        verifyNoMoreInteractions(songsRepository)
    }
}
