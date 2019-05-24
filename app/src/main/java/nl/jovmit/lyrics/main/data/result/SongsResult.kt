package nl.jovmit.lyrics.main.data.result

import nl.jovmit.lyrics.main.data.Song

sealed class SongsResult {

    data class Fetched(val songs: List<Song>) : SongsResult()
}
