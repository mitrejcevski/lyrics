package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.main.data.song.Song

class FirebaseSongsService : SongsService {

    override suspend fun fetchAllSongs(): List<Song> {
        TODO("not implemented")
    }

    override suspend fun addNewSong(newSong: Song) {
        TODO("not implemented")
    }
}