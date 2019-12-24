package nl.jovmit.lyrics.main.data.song

data class SongLyrics(val lyrics: String) {

    fun isValid(): Boolean = lyrics.isNotBlank()
}
