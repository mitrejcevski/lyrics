package nl.jovmit.lyrics.main.data.song

class SongBuilder {

    private var songId: String = ""
    private var songTitle: String = ""
    private var songPerformer: String = ""
    private var songLyrics: String = ""

    companion object {
        fun aSong(): SongBuilder {
            return SongBuilder()
        }
    }

    fun withId(songId: String): SongBuilder {
        this.songId = songId
        return this
    }

    fun withTitle(songTitle: String): SongBuilder {
        this.songTitle = songTitle
        return this
    }

    fun withPerformer(songPerformer: String): SongBuilder {
        this.songPerformer = songPerformer
        return this
    }

    fun withLyrics(songLyrics: String): SongBuilder {
        this.songLyrics = songLyrics
        return this
    }

    fun build(): Song {
        return Song(
            SongId(songId),
            SongTitle(songTitle),
            SongPerformer(songPerformer),
            SongLyrics(songLyrics)
        )
    }
}