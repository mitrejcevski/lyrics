package nl.jovmit.lyrics.main.data.song

data class SongTitle(val value: String) {

    fun isValid(): Boolean = value.isNotBlank()
}
