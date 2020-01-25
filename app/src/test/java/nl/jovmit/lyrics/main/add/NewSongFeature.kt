package nl.jovmit.lyrics.main.add

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.verify
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.InMemorySongsService
import nl.jovmit.lyrics.main.data.result.NewSongResult
import nl.jovmit.lyrics.utils.IdGenerator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class, InstantTaskExecutorExtension::class)
class NewSongFeature {

    @Mock
    private lateinit var newSongObserver: Observer<NewSongResult>
    @Mock
    private lateinit var idGenerator: IdGenerator

    private val title = "song title"
    private val emptySongTitle = ""
    private val performer = "performer"
    private val lyrics = "song lyrics"
    private val startLoading = NewSongResult.Loading(true)
    private val songAdded = NewSongResult.SongAdded
    private val stopLoading = NewSongResult.Loading(false)

    private lateinit var newSongViewModel: NewSongViewModel

    @BeforeEach
    fun set_up() {
        val dispatchers = TestCoroutineDispatchers()
        val songsService = InMemorySongsService(idGenerator)
        val newSongRepository = NewSongRepository(songsService)
        newSongViewModel = NewSongViewModel(newSongRepository, dispatchers)
        newSongViewModel.newSongLiveData().observeForever(newSongObserver)
    }

    @Test
    fun should_perform_validation() {
        newSongViewModel.addNewSong(emptySongTitle, performer, lyrics)

        verify(newSongObserver).onChanged(NewSongResult.EmptyTitle)
    }

    @Test
    fun should_deliver_successfully_added_song() {
        given(idGenerator.next()).willReturn("::irrelevant song id::")

        newSongViewModel.addNewSong(title, performer, lyrics)

        val inOrder = inOrder(newSongObserver)
        inOrder.verify(newSongObserver).onChanged(startLoading)
        inOrder.verify(newSongObserver).onChanged(songAdded)
        inOrder.verify(newSongObserver).onChanged(stopLoading)
    }
}