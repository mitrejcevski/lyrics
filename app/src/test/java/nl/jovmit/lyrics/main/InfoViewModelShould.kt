package nl.jovmit.lyrics.main

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import nl.jovmit.lyrics.InstantTaskExecutorExtension
import nl.jovmit.lyrics.main.data.result.InfoResult
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class, InstantTaskExecutorExtension::class)
class InfoViewModelShould {

    @Mock
    private lateinit var infoLiveDataObserver: Observer<InfoResult>

    private lateinit var infoViewModel: InfoViewModel

    @BeforeEach
    fun set_up() {
        infoViewModel = InfoViewModel()
        infoViewModel.infoLiveData().observeForever(infoLiveDataObserver)
    }

    @Test
    fun deliver_new_success_message() {
        val infoMessage = "Success"
        val infoResult = InfoResult.Success(infoMessage)

        infoViewModel.showInfo(infoMessage)

        verify(infoLiveDataObserver).onChanged(infoResult)
    }

    @Test
    fun deliver_new_error_message() {
        val errorMessage = "Error"
        val infoResult = InfoResult.Error(errorMessage)

        infoViewModel.showError(errorMessage)

        verify(infoLiveDataObserver).onChanged(infoResult)
    }
}