package nl.jovmit.lyrics.main.data.song

data class SongPerformer(val name: String) {

    fun isValid(): Boolean = name.isNotBlank()
}
