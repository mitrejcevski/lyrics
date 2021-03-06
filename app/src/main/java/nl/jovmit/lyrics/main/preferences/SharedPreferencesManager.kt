package nl.jovmit.lyrics.main.preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import nl.jovmit.lyrics.main.data.user.User

class SharedPreferencesManager(
    private val sharedPreferences: SharedPreferences
) : PreferencesManager {

    override fun loggedInUser(): User? {
        val userId = sharedPreferences.getString("userId", "") ?: ""
        val username = sharedPreferences.getString("username", "") ?: ""
        val password = sharedPreferences.getString("password", "") ?: ""
        val about = sharedPreferences.getString("about", "") ?: ""
        return if (userId.isNotBlank() && username.isNotBlank()) {
            User(userId, username, password, about)
        } else null
    }

    override fun loggedInUser(user: User) {
        sharedPreferences.edit {
            putString("userId", user.userId)
            putString("username", user.username)
            putString("about", user.about)
        }
    }

    override fun clearLoggedInUser() {
        sharedPreferences.edit {
            putString("userId", "")
            putString("username", "")
            putString("about", "")
        }
    }
}
