package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.main.data.song.SongData

interface SongsService {

    suspend fun fetchAllSongs(): List<SongData>

    suspend fun addNewSong(newSongData: SongData)
}
