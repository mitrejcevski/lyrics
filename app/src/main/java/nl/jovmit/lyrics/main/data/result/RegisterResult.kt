package nl.jovmit.lyrics.main.data.result

import nl.jovmit.lyrics.main.data.user.User

sealed class RegisterResult {

    data class Loading(val loading: Boolean) : RegisterResult()

    data class Registered(val user: User) : RegisterResult()
}
