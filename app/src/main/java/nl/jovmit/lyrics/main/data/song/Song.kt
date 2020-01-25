package nl.jovmit.lyrics.main.data.song

data class Song(
    val songId: SongId,
    val songTitle: SongTitle,
    val songPerformer: SongPerformer,
    val songLyric: SongLyrics
)