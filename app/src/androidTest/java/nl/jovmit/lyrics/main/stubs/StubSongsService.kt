package nl.jovmit.lyrics.main.stubs

import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongData

open class StubSongsService : SongsService {

    override suspend fun fetchAllSongs(): List<Song> {
        TODO("not implemented")
    }

    override suspend fun addNewSong(newSongData: SongData) {
        TODO("not implemented")
    }

    override suspend fun findSongById(songId: String): Song {
        TODO("not implemented")
    }

    override suspend fun search(query: String): List<Song> {
        TODO("not implemented")
    }

    override suspend fun deleteSongById(songId: String) {
        TODO("not implemented")
    }

    override suspend fun updateSong(songId: String, songData: SongData) {
        TODO("not implemented")
    }
}
