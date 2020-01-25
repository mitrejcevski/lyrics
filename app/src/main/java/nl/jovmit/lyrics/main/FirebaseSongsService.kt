package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongData

class FirebaseSongsService : SongsService {

    override suspend fun fetchAllSongs(): List<Song> {
        TODO("not implemented")
    }

    override suspend fun addNewSong(newSongData: SongData) {
        TODO("not implemented")
    }
}