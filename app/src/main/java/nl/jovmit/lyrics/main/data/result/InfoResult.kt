package nl.jovmit.lyrics.main.data.result

sealed class InfoResult {

    data class Success(val message: String) : InfoResult()
}
