package nl.jovmit.lyrics.main.data.result

import nl.jovmit.lyrics.main.data.user.User

sealed class LoginResult {

    data class Loading(val loading: Boolean) : LoginResult()

    data class LoggedIn(val user: User) : LoginResult()

    object UserNotFoundError : LoginResult()

    object Offline : LoginResult()
}
