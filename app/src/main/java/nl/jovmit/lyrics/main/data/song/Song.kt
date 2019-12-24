package nl.jovmit.lyrics.main.data.song

import nl.jovmit.lyrics.main.data.result.NewSongResult

data class Song(
    val songTitle: SongTitle,
    val songPerformer: SongPerformer,
    val songLyric: SongLyrics
) {

    fun validate(): NewSongResult {
        return if (!songTitle.isValid()) {
            NewSongResult.EmptyTitle
        } else if (!songPerformer.isValid()) {
            NewSongResult.EmptyPerformer
        } else if (!songLyric.isValid()) {
            NewSongResult.EmptyLyrics
        } else {
            NewSongResult.Valid
        }
    }
}