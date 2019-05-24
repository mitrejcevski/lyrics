package nl.jovmit.lyrics.common

import kotlinx.coroutines.Dispatchers

class TestCoroutineDispatchers : CoroutineDispatchers {

    override val ui = Dispatchers.Default

    override val background = Dispatchers.Default
}
