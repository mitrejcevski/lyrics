package nl.jovmit.lyrics.main.data.result

import nl.jovmit.lyrics.main.data.song.SongData

sealed class SongsResult {

    data class Loading(val loading: Boolean) : SongsResult()

    data class Fetched(val songs: List<SongData>) : SongsResult()

    object FetchingError : SongsResult()
}
