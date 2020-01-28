package nl.jovmit.lyrics.main.data.result

import nl.jovmit.lyrics.main.data.song.Song

sealed class SongsResult {

    data class Loading(val loading: Boolean) : SongsResult()

    data class Fetched(val songs: List<Song>) : SongsResult()

    object FetchingError : SongsResult()

    object SearchError : SongsResult()
}
