package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.main.data.song.Song

class InMemorySongsService : SongsService {

    private val songs = mutableListOf<Song>()

    override suspend fun fetchAllSongs(): List<Song> {
        return songs
    }

    override suspend fun addNewSong(newSong: Song) {
        songs.add(newSong)
    }
}