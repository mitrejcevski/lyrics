package nl.jovmit.lyrics.main.add

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import nl.jovmit.lyrics.common.AppCoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.main.SongsRepository
import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.overview.SongsViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class NewSongScreenSpecification {

    @Mock
    private lateinit var songsService: SongsService

    private val songsOverviewModule = module {
        factory { SongsRepository(songsService) }
        single<CoroutineDispatchers> { AppCoroutineDispatchers() }
        viewModel { SongsViewModel(get(), get()) }
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        loadKoinModules(songsOverviewModule)
        runBlocking { whenever(songsService.fetchAllSongs()).thenReturn(emptyList()) }
    }

    @Test
    fun shouldShowEmptySongTitleError() {
        launchNewSongScreen {} submit {
            emptySongTitleErrorIsDisplayed()
        }
    }
}