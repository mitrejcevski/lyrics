package nl.jovmit.lyrics.main.preferences

import nl.jovmit.lyrics.main.data.user.User
import nl.jovmit.lyrics.utils.UserBuilder.Companion.aUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

abstract class PreferencesManagerContract {

    private val noUser: User? = null
    private val loggedInUser = aUser().build()

    @Test
    fun return_empty_user_result() {
        val preferencesManager = preferencesManagerWith(noUser)

        val result = preferencesManager.loggedInUser()

        assertEquals(noUser, result)
    }

    @Test
    fun return_logged_in_user() {
        val preferencesManager = preferencesManagerWith(loggedInUser)

        val result = preferencesManager.loggedInUser()

        assertEquals(loggedInUser, result)
    }

    abstract fun preferencesManagerWith(user: User?): PreferencesManager
}