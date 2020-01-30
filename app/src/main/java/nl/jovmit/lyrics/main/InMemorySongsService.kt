package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongData
import nl.jovmit.lyrics.main.data.song.SongId
import nl.jovmit.lyrics.main.exceptions.SongsServiceException
import nl.jovmit.lyrics.utils.IdGenerator

class InMemorySongsService(
    private val idGenerator: IdGenerator,
    songsList: List<Song> = emptyList()
) : SongsService {

    private val songs = mutableListOf<Song>()

    init {
        this.songs.addAll(songsList)
    }

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

    override suspend fun findSongById(songId: String): Song {
        return songs.find { it.songId.value == songId }
            ?: throw SongsServiceException()
    }

    override suspend fun search(query: String): List<Song> {
        return songs.filter {
            it.songTitle.value.contains(query, ignoreCase = true) ||
                    it.songPerformer.name.contains(query, ignoreCase = true) ||
                    it.songLyric.lyrics.contains(query, ignoreCase = true)
        }
    }

    override suspend fun deleteSongById(songId: String) {
        songs.removeAll { it.songId.value == songId }
    }
}