package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongData
import nl.jovmit.lyrics.main.data.song.SongId
import nl.jovmit.lyrics.utils.IdGenerator

class InMemorySongsService(
    private val idGenerator: IdGenerator
) : SongsService {

    private val songs = mutableListOf<Song>()

    override suspend fun fetchAllSongs(): List<Song> {
        return songs
    }

    override suspend fun addNewSong(newSongData: SongData) {
        with(newSongData) {
            val songId = SongId(idGenerator.next())
            val newSong = Song(songId, songTitle, songPerformer, songLyric)
            songs.add(newSong)
        }
    }
}