package nl.jovmit.lyrics.main.data.result

sealed class InfoResult {

    data class Success(val message: String) : InfoResult()

    data class Error(val message: String) : InfoResult()
}
