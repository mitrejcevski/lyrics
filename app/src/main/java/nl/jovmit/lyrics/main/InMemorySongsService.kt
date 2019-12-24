package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.main.data.song.Song

class InMemorySongsService : SongsService {

    override suspend fun fetchAllSongs(): List<Song> {
        return emptyList()
    }
}