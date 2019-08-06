package nl.jovmit.lyrics.main.overview

import nl.jovmit.lyrics.main.data.Song

interface SongsService {

    suspend fun fetchAllSongs(): List<Song>
}
