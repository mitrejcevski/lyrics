package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.common.AppCoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.main.add.NewSongRepository
import nl.jovmit.lyrics.main.add.NewSongViewModel
import nl.jovmit.lyrics.main.details.SongDetailsViewModel
import nl.jovmit.lyrics.main.overview.SongsRepository
import nl.jovmit.lyrics.main.overview.SongsViewModel
import nl.jovmit.lyrics.utils.IdGenerator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single<CoroutineDispatchers> { AppCoroutineDispatchers() }
    single { IdGenerator() }
    single<SongsService> { InMemorySongsService(get()) }
    factory { SongsRepository(get()) }
    factory { NewSongRepository(get()) }
    viewModel { SongsViewModel(get(), get()) }
    viewModel { NewSongViewModel(get(), get()) }
    viewModel { InfoViewModel() }
    viewModel { SongDetailsViewModel(get(), get()) }
}
