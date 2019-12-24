package nl.jovmit.lyrics.main.data.result

sealed class NewSongResult {

    data class Loading(val value: Boolean) : NewSongResult()

    object SongAdded : NewSongResult()

    object EmptyTitle : NewSongResult()

    object EmptyPerformer : NewSongResult()

    object EmptyLyrics : NewSongResult()

    object Valid : NewSongResult()

    object ErrorAddingSong : NewSongResult()
}
