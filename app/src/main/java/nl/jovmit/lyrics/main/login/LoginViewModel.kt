package nl.jovmit.lyrics.main.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.lyrics.main.data.result.LoginResult

class LoginViewModel {

    private val loginLiveData = MutableLiveData<LoginResult>()

    fun loginLiveData(): LiveData<LoginResult> {
        return loginLiveData
    }

    fun login(username: String, password: String) {

    }
}
