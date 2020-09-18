package nl.jovmit.lyrics.main.preferences

import androidx.lifecycle.ViewModel
import nl.jovmit.lyrics.main.data.user.User

class UserPreferencesViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    fun getLoggedInUser(): User? {
        return preferencesManager.loggedInUser()
    }

    fun setLoggedInUser(user: User) {
        preferencesManager.loggedInUser(user)
    }

    fun clearLoggedInUser() {
        preferencesManager.clearLoggedInUser()
    }
}
