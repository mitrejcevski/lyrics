package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.main.data.song.SongData

class FirebaseSongsService : SongsService {

    override suspend fun fetchAllSongs(): List<SongData> {
        TODO("not implemented")
    }

    override suspend fun addNewSong(newSongData: SongData) {
        TODO("not implemented")
    }
}