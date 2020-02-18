package nl.jovmit.lyrics.main.data.result

import nl.jovmit.lyrics.main.data.song.Song

sealed class SongResult {

    data class Loading(val loading: Boolean) : SongResult()

    data class Fetched(val song: Song) : SongResult()

    object NotFound : SongResult()

    object Deleted : SongResult()

    object ErrorDeleting : SongResult()

    object Updated : SongResult()

    object ErrorUpdating : SongResult()
}
