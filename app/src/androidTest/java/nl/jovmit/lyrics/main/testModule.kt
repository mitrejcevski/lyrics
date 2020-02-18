package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.common.AppCoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.main.add.NewSongRepository
import nl.jovmit.lyrics.main.add.NewSongViewModel
import nl.jovmit.lyrics.main.details.SongDetailsViewModel
import nl.jovmit.lyrics.main.edit.UpdateSongViewModel
import nl.jovmit.lyrics.main.overview.SongsRepository
import nl.jovmit.lyrics.main.overview.SongsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

fun testModuleWithCustomSongsService(songsService: SongsService): Module {
    return module {
        single<CoroutineDispatchers> { AppCoroutineDispatchers() }
        single { songsService }
        factory { NewSongRepository(get()) }
        factory { SongsRepository(get()) }
        viewModel { NewSongViewModel(get(), get()) }
        viewModel { InfoViewModel() }
        viewModel { SongsViewModel(get(), get()) }
        viewModel { SongDetailsViewModel(get(), get()) }
        viewModel { UpdateSongViewModel(get(), get()) }
    }
}