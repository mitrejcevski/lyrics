package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.utils.IdGenerator
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class InMemorySongsServiceShould : SongsServiceContract() {

    @Mock
    private lateinit var idGenerator: IdGenerator

    override fun songsServiceWith(songsList: List<Song>): SongsService {
        return InMemorySongsService(idGenerator, songsList)
    }
}