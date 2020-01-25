package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.main.data.song.SongData

class InMemorySongsService : SongsService {

    private val songs = mutableListOf<SongData>()

    override suspend fun fetchAllSongs(): List<SongData> {
        return songs
    }

    override suspend fun addNewSong(newSongData: SongData) {
        songs.add(newSongData)
    }
}