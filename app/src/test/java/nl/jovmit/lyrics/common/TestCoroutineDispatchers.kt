package nl.jovmit.lyrics.common

import kotlinx.coroutines.Dispatchers

class TestCoroutineDispatchers : CoroutineDispatchers {

    override val ui = Dispatchers.Unconfined

    override val background = Dispatchers.Unconfined
}
