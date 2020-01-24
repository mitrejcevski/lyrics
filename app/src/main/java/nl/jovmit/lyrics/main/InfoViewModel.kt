package nl.jovmit.lyrics.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.jovmit.lyrics.main.data.result.InfoResult

class InfoViewModel : ViewModel() {

    private val infoLiveData = MutableLiveData<InfoResult>()

    fun infoLiveData(): LiveData<InfoResult> = infoLiveData

    fun showInfo(infoMessage: String) {
        infoLiveData.value = InfoResult.Success(infoMessage)
    }

    fun showError(errorMessage: String) {
        infoLiveData.value = InfoResult.Error(errorMessage)
    }
}
