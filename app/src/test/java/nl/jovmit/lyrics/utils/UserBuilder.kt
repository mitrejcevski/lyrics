package nl.jovmit.lyrics.utils

import nl.jovmit.lyrics.main.data.user.User
import java.util.*

class UserBuilder {

    companion object {

        fun aUser(): UserBuilder {
            return UserBuilder()
        }
    }

    private var userId = UUID.randomUUID().toString()
    private var username = "username"
    private var about = "about"

    fun withUserId(userId: String): UserBuilder {
        this.userId = userId
        return this
    }

    fun withUsername(username: String): UserBuilder {
        this.username = username
        return this
    }

    fun withAbout(about: String): UserBuilder {
        this.about = about
        return this
    }

    fun build(): User {
        return User(userId, username, about)
    }
}
