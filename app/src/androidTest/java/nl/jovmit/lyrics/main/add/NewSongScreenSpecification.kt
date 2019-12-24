package nl.jovmit.lyrics.main.add

import androidx.test.ext.junit.runners.AndroidJUnit4
import nl.jovmit.lyrics.common.AppCoroutineDispatchers
import nl.jovmit.lyrics.main.InMemorySongsService
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class NewSongScreenSpecification {

    private val newSongModule = module {
        val dispatchers = AppCoroutineDispatchers()
        val newSongRepository = NewSongRepository(InMemorySongsService())
        viewModel {
            NewSongViewModel(dispatchers, newSongRepository)
        }
    }

    @Before
    fun setUp() {
        loadKoinModules(newSongModule)
    }

    @Test
    fun should_show_empty_song_title_error() {
        launchNewSongScreen {} submit {
            emptySongTitleErrorIsDisplayed()
        }
    }

    @After
    fun tearDown() {
        unloadKoinModules(newSongModule)
    }
}