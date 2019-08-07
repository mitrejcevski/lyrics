package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.main.data.Song

interface SongsService {

    suspend fun fetchAllSongs(): List<Song>
}
