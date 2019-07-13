package nl.jovmit.lyrics.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

class TestCoroutineDispatchers : CoroutineDispatchers {

    @ExperimentalCoroutinesApi
    override val ui = Dispatchers.Unconfined

    @ExperimentalCoroutinesApi
    override val background = Dispatchers.Unconfined
}
