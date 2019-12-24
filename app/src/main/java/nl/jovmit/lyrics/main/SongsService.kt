package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.main.data.song.Song

interface SongsService {

    suspend fun fetchAllSongs(): List<Song>
}
