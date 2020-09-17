package nl.jovmit.lyrics.main.preferences

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import nl.jovmit.lyrics.main.data.user.User
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class SharedPreferencesManagerShould : PreferencesManagerContract() {

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    override fun preferencesManagerWith(user: User?): PreferencesManager {
        user?.let {
            doReturn(it.userId).whenever(sharedPreferences).getString("userId", "")
            doReturn(it.username).whenever(sharedPreferences).getString("username", "")
            doReturn(it.password).whenever(sharedPreferences).getString("password", "")
            doReturn(it.about).whenever(sharedPreferences).getString("about", "")
        }
        return SharedPreferencesManager(sharedPreferences)
    }
}
