package nl.jovmit.lyrics.main.edit

import com.nhaarman.mockitokotlin2.verifyBlocking
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.common.TestCoroutineDispatchers
import nl.jovmit.lyrics.main.data.song.SongDataBuilder.Companion.aSongData
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
class UpdateSongViewModelShould {

    @Mock
    private lateinit var songsRepository: SongsRepository

    private val songId = "::songId::"
    private val title = "::Song Title"
    private val performer = "::Performer::"
    private val lyrics = "::Lyrics::"
    private val songData = aSongData()
        .withTitle(title)
        .withPerformer(performer)
        .withLyrics(lyrics)
        .build()

    private lateinit var viewModel: UpdateSongViewModel

    @BeforeEach
    fun setUp() {
        val dispatchers = TestCoroutineDispatchers()
        viewModel = UpdateSongViewModel(songsRepository, dispatchers)
    }

    @Test
    fun update_song_using_songs_repository() {
        viewModel.updateSong(songId, SongTitle(title), SongPerformer(performer), SongLyrics(lyrics))

        verifyBlocking(songsRepository) { updateSong(songId, songData) }
    }
}