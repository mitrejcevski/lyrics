package nl.jovmit.lyrics.common

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {

    val ui: CoroutineDispatcher

    val background: CoroutineDispatcher
}
