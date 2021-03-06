package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongData

interface SongsService {

    suspend fun fetchAllSongs(): List<Song>

    suspend fun addNewSong(newSongData: SongData)

    suspend fun findSongById(songId: String): Song

    suspend fun search(query: String): List<Song>

    suspend fun deleteSongById(songId: String)

    suspend fun updateSong(songId: String, songData: SongData)
}
