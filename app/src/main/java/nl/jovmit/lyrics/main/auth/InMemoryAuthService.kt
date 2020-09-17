package nl.jovmit.lyrics.main.auth

import nl.jovmit.lyrics.main.data.user.LoginData
import nl.jovmit.lyrics.main.data.user.RegistrationData
import nl.jovmit.lyrics.main.data.user.User
import nl.jovmit.lyrics.main.exceptions.NetworkUnavailableException
import nl.jovmit.lyrics.main.exceptions.UsernameTakenException
import nl.jovmit.lyrics.utils.IdGenerator

class InMemoryAuthService(
    private val idGenerator: IdGenerator
) : AuthenticationService {

    private val users = mutableListOf<User>()

    override suspend fun createUser(registrationData: RegistrationData): User {
        validate(registrationData)
        val userId = idGenerator.next()
        val user = User(userId, registrationData.username, registrationData.about)
        users.add(user)
        return user
    }

    override suspend fun login(loginData: LoginData): User {
        TODO("not implemented")
    }

    private fun validate(registrationData: RegistrationData) {
        if (registrationData.isEmpty()) {
            throw NetworkUnavailableException()
        }
        if (users.any { it.username == registrationData.username }) {
            throw UsernameTakenException()
        }
    }

    private fun RegistrationData.isEmpty(): Boolean {
        return username.isBlank() && password.isBlank() && about.isBlank()
    }
}
