package nl.jovmit.lyrics.main.preferences

import nl.jovmit.lyrics.main.data.user.User

interface PreferencesManager {

    fun loggedInUser(): User?

    fun loggedInUser(user: User)

    fun clearLoggedInUser()
}
