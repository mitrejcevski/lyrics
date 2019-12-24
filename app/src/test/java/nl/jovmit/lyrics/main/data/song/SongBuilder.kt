package nl.jovmit.lyrics.main.data.song

class SongBuilder {

    private var songTitle: String = ""
    private var songPerformer: String = ""
    private var songLyrics: String = ""

    companion object {
        fun aSong(): SongBuilder {
            return SongBuilder()
        }
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
            SongTitle(songTitle),
            SongPerformer(songPerformer),
            SongLyrics(songLyrics)
        )
    }
}
