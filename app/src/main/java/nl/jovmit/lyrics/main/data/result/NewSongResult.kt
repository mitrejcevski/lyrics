package nl.jovmit.lyrics.main.data.result

sealed class NewSongResult {

    object SongAdded : NewSongResult()

    object EmptyTitle : NewSongResult()

    object EmptyPerformer : NewSongResult()

    object EmptyLyrics : NewSongResult()

    object Valid : NewSongResult()

    object ErrorAddingSong : NewSongResult()
}
