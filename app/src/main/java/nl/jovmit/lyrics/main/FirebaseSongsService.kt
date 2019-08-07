package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.main.data.Song

class FirebaseSongsService : SongsService {

    override suspend fun fetchAllSongs(): List<Song> {
        return emptyList()
    }
}