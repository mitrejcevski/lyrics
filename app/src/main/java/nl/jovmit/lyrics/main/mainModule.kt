package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.common.AppCoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.main.overview.SongsOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single<CoroutineDispatchers> { AppCoroutineDispatchers() }
    factory { SongsRepository(FirebaseSongsService()) }
    viewModel { SongsOverviewViewModel(get(), get()) }
}
