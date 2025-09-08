package nl.jovmit.lyrics.main.auth

import nl.jovmit.lyrics.main.data.user.LoginData
import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.data.user.User
import nl.jovmit.lyrics.main.exceptions.NetworkUnavailableException
import nl.jovmit.lyrics.main.exceptions.UserNotFoundException
import nl.jovmit.lyrics.main.exceptions.UsernameTakenException
import nl.jovmit.lyrics.utils.IdGenerator

class InMemoryAuthService(
    private val idGenerator: IdGenerator
) : AuthenticationService {

    private var isUnavailable: Boolean = false
    private var isOffline: Boolean = false
    private val users = mutableListOf<User>()

    override suspend fun createUser(registrationData: RegistrationData): User {
        if (isUnavailable) throw UsernameTakenException()
        if (isOffline) throw NetworkUnavailableException()
        validate(registrationData)
        val userId = idGenerator.next()
        val user = with(registrationData) { User(userId, username, password, about) }
        users.add(user)
        return user
    }

    override suspend fun login(loginData: LoginData): User {
        if (isOffline) throw NetworkUnavailableException()
        validate(loginData)
        val result = users.find { it.username == loginData.username }
        return if (result?.password == loginData.password) {
            result
        } else {
            throw UserNotFoundException()
        }
    }

    fun setUnavailable() {
        isUnavailable = true
    }

    fun setOffline() {
        isOffline = true
    }

    private fun validate(registrationData: RegistrationData) {
        if (registrationData.isEmpty()) {
            throw NetworkUnavailableException()
        }
        if (users.any { it.username == registrationData.username }) {
            throw UsernameTakenException()
        }
    }

    private fun validate(loginData: LoginData) {
        if (loginData.isEmpty()) {
            throw NetworkUnavailableException()
        }
    }

    private fun RegistrationData.isEmpty(): Boolean {
        return username.isBlank() && password.isBlank() && about.isBlank()
    }

    private fun LoginData.isEmpty(): Boolean {
        return username.isEmpty() && password.isEmpty()
    }
}
