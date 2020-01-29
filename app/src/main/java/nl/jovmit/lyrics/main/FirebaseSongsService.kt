package nl.jovmit.lyrics.main

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import nl.jovmit.lyrics.main.data.song.*
import nl.jovmit.lyrics.main.exceptions.SongsServiceException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseSongsService(
    private val database: FirebaseFirestore
) : SongsService {

    private companion object {
        private const val SONGS_COLLECTION = "songs"
        private const val SONG_TITLE = "songTitle"
        private const val SONG_PERFORMER = "songPerformer"
        private const val SONG_LYRIC = "songLyric"
    }

    private var songsCache = mutableListOf<Song>()

    override suspend fun fetchAllSongs(): List<Song> = suspendCoroutine { continuation ->
        database.collection(SONGS_COLLECTION).get()
            .addOnSuccessListener { result ->
                val songs = result.documents.map { createSongFor(it) }
                updateCache(songs)
                continuation.resume(songs)
            }
            .addOnFailureListener {
                continuation.resumeWithException(SongsServiceException())
            }
    }

    private fun updateCache(songs: List<Song>) {
        songsCache.clear()
        songsCache.addAll(songs)
    }

    override suspend fun addNewSong(
        newSongData: SongData
    ) = suspendCoroutine<Unit> { continuation ->
        val firebaseSongData = firebaseSongDataFrom(newSongData)
        database.collection(SONGS_COLLECTION).add(firebaseSongData)
            .addOnSuccessListener { continuation.resume(Unit) }
            .addOnFailureListener { continuation.resumeWithException(SongsServiceException()) }
    }

    override suspend fun findSongById(songId: String): Song = suspendCoroutine { continuation ->
        database.collection(SONGS_COLLECTION).document(songId).get()
            .addOnSuccessListener { result ->
                val song = createSongFor(result)
                continuation.resume(song)
            }
            .addOnFailureListener {
                continuation.resumeWithException(SongsServiceException())
            }
    }

    override suspend fun search(query: String): List<Song> {
        return songsCache.filter {
            it.songTitle.value.contains(query, ignoreCase = true) ||
                    it.songPerformer.name.contains(query, ignoreCase = true) ||
                    it.songLyric.lyrics.contains(query, ignoreCase = true)
        }
    }

    private fun firebaseSongDataFrom(newSongData: SongData): HashMap<String, String> {
        return hashMapOf(
            SONG_TITLE to newSongData.songTitle.value,
            SONG_PERFORMER to newSongData.songPerformer.name,
            SONG_LYRIC to newSongData.songLyric.lyrics
        )
    }

    private fun createSongFor(document: DocumentSnapshot): Song {
        return Song(
            SongId(document.id),
            SongTitle(document.getString(SONG_TITLE) ?: ""),
            SongPerformer(document.getString(SONG_PERFORMER) ?: ""),
            SongLyrics(document.getString(SONG_LYRIC) ?: "")
        )
    }
}