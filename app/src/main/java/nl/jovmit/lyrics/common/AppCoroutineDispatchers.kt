package nl.jovmit.lyrics.common

import kotlinx.coroutines.Dispatchers

class AppCoroutineDispatchers : CoroutineDispatchers {

    override val ui = Dispatchers.Main

    override val background = Dispatchers.IO
}