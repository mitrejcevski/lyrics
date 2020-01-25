package nl.jovmit.lyrics.main.data.song

class SongDataBuilder {

    private var songTitle: String = ""
    private var songPerformer: String = ""
    private var songLyrics: String = ""

    companion object {
        fun aSong(): SongDataBuilder {
            return SongDataBuilder()
        }
    }

    fun withTitle(songTitle: String): SongDataBuilder {
        this.songTitle = songTitle
        return this
    }

    fun withPerformer(songPerformer: String): SongDataBuilder {
        this.songPerformer = songPerformer
        return this
    }

    fun withLyrics(songLyrics: String): SongDataBuilder {
        this.songLyrics = songLyrics
        return this
    }

    fun build(): SongData {
        return SongData(
            SongTitle(songTitle),
            SongPerformer(songPerformer),
            SongLyrics(songLyrics)
        )
    }
}
